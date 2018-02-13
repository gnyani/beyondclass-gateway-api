package com.engineering.Application.Controller

import api.Subject
import api.teacherstudentspace.TeacherAssignmentUpload
import api.user.User
import com.engineering.core.Service.ServiceUtilities
import com.engineering.core.Service.NotificationService
import com.mongodb.gridfs.GridFSDBFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

/**
 * Created by GnyaniMac on 15/08/17.
 */

@RestController
class TeacherAssignmentRestController {

    @Autowired
    ServiceUtilities serviceUtils;

    @Autowired
    @Qualifier("TeacherAssignmentUpload")
    GridFsTemplate gridFsTemplate

    @Value('${engineering.everything.host}')
    private String servicehost;

    @Autowired
    NotificationService notificationService

    @ResponseBody
    @PostMapping(value="/teacher/assignments/upload")
    public ResponseEntity<?> uploadassignments (@RequestBody TeacherAssignmentUpload assignments, OAuth2Authentication auth)
    {
        String email = serviceUtils.parseEmail(auth)
        User currentuser = serviceUtils.findUserByEmail(email);
        String time = System.currentTimeMillis()
        String filename=serviceUtils.generateFileName(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),assignments.getBatch(),currentuser.getEmail(),assignments.getSubject(),time)
        InputStream inputStream = new ByteArrayInputStream(assignments.getFile())
        gridFsTemplate.store(inputStream,filename)
        //def message ="You got a new assignment from your teacher ${currentuser.firstName.toUpperCase()}"
       // notificationService.storeNotifications(currentuser,message,"teacherstudentspace",assignments.getBatch())
        log.info("<Questions>["+email+"](get all Questions)")
        new ResponseEntity<>("File uploaded successfully with filename ${filename}",HttpStatus.CREATED)
    }


    @PostMapping(value="/teacher/assignmentslist")
    public  String[] retrievedefault (@RequestBody Subject subjects, OAuth2Authentication auth)
    {
        String email = serviceUtils.parseEmail(auth)
        User currentuser = serviceUtils.findUserByEmail(email);
        String filename = serviceUtils.generateFileName(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),subjects.getTeacherclass(),email)
        Query query = new Query().addCriteria(Criteria.where("filename").regex(filename))

        def list = gridFsTemplate.find(query)
        def filelist = []
        log.info("<Questions>["+email+"](get all Questions)")
        list.each {
            filelist << "http://${servicehost}:8080/teacher/assignment/${it.getFilename()}"
        }
        filelist
    }
    @PostMapping(value="/teacher/student/assignmentslist" )
    public  String[] retrievedefaultassignments (@RequestBody Subject subjects, OAuth2Authentication auth)
    {
        String email = serviceUtils.parseEmail(auth)
        User currentuser = serviceUtils.findUserByEmail(email);
        String filename = serviceUtils.generateFileName(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),subjects.getTeacherclass())
        Query query = new Query().addCriteria(Criteria.where("filename").regex(filename))

        def list = gridFsTemplate.find(query)
        def filelist = []
        list.each {
            filelist << "http://${servicehost}:8080/teacher/assignment/${it.getFilename()}"
        }
        log.info("<Questions>["+email+"](get all Questions)")
        filelist
    }

    @GetMapping(value="/teacher/assignment/{filename:.+}",produces = "image/jpg")
    public ResponseEntity<?> retrieveAssignment(@PathVariable(value = "filename", required = true) String filename){
        byte[] file
        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput ?. writeTo(baos);
        file =baos ?. toByteArray();
        log.info("<Questions>["+email+"](get all Questions)")
        new ResponseEntity<>(file,HttpStatus.OK)
    }

    @GetMapping(value = "/teacher/assignments/{filename:.+}/delete")
    public ResponseEntity<?> deleteAssign(@PathVariable(value="filename", required = true) String filename){
        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        try {
            gridFsTemplate.delete(query)
        }catch (Exception e){
            new ResponseEntity<>("sorry something went wrong",HttpStatus.INTERNAL_SERVER_ERROR)
        }
        log.info("<Questions>["+email+"](get all Questions)")
        new ResponseEntity<>("successfully deleted",HttpStatus.OK)
    }

    @PostMapping(value="/teacher/assignment/{filename:.+}/download",produces = "application/octet-stream")
    public ResponseEntity<?> downloadAssignment(@PathVariable(value = "filename", required = true) String filename){
        byte[] file
        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput ?. writeTo(baos);
        file =baos ?. toByteArray();
        log.info("<Questions>["+email+"](get all Questions)")
        new ResponseEntity<>(file,HttpStatus.OK)
    }
}

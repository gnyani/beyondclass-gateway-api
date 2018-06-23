package com.engineering.Application.Controller

import api.Subject
import api.handouts.Handouts
import api.handouts.HandoutsResponse
import api.user.User
import com.engineering.core.Service.NotificationService
import com.engineering.core.Service.ServiceUtilities
import com.engineering.core.constants.EmailTypes
import com.engineering.core.repositories.HandoutsRepository
import com.mongodb.gridfs.GridFSDBFile
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletResponse

/**
 * Created by Gnyani on 20/06/18.
 */
@RestController
class HandoutsRestController {

    @Autowired
    @Qualifier("notes")
    GridFsTemplate gridFsTemplate

    @Autowired
    ServiceUtilities serviceUtils

    @Value('${engineering.everything.host}')
    private String servicehost;

    @Autowired
    NotificationService notificationService

    @Autowired
    ServiceUtilities serviceUtilities

    @Autowired
    HandoutsRepository handoutsRepository


    private static Logger log = LoggerFactory.getLogger(NotesRestController.class)

    @ResponseBody
    @PostMapping(value="/teacher/handouts/upload")
    public ResponseEntity<?> shareHandouts (@RequestBody Handouts handouts, OAuth2Authentication auth)
    {
        String email = serviceUtils.parseEmail(auth)
        String time = System.currentTimeMillis()

        User currentUser = serviceUtilities.findUserByEmail(email)

        def uniqueClassId = serviceUtilities.generateUniqueClassIdForTeacher(handouts.batch, email)

        String filename = serviceUtils.generateFileName(uniqueClassId,handouts.getSubject(),email,time)
        handouts.filename = filename
        def savedNotes = handoutsRepository.save(handouts)
        if(savedNotes == null){
            return  new ResponseEntity<>("Sorry Something went wrong please try again",HttpStatus.INTERNAL_SERVER_ERROR)
        }
        InputStream inputStream = new ByteArrayInputStream(handouts.getFile())
        //storing notification
        def message = "You got a handout on subject ${handouts.subject.toUpperCase()} from your teacher ${currentUser.firstName}"
        notificationService.storeNotifications(currentUser,message,"teacherstudentspace", handouts.batch)
        log.info("<handouts>[${email}](new handouts notification created)")

        //sending email
        serviceUtils.findUsersAndSendEmail(uniqueClassId, EmailTypes.HANDOUT, currentUser.firstName + currentUser.lastName)
        log.info("<handouts>[${email}](new handouts email sent")

        log.info("<handouts>["+email+"](uploaded handouts)")
        gridFsTemplate.store(inputStream,filename) ? new ResponseEntity<>("File uploaded successfully with filename ${filename}",HttpStatus.CREATED)
                : new ResponseEntity<>("Sorry something went wrong",HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ResponseBody
    @PostMapping(value="/teacher/handoutslist",produces = "application/json")
    public HandoutsResponse retrieveTeacherHandouts (@RequestBody Subject batch, OAuth2Authentication auth)
    {
        HandoutsResponse handoutsResponse = new HandoutsResponse()
        String email = serviceUtils.parseEmail(auth)
        User currentUser = serviceUtils.findUserByEmail(email)
        def uniqueClassId = serviceUtils.generateUniqueClassIdForTeacher(batch.teacherclass,email)
        String filename = serviceUtils.generateFileName(uniqueClassId,'.*',email)
        Query query = new Query().addCriteria(Criteria.where("filename").regex(filename))
        def commentsMap = generateHashMap(filename)
        def list = gridFsTemplate.find(query)
        def filelist = []
        def comments = []
        println("filename is ${filename}")
        list.each {
            comments << commentsMap.get(it.filename)
            filelist << "http://${servicehost}:8080/teacher/handouts/${it.getFilename()}"
        }
        println("comment are ${comments}")
        handoutsResponse.links = filelist
        handoutsResponse.comments = comments
        handoutsResponse.profilePicUrl = currentUser.normalpicUrl ? currentUser.normalpicUrl : currentUser.googlepicUrl
        log.info("<handouts>["+email+"](handouts list)")
        handoutsResponse
    }

    @ResponseBody
    @PostMapping(value="/student/handoutslist",produces = "application/json")
    public HandoutsResponse retrieveStudentHandouts (@RequestBody Subject subject, OAuth2Authentication auth)
    {
        HandoutsResponse handoutsResponse = new HandoutsResponse()
        String email = serviceUtils.parseEmail(auth)
        User currentUser = serviceUtils.findUserByEmail(email)
        String filename = serviceUtils.generateFileName(currentUser.uniqueclassid,subject.subject)
        Query query = new Query().addCriteria(Criteria.where("filename").regex(filename))
        def commentsMap = generateHashMap(filename)
        def list = gridFsTemplate.find(query)
        def filelist = []
        def comments = []
        list.each {
            comments << commentsMap.get(it.filename)
            filelist << "http://${servicehost}:8080/teacher/handouts/${it.getFilename()}"
        }
        handoutsResponse.links = filelist
        handoutsResponse.comments = comments
        handoutsResponse.profilePicUrl = currentUser.normalpicUrl ? currentUser.normalpicUrl : currentUser.googlepicUrl
        log.info("<handouts>["+email+"](student handouts list)")
        handoutsResponse
    }

    @ResponseBody
    @GetMapping(value = "/teacher/handouts/{filename:.+}/delete")
    public ResponseEntity<?> deleteNotes(@PathVariable(value = "filename", required = true) String filename, OAuth2Authentication auth){

        String email = serviceUtils.parseEmail(auth)
        String uploadeduser = filename.tokenize('-')[7]
        log.info("<handouts>["+email+"](deleted file "+filename+")")
        if(uploadeduser == email){
            try {
                Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
                gridFsTemplate.delete(query)
            }catch (Exception e){
                return new ResponseEntity<>("Something went wrong please try again",HttpStatus.INTERNAL_SERVER_ERROR)
            }
            return new ResponseEntity<>("Successfully Deleted",HttpStatus.OK)
        }else{
            return  new ResponseEntity<>("Not Authorized",HttpStatus.UNAUTHORIZED)
        }

    }

    @RequestMapping(value="/teacher/handouts/{filename:.+}",produces = "application/pdf" )
    public ResponseEntity<?> retrieveNotes(@PathVariable(value = "filename", required = true) String filename,OAuth2Authentication auth){

        byte[] file
        log.info("<notes>["+serviceUtils.parseEmail(auth)+"](retrieve notes)")
        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput ?. writeTo(baos);
        file =baos ?. toByteArray();
        new ResponseEntity<>(file,HttpStatus.OK);
    }

    @PostMapping(value="/teacher/handouts/{filename:.+}/download",produces = "application/octet-stream")
    public ResponseEntity<?> downloadNotes(@PathVariable(value = "filename", required = true) String filename,OAuth2Authentication auth, HttpServletResponse response){

        log.info("<notes>["+serviceUtils.parseEmail(auth)+"](download notes " +filename+ " )")
        byte[] file
        def filenameactual = filename.substring(filename.indexOf("/") + 1)
        Query query = new Query().addCriteria(Criteria.where("filename").is(filenameactual))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)

        response.setContentType("application/pdf")
        response.setHeader("Content-Disposition", "attachment; filename=handout.pdf")

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput ?. writeTo(baos);
        file =baos ?.toByteArray();
        new ResponseEntity<>(file,HttpStatus.OK)
    }

    public HashMap generateHashMap(String filename){
        List<Handouts> handouts = handoutsRepository.findByfilenameRegex(filename)
        HashMap hashMap = new HashMap()
        handouts.each {
            hashMap.put(it.filename,it.comment)
        }
        hashMap
    }

}

package com.engineering.Application.Controller

import api.Assignments
import api.Subject
import api.TeacherAssignmentUpload
import api.User
import com.engineering.core.Service.EmailGenerationService
import com.engineering.core.Service.FilenameGenerator
import com.engineering.core.repositories.UserRepository
import com.mongodb.gridfs.GridFSDBFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

/**
 * Created by GnyaniMac on 15/08/17.
 */

@CrossOrigin(origins = ["http://localhost:8081","http://localhost:3000"])
@RestController
class TeacherAssignmentRestController {
    @Autowired
    FilenameGenerator fg;

    @Autowired
    UserRepository repository;

    @Autowired
    EmailGenerationService emailGenerationService;

    @Autowired
    @Qualifier("TeacherAssignmentUpload")
    GridFsTemplate gridFsTemplate

    @Value('${engineering.everything.host}')
    private String servicehost;

    @ResponseBody
    @RequestMapping(value="/teacher/assignments/upload",method= RequestMethod.POST)
    public String uploadassignments (@RequestBody TeacherAssignmentUpload assignments, OAuth2Authentication auth)
    {
        String email = emailGenerationService.parseEmail(auth)
        User currentuser = repository.findByEmail(email);
        String time = System.currentTimeMillis()
        String filename=fg.genericGenerator(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),assignments.getTeacherclass(),currentuser.getEmail(),assignments.getSubject(),time)
        InputStream inputStream = new ByteArrayInputStream(assignments.getFile())
        gridFsTemplate.store(inputStream,filename)

        return "File uploaded successfully with filename " + filename;
    }


    @RequestMapping(value="/teacher/assignmentslist" ,method= RequestMethod.POST)
    public  String[] retrievedefault (@RequestBody Subject subjects, OAuth2Authentication auth)
    {
        String email = emailGenerationService.parseEmail(auth)
        User currentuser = repository.findByEmail(email);
        String filename = fg.genericGenerator(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),subjects.getTeacherclass(),email)
        println("filename is" +filename)

        Query query = new Query().addCriteria(Criteria.where("filename").regex(filename))

        def list = gridFsTemplate.find(query)
        def filelist = []
        def links = []
        int i = 0
        list.each {
            filelist[i++] = it.getFilename();
        }
        i=0;
        //println("filelist" +filelist)
        filelist.each {
            println(it);
            links[i++] = "http://"+servicehost+":8080/teacher/assignment/"+it;
        }
        return links;
    }
    @RequestMapping(value="/teacher/student/assignmentslist" ,method= RequestMethod.POST)
    public  String[] retrievedefaultassignments (@RequestBody Subject subjects, OAuth2Authentication auth)
    {
        String email = emailGenerationService.parseEmail(auth)
        User currentuser = repository.findByEmail(email);
        String filename = fg.genericGenerator(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),subjects.getTeacherclass())
        println("filename is" +filename)

        Query query = new Query().addCriteria(Criteria.where("filename").regex(filename))

        def list = gridFsTemplate.find(query)
        def filelist = []
        def links = []
        int i = 0
        list.each {
            filelist[i++] = it.getFilename();
        }
        i=0;
        //println("filelist" +filelist)
        filelist.each {
            println(it);
            links[i++] = "http://"+servicehost+":8080/teacher/assignment/"+it;
        }
        return links;
    }

    @RequestMapping(value="/teacher/assignment/{filename:.+}",produces = "image/jpg")
    public byte[] retrieveAssignment(@PathVariable(value = "filename", required = true) Object filename){
        byte[] file = null;
        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput ?. writeTo(baos);
        file =baos ?. toByteArray();
        return file;
    }

    @RequestMapping(value = "/teacher/assignments/{filename:.+}/delete", method = RequestMethod.GET)
    public String deleteAssign(@PathVariable(value="filename", required = true) String filename){
        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        try {
            gridFsTemplate.delete(query)
        }catch (Exception e){
            return "sorry something went wrong"
        }
        return "successfully deleted"
    }

    @RequestMapping(value="/teacher/assignment/{filename:.+}/download",produces = "application/octet-stream",method = RequestMethod.POST)
    public byte[] downloadAssignment(@PathVariable(value = "filename", required = true) String filename){

        byte[] file = null;
        // get image file by it's filename
        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput ?. writeTo(baos);
        file =baos ?. toByteArray();
        return file;
    }


}

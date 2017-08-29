package com.engineering.Application.Controller

import api.Subject
import api.Assignments
import api.User
import com.engineering.core.Service.EmailGenerationService
import com.engineering.core.Service.FilenameGenerator
import com.engineering.core.Service.NotificationService
import com.engineering.core.repositories.UserRepository
import com.mongodb.gridfs.GridFSDBFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController




/**
 * Created by GnyaniMac on 05/05/17.
 */
@RestController
class AssignmentsRestController {
    @Autowired
    FilenameGenerator fg;

    @Autowired
    UserRepository repository;

    @Autowired
    EmailGenerationService emailGenerationService;

    @Autowired
    @Qualifier("assignments")
    GridFsTemplate gridFsTemplate

    @Value('${engineering.everything.host}')
    private String servicehost;

    @Autowired
    NotificationService notificationService


    @ResponseBody
    @RequestMapping(value="/user/assignments/upload",method= RequestMethod.POST)
    public String uploadassignments (@RequestBody Assignments assignments, OAuth2Authentication auth)
    {
        String email = emailGenerationService.parseEmail(auth)
        User currentuser = repository.findByEmail(email);
        String time = System.currentTimeMillis()
        String filename=fg.genericGenerator(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),currentuser.getSection(),currentuser.getYear(),currentuser.getSem(),assignments.getSubject(),currentuser.getEmail(),time)
        InputStream inputStream = new ByteArrayInputStream(assignments.getFile())
        gridFsTemplate.store(inputStream,filename)
        //storing notification
        def message = "You have a new Assignment on subject ${assignments.subject.toUpperCase()} from your friend ${currentuser.firstName}"
        notificationService.storeNotifications(currentuser,message,"assignments")
        return "File uploaded successfully with filename " + filename;
    }
    @RequestMapping(value="/user/assignmentslist" ,method= RequestMethod.POST)
    public  String[] retrievedefault (@RequestBody Subject subjects, OAuth2Authentication auth)
    {
        String email = emailGenerationService.parseEmail(auth)
        User currentuser = repository.findByEmail(email);
        String filename = fg.generateAssignmentNamewithoutEmail(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),currentuser.getSection(),currentuser.getYear(),currentuser.getSem(),subjects.getSubject())
        filename = filename + "-*";

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
            links[i++] = "http://"+servicehost+":8080/user/assignment/"+it;
        }
        return links;
    }

    @RequestMapping(value="/user/assignment/{filename:.+}",produces = "application/pdf" )
    public byte[] retrieveAssignment(@PathVariable(value = "filename", required = true) Object filename){

        byte[] file = null;

        // get image file by it's filename
        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput ?. writeTo(baos);
        file =baos ?. toByteArray();
        return file;
    }

    @RequestMapping(value="/user/assignment/{filename:.+}/download",produces = "application/octet-stream",method = RequestMethod.POST)
    public byte[] downloadAssignment(@PathVariable(value = "filename", required = true) Object filename){

        byte[] file = null;
        def filename1=filename.toString()
        def filenameactual = filename1.substring(filename1.indexOf("/") + 1)
        // get image file by it's filename
        Query query = new Query().addCriteria(Criteria.where("filename").is(filenameactual))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput ?. writeTo(baos);
        file =baos ?. toByteArray();
        return file;
    }


}

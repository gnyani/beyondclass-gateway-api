package com.engineering.Application.Controller

import api.Notes
import api.Subject
import api.User
import com.engineering.core.Service.EmailGenerationService
import com.engineering.core.Service.FilenameGenerator
import com.engineering.core.repositories.UserRepository
import com.mongodb.gridfs.GridFSDBFile
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.*

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by GnyaniMac on 05/05/17.
 */
@CrossOrigin(origins = ["http://localhost:8081","http://localhost:3000"])
@RestController
class NotesRestController {
    @Autowired
    FilenameGenerator fg;

    @Autowired
    UserRepository repository;

    @Autowired
    @Qualifier("notes")
    GridFsTemplate gridFsTemplate

    @Autowired
    EmailGenerationService emailGenerationService

    @Value('${engineering.everything.host}')
    private String servicehost;




    @ResponseBody
    @RequestMapping(value="/user/notes/upload",method= RequestMethod.POST)
    public String uploadanotes (@RequestBody Notes notes,OAuth2Authentication auth)
    {
        String email = emailGenerationService.parseEmail(auth)
        User currentuser = repository.findByEmail(email);
        //Using generate assignment name since both notes and assignments need the same basic functionality
        String time= System.currentTimeMillis()
        String filename=fg.genericGenerator(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),currentuser.getSection(),currentuser.getYear(),currentuser.getSem(),notes.getSubject(),currentuser.getEmail(),time)
        InputStream inputStream = new ByteArrayInputStream(notes.getFile())
        gridFsTemplate.store(inputStream,filename)

        return "File uploaded successfully with filename " + filename;
    }
    @RequestMapping(value="/user/noteslist" ,method= RequestMethod.POST)
    public  String[] retrievedefaultnotes (@RequestBody Subject subjects, HttpServletRequest request, HttpServletResponse response,OAuth2Authentication auth)
    {
        String email = emailGenerationService.parseEmail(auth)
        User currentuser = repository.findByEmail(email)
        //using generate syllabus because of generate assignment name has email
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
        filelist.each{
            links[i++] = "http://"+servicehost+":8080/user/notes/"+it;
        }

        return links;
    }

    @RequestMapping(value="/user/notes/{filename:.+}",produces = "application/pdf" )
    public byte[] retrieveNotes(@PathVariable(value = "filename", required = true) Object filename){

        byte[] file = null;

        // get image file by it's filename
        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput ?. writeTo(baos);
        file =baos ?. toByteArray();
        return file;
    }

    @RequestMapping(value="/user/notes/{filename:.+}/download",produces = "application/octet-stream",method = RequestMethod.POST)
    public byte[] downloadNotes(@PathVariable(value = "filename", required = true) Object filename){

        byte[] file = null;
        def filename1=filename.toString()
        def filenameactual = filename1.substring(filename1.indexOf("/") + 1)
        // get image file by it's filename
        Query query = new Query().addCriteria(Criteria.where("filename").is(filenameactual))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput ?. writeTo(baos);
        file =baos ?.toByteArray();
        return file;
    }
}

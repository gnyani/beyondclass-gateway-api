package com.engineering.Application.Controller

import api.notes.Notes
import api.Subject
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
import org.springframework.web.bind.annotation.*

/**
 * Created by GnyaniMac on 05/05/17.
 */

@RestController
class NotesRestController {

    @Autowired
    @Qualifier("notes")
    GridFsTemplate gridFsTemplate

    @Autowired
    ServiceUtilities serviceUtils

    @Value('${engineering.everything.host}')
    private String servicehost;

    @Autowired
    NotificationService notificationService


    @ResponseBody
    @PostMapping(value="/user/notes/upload")
    public ResponseEntity<?> uploadanotes (@RequestBody Notes notes, OAuth2Authentication auth)
    {
        String email = serviceUtils.parseEmail(auth)
        User currentuser = serviceUtils.findUserByEmail(email);
        String time= System.currentTimeMillis()
        String filename= serviceUtils.generateFileName(currentuser.getUniqueclassid(),notes.getSubject(),currentuser.getEmail(),time)
        InputStream inputStream = new ByteArrayInputStream(notes.getFile())
        //storing notification
        def message = "You have a new Notes on subject ${notes.subject.toUpperCase()} from your friend ${currentuser.firstName}"
        notificationService.storeNotifications(currentuser,message,"notes")

        gridFsTemplate.store(inputStream,filename) ? new ResponseEntity<>("File uploaded successfully with filename ${filename}",HttpStatus.CREATED)
                : new ResponseEntity<>("Sorry something went wrong",HttpStatus.INTERNAL_SERVER_ERROR)

    }
    @ResponseBody
    @PostMapping(value="/user/noteslist",produces = "application/json")
    public  String[] retrievedefaultnotes (@RequestBody Subject subjects,OAuth2Authentication auth)
    {
        String email = serviceUtils.parseEmail(auth)
        User currentuser = serviceUtils.findUserByEmail(email)
        String filename = serviceUtils.generateFileName(currentuser.getUniqueclassid(),subjects.getSubject())
        Query query = new Query().addCriteria(Criteria.where("filename").regex(filename))
        def list = gridFsTemplate.find(query)
        def filelist = []
        list.each {
            filelist << "http://${servicehost}:8080/user/notes/${it.getFilename()}"
        }

        filelist
    }

    @RequestMapping(value="/user/notes/{filename:.+}",produces = "application/pdf" )
    public ResponseEntity<?> retrieveNotes(@PathVariable(value = "filename", required = true) String filename){

        byte[] file
        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput ?. writeTo(baos);
        file =baos ?. toByteArray();
        new ResponseEntity<>(file,HttpStatus.OK);
    }

    @PostMapping(value="/user/notes/{filename:.+}/download",produces = "application/octet-stream")
    public ResponseEntity<?> downloadNotes(@PathVariable(value = "filename", required = true) String filename){

        byte[] file
        def filenameactual = filename.substring(filename.indexOf("/") + 1)
        Query query = new Query().addCriteria(Criteria.where("filename").is(filenameactual))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput ?. writeTo(baos);
        file =baos ?.toByteArray();
        new ResponseEntity<>(file,HttpStatus.OK);
    }
}

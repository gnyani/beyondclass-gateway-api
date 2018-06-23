package com.engineering.Application.Controller

import api.notes.Notes
import api.Subject
import api.notes.NotesResponse
import api.user.User
import com.engineering.core.Service.ServiceUtilities
import com.engineering.core.Service.NotificationService
import com.engineering.core.repositories.NotesRepository
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
import org.springframework.web.bind.annotation.*

import javax.servlet.http.HttpServletResponse

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

    @Autowired
    NotesRepository notesRepository

    private static Logger log = LoggerFactory.getLogger(NotesRestController.class)

    @ResponseBody
    @PostMapping(value="/user/notes/upload")
    public ResponseEntity<?> uploadanotes (@RequestBody Notes notes, OAuth2Authentication auth)
    {
        String email = serviceUtils.parseEmail(auth)
        User currentuser = serviceUtils.findUserByEmail(email);
        String time = System.currentTimeMillis()
        String filename = serviceUtils.generateFileName(currentuser.getUniqueclassid(),notes.getSubject(),currentuser.getEmail(),time)
        notes.filename = filename
        def savedNotes = notesRepository.save(notes)
        if(savedNotes == null){
            return  new ResponseEntity<>("Sorry Something went wrong please try again",HttpStatus.INTERNAL_SERVER_ERROR)
        }
        InputStream inputStream = new ByteArrayInputStream(notes.getFile())
        //storing notification
        def message = "You have a new Notes on subject ${notes.subject.toUpperCase()} from your friend ${currentuser.firstName}"
        notificationService.storeNotifications(currentuser,message,"notes")
        log.info("<notes>["+email+"](uploaded notes)")
        gridFsTemplate.store(inputStream,filename) ? new ResponseEntity<>("File uploaded successfully with filename ${filename}",HttpStatus.CREATED)
                : new ResponseEntity<>("Sorry something went wrong",HttpStatus.INTERNAL_SERVER_ERROR)

    }
    @ResponseBody
    @PostMapping(value="/user/noteslist",produces = "application/json")
    public NotesResponse retrievedefaultnotes (@RequestBody Subject subjects, OAuth2Authentication auth)
    {
        NotesResponse notesResponse = new NotesResponse()
        String email = serviceUtils.parseEmail(auth)
        User currentuser = serviceUtils.findUserByEmail(email)
        String filename = serviceUtils.generateFileName(currentuser.getUniqueclassid(),subjects.getSubject())
        Query query = new Query().addCriteria(Criteria.where("filename").regex(filename))
        def commentsMap = generateHashMap(filename)
        def list = gridFsTemplate.find(query)
        def filelist = []
        def comments = []
        list.each {
            comments << commentsMap.get(it.filename)
            filelist << "http://${servicehost}:8080/user/notes/${it.getFilename()}"
        }
        notesResponse.links = filelist
        notesResponse.comments = comments
        notesResponse.profilePicUrl = currentuser.normalpicUrl ? currentuser.normalpicUrl : currentuser.googlepicUrl
        log.info("<notes>["+serviceUtils.parseEmail(auth)+"](notes list)")
        notesResponse
    }

    @ResponseBody
    @GetMapping(value = "/user/notes/{filename:.+}/delete")
    public ResponseEntity<?> deleteNotes(@PathVariable(value = "filename", required = true) String filename,OAuth2Authentication auth){

        String email = serviceUtils.parseEmail(auth)
        String uploadeduser = filename.tokenize('-')[7]
        log.info("<notes>["+email+"](deleted file "+filename+")")
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



    @GetMapping(value="/user/notes/{filename:.+}",produces = "application/pdf" )
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

    @PostMapping(value="/user/notes/{filename:.+}/download",produces = "application/octet-stream")
    public ResponseEntity<?> downloadNotes(@PathVariable(value = "filename", required = true) String filename,OAuth2Authentication auth, HttpServletResponse response){

        log.info("<notes>["+serviceUtils.parseEmail(auth)+"](download notes " +filename+ " )")
        byte[] file
        def filenameactual = filename.substring(filename.indexOf("/") + 1)
        Query query = new Query().addCriteria(Criteria.where("filename").is(filenameactual))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)

        response.setContentType("application/pdf")
        response.setHeader("Content-Disposition", "attachment; filename=notes.pdf")

        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        imageForOutput ?. writeTo(baos)
        file =baos ?.toByteArray()
        new ResponseEntity<>(file,HttpStatus.OK)
    }

     public HashMap generateHashMap(String filename){
         List<Notes> notes = notesRepository.findByfilenameStartingWith(filename)
         HashMap hashMap = new HashMap()
         notes.each {
             hashMap.put(it.filename,it.comment)
         }
         hashMap
     }
}

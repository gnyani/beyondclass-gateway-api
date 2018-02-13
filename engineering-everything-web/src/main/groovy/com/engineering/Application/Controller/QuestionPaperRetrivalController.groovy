package com.engineering.Application.Controller

import com.engineering.core.Service.ServiceUtilities
import api.questionpapers.Questionpaper
import api.questionpapers.QuestionPaperSubject
import api.user.User
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

/**
 * Created by GnyaniMac on 01/05/17.
 */

@RestController
class QuestionPaperRetrivalController {

    @Autowired
    ServiceUtilities serviceUtils

    @Autowired
    @Qualifier("questionpapers")
    GridFsTemplate gridFsTemplate

    @Value('${engineering.everything.host}')
    private String servicehost;

    private static Logger log = LoggerFactory.getLogger(QuestionPaperRetrivalController.class)




    @ResponseBody
    @RequestMapping(value="/user/questionpaper/{filename:.+}",produces= "application/pdf" )
    public ResponseEntity<?> retrievedefault (@PathVariable(value = "filename", required = true) Object filename, OAuth2Authentication auth)
    {
        byte[] file
        GridFSDBFile imageForOutput = getGridFsFile(filename)
        file=convertToByteStream(imageForOutput)
        log.info("<Questions>["+serviceUtils.parseEmail(auth)+"](get all Questions)")
        new ResponseEntity<>(file,HttpStatus.OK)
    }

    @ResponseBody
    @PostMapping(value="/user/questionpaper/{filename:.+}/download",produces = "application/octet-stream")
    public ResponseEntity<?> downloadQp (@PathVariable(value = "filename", required = true) Object filename, OAuth2Authentication auth)
    {
        byte[] file;
        log.info("<Questions>["+serviceUtils.parseEmail(auth)+"](get all Questions)")
        GridFSDBFile imageForOutput = getGridFsFile(filename)
        file= convertToByteStream(imageForOutput)
        new ResponseEntity<>(file,HttpStatus.OK)
    }

    @ResponseBody
    @PostMapping(value="/user/questionpaperurl",produces= "text/plain" )
    public ResponseEntity<?> retrieveQpurl (@RequestBody QuestionPaperSubject subject,OAuth2Authentication auth)
    {
        def url = null
        String email = serviceUtils.parseEmail(auth)
        User currentuser = serviceUtils.findUserByEmail(email);
        String filename = serviceUtils.generateFileName(currentuser.getUniversity(),currentuser.getCollege(),subject.getBranch(),subject.getSubject(),subject.getQpyear());
        GridFSDBFile imageForOutput = getGridFsFile(filename)
        if(imageForOutput)
            url = "http://${servicehost}:8080/user/questionpaper/${filename}"
        log.info("<Questions>["+serviceUtils.parseEmail(auth)+"](get all Questions)")
        url? new ResponseEntity<>(url,HttpStatus.OK) : new ResponseEntity<>("Not found",HttpStatus.NOT_FOUND)
    }


    private GridFSDBFile getGridFsFile(filename) {
        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)
        imageForOutput
    }

    private byte[] convertToByteStream(GridFSDBFile imageForOutput) {
        byte[] file
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput?.writeTo(baos);
        file = baos.toByteArray()
        file
    }

}

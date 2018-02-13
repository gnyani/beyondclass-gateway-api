package com.engineering.Application.Controller

import api.Subject
import api.syllabus.Syllabus
import api.user.User
import com.engineering.core.Service.ServiceUtilities
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


/**
 * Created by GnyaniMac on 01/05/17.
 */
@RestController
class SyllabusRetrivalController {


    @Autowired
    ServiceUtilities serviceUtils;

    @Autowired
    @Qualifier("syllabus")
    GridFsTemplate gridFsTemplate

    @Value('${engineering.everything.host}')
    private String servicehost;

    Logger log = LoggerFactory.getLogger(SyllabusRetrivalController.class)


    @ResponseBody
    @PostMapping(value="/user/syllabusurl",produces = "text/plain")
    public ResponseEntity<?> retrievedefault (@RequestBody Syllabus syllabus, OAuth2Authentication auth)
    {
        def url = null
        String email = serviceUtils.parseEmail(auth)
        User currentuser = serviceUtils.findUserByEmail(email);
        String filename = serviceUtils.generateFileName(currentuser.getUniversity(),currentuser.getCollege(),syllabus.getBranch(),syllabus.getSubject())
        GridFSDBFile imageForOutput = getGridFsFile(filename)
        log.info("<Questions>["+serviceUtils.parseEmail(auth)+"](get all Questions)")
        if(imageForOutput)
            url = "http://${servicehost}:8080/user/syllabus/${filename}"
        url? new ResponseEntity<>(url,HttpStatus.OK) : new ResponseEntity<>("Not found",HttpStatus.NOT_FOUND)
    }

    @ResponseBody
    @PostMapping(value="/user/syllabus/{filename:.+}/download",produces = "application/octet-stream" )
    public ResponseEntity<?> downloadSyllabus (@PathVariable(value = "filename", required = true) Object filename, OAuth2Authentication auth)
    {
        byte[] file
        GridFSDBFile imageForOutput = getGridFsFile(filename)
        file = convertToByteStream(imageForOutput)
        log.info("<Questions>["+serviceUtils.parseEmail(auth)+"](get all Questions)")
        new ResponseEntity<>(file,HttpStatus.OK)
    }



    @ResponseBody
    @RequestMapping(value="/user/syllabus/{filename:.+}",produces = "application/pdf")
    public ResponseEntity<?> retrievedefault (@PathVariable(value = "filename", required = true) Object filename,OAuth2Authentication auth)
    {
        byte[] file
        GridFSDBFile imageForOutput = getGridFsFile(filename)
        file=convertToByteStream(imageForOutput)
        log.info("<Questions>["+serviceUtils.parseEmail(auth)+"](get all Questions)")
        new ResponseEntity<>(file,HttpStatus.OK)
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

package com.engineering.Application.Controller

import api.Subject
import api.syllabus.Syllabus
import api.user.User
import com.engineering.core.Service.ServiceUtilities
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


    @ResponseBody
    @PostMapping(value="/user/syllabusurl",produces = "text/plain")
    public ResponseEntity<?> retrievedefault (@RequestBody Syllabus syllabus, OAuth2Authentication auth)
    {
        String email = serviceUtils.parseEmail(auth)
        User currentuser = serviceUtils.findUserByEmail(email);
        String filename = serviceUtils.generateFileName(currentuser.getUniversity(),currentuser.getCollege(),syllabus.getBranch(),syllabus.getSubject())
        def url = "http://${servicehost}:8080/user/syllabus/${filename}"
        new ResponseEntity<>(url,HttpStatus.OK)
    }
    @ResponseBody
    @PostMapping(value="/user/syllabus/{filename:.+}/download",produces = "application/octet-stream" )
    public ResponseEntity<?> downloadSyllabus (@PathVariable(value = "filename", required = true) Object filename)
    {
        byte[] file
        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput ?. writeTo(baos);
        file=baos.toByteArray()

        new ResponseEntity<>(file,HttpStatus.OK)
    }


    @ResponseBody
    @GetMapping(value="/user/syllabus/{filename:.+}",produces = "image/jpg")
    public ResponseEntity<?> retrievedefault (@PathVariable(value = "filename", required = true) Object filename)
    {
        byte[] file
        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput ?. writeTo(baos);
        file=baos.toByteArray()

        new ResponseEntity<>(file,HttpStatus.OK)
    }

}

package com.engineering.Application.Controller

import api.Subject
import api.Syllabus
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
 * Created by GnyaniMac on 01/05/17.
 */
@CrossOrigin(origins = ["http://localhost:8081","http://localhost:3000"])
@RestController
class SyllabusRetrivalController {

    @Autowired
    FilenameGenerator fg;

    @Autowired
    public UserRepository repository;

    @Autowired
    EmailGenerationService emailGenerationService;

    @Autowired
    @Qualifier("syllabus")
    GridFsTemplate gridFsTemplate

    @Value('${engineering.everything.host}')
    private String servicehost;


    @ResponseBody
    @RequestMapping(value="/user/syllabusurl/other",method= RequestMethod.POST)
    public Object retriveotherurl (@RequestBody Syllabus qp)
    {
        String filename=fg.generateSyllabusName(qp.getUniversity(),qp.getCollege(),qp.getBranch(),qp.getYear(),qp.getSem(),qp.getSubject())
        def url = "http://"+servicehost+":8080/user/syllabus/"+filename
        return url
    }

    @ResponseBody
    @RequestMapping(value="/user/syllabusurl",produces = "text/plain" ,method= RequestMethod.POST)
    public Object retrievedefault (@RequestBody Subject subject, OAuth2Authentication auth)
    {
        String email = emailGenerationService.parseEmail(auth)
        User currentuser = repository.findByEmail(email);
        String filename = fg.generateSyllabusName(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),currentuser.getYear(),currentuser.getSem(),subject.getSubject())
        def url = "http://"+servicehost+":8080/user/syllabus/"+filename
        return url
    }
    @ResponseBody
    @RequestMapping(value="/user/syllabus/{filename:.+}/download",produces = "application/octet-stream" ,method= RequestMethod.POST)
    public byte[] downloadSyllabus (@PathVariable(value = "filename", required = true) Object filename)
    {
        byte[] file = null;
        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput ?. writeTo(baos);
        file=baos.toByteArray()

        return file
    }


    @ResponseBody
    @RequestMapping(value="/user/syllabus/{filename:.+}",produces = "image/jpg" ,method= RequestMethod.GET)
    public byte[] retrievedefault (@PathVariable(value = "filename", required = true) Object filename)
    {
        byte[] file = null;
        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput ?. writeTo(baos);
        file=baos.toByteArray()

        return file
    }

}

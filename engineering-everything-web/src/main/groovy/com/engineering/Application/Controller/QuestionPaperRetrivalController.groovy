package com.engineering.Application.Controller

import com.engineering.core.Service.EmailGenerationService
import com.engineering.core.Service.FilenameGenerator
import api.Questionpaper
import api.QuestionPaperSubject
import api.User
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
import com.engineering.core.repositories.UserRepository


/**
 * Created by GnyaniMac on 01/05/17.
 */

@RestController
class QuestionPaperRetrivalController {


    @Autowired
    FilenameGenerator fg;

    @Autowired
    UserRepository repository;

    @Autowired
    EmailGenerationService emailGenerationService

    @Autowired
    @Qualifier("questionpapers")
    GridFsTemplate gridFsTemplate

    @Value('${engineering.everything.host}')
    private String servicehost;


    @ResponseBody
    @RequestMapping(value="/user/questionpaperurl/other",produces= "text/plain" ,method= RequestMethod.POST)
    public Object retriveotherQpurl (@RequestBody Questionpaper qp)
    {
        String filename= fg.generateQpName(qp.getUniversity(),qp.getCollege(),qp.getBranch(),qp.getYear(),qp.getSem(),qp.getSubject(),qp.getQpyear())
        println("file name is " + filename)
        def url = "http://"+servicehost+":8080/user/questionpaper/"+filename
        return url
    }


    @ResponseBody
    @RequestMapping(value="/user/questionpaper/{filename:.+}",produces= "image/jpg" ,method= RequestMethod.GET)
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

    @ResponseBody
    @RequestMapping(value="/user/questionpaper/{filename:.+}/download",produces = "application/octet-stream" ,method= RequestMethod.POST)
    public byte[] downloadQp (@PathVariable(value = "filename", required = true) Object filename)
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
    @RequestMapping(value="/user/questionpaperurl",produces= "text/plain" ,method= RequestMethod.POST)
    public Object retrieveQpurl (@RequestBody QuestionPaperSubject subject,OAuth2Authentication auth)
    {
        String email = emailGenerationService.parseEmail(auth)
        User currentuser = repository.findByEmail(email);
        String filename = fg.generateQpName(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),currentuser.getYear(),currentuser.getSem(),subject.getSubject(),subject.getQpyear());
        // get image file by it's filename
        println("file name is " + filename)
        def url = "http://"+servicehost+":8080/user/questionpaper/"+filename
        return url
    }

}

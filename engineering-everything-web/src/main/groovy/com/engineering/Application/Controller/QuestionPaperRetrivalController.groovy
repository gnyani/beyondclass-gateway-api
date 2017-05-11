package com.engineering.Application.Controller

import com.engineering.core.Service.FilenameGenerator
import api.Questionpaper
import api.QuestionPaperSubject
import api.User
import com.mongodb.gridfs.GridFSDBFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import com.engineering.core.repositories.UserRepository

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

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
    @Qualifier("questionpapers")
    GridFsTemplate gridFsTemplate



    @ResponseBody
    @RequestMapping(value="/users/questionpapers/other",produces= "image/*e" ,method= RequestMethod.POST)
    public byte[] retriveothers (@RequestBody Questionpaper qp, HttpServletRequest request, HttpServletResponse response)
    {
        byte[] file = null;
        String filename= fg.generateQpName(qp.getUniversity(),qp.getCollege(),qp.getBranch(),qp.getYear(),qp.getSem(),qp.getSubject(),qp.getQpyear())
        // get image file by it's filename
        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput.writeTo(baos);
        file=baos.toByteArray()

        return file
    }

    @ResponseBody
    @RequestMapping(value="/users/questionpapers",produces= "image/*e" ,method= RequestMethod.POST)
    public byte[] retrievedefault (@RequestBody QuestionPaperSubject subject, HttpServletRequest request, HttpServletResponse response)
    {
        byte[] file = null;
        User userLogin = request.getSession().getAttribute("LOGGEDIN_USER");
        User currentuser = repository.findByEmail(userLogin.getEmail());
        String filename = fg.generateQpName(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),currentuser.getYear(),currentuser.getSem(),subject.getSubject(),subject.getQpyear());
        // get image file by it's filename
        println("file name is " + filename)
        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput.writeTo(baos);
        file=baos.toByteArray()

        return file
    }

}

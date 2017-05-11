package com.engineering.Application.Controller

import api.Subject
import api.Syllabus
import api.User
import com.engineering.core.Service.FilenameGenerator
import com.engineering.core.repositories.UserRepository
import com.mongodb.gridfs.GridFSDBFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.web.bind.annotation.*

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by GnyaniMac on 01/05/17.
 */

@RestController
class SyllabusRetrivalController {

    @Autowired
    FilenameGenerator fg;

    @Autowired
    public UserRepository repository;

    @Autowired
    @Qualifier("syllabus")
    GridFsTemplate gridFsTemplate


    @ResponseBody
    @RequestMapping(value="/users/syllabus/other",produces = "application/pdf" ,method= RequestMethod.POST)
    public byte[] retriveothers (@RequestBody Syllabus qp, HttpServletRequest request, HttpServletResponse response)
    {
        byte[] file = null;
        String filename=fg.generateSyllabusName(qp.getUniversity(),qp.getCollege(),qp.getBranch(),qp.getSection(),qp.getYear(),qp.getSem(),qp.getSubject())

        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput.writeTo(baos);
        file=baos.toByteArray()

        return file
    }
    @ResponseBody
    @RequestMapping(value="/users/syllabus",produces = "application/pdf" ,method= RequestMethod.POST)
    public byte[] retrievedefault (@RequestBody Subject subject, HttpServletRequest request, HttpServletResponse response)
    {
        byte[] file = null;
        User userLogin = request.getSession().getAttribute("LOGGEDIN_USER");
        User currentuser = repository.findByEmail(userLogin.getEmail());
        String filename = fg.generateSyllabusName(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),currentuser.getSection(),currentuser.getYear(),currentuser.getSem(),subject.getSubject())

        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput.writeTo(baos);
        file=baos.toByteArray()

        return file
    }

}

package com.engineering.Application.Controller

import api.Subject
import api.Assignments
import api.User
import com.engineering.core.Service.FilenameGenerator
import com.engineering.core.repositories.UserRepository
import com.mongodb.gridfs.GridFSDBFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse



/**
 * Created by GnyaniMac on 05/05/17.
 */
@RestController
class AssignmentsRestController {
    @Autowired
    FilenameGenerator fg;

    @Autowired
    UserRepository repository;

    @Autowired
    @Qualifier("assignments")
    GridFsTemplate gridFsTemplate




    @ResponseBody
    @RequestMapping(value="/users/assignments/upload",method= RequestMethod.POST)
    public String uploadassignments (@RequestBody Assignments assignments, HttpServletRequest request, HttpServletResponse response)
    {

        User userLogin = request.getSession().getAttribute("LOGGEDIN_USER");
        User currentuser = repository.findByEmail(userLogin.getEmail());
        String filename=fg.generateAssignmentName(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),currentuser.getSection(),currentuser.getYear(),currentuser.getSem(),assignments.getSubject(),currentuser.getEmail())
        InputStream inputStream = new ByteArrayInputStream(assignments.getFile())
        gridFsTemplate.store(inputStream,filename)

        return "File uploaded successfully with filename " + filename;
    }
    @RequestMapping(value="/users/assignmentslist" ,method= RequestMethod.POST)
    public  String[] retrievedefault (@RequestBody Subject subjects, HttpServletRequest request, HttpServletResponse response)
    {
        User userLogin = request.getSession().getAttribute("LOGGEDIN_USER");
        User currentuser = repository.findByEmail(userLogin.getEmail());
        String filename = fg.generateSyllabusName(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),currentuser.getSection(),currentuser.getYear(),currentuser.getSem(),subjects.getSubject())
        filename = filename + "-*";

        Query query = new Query().addCriteria(Criteria.where("filename").regex(filename))

        def list = gridFsTemplate.find(query)
        def filelist = []
        def links = []
        int i = 0
        for(GridFSDBFile fl : list)
            filelist[i++] = fl.getFilename();

        i=0;
        println("filelist" +filelist)
        for (String fl : filelist){
            println(fl);
            links[i++] = "http://localhost:8080/users/assignments?filename="+fl;
        }
        println("links are "+links.toString());

        return links;
    }

    @RequestMapping(value="/users/assignments",produces = "application/pdf" ,method= RequestMethod.GET)
    public byte[] retrieveAssignment(@RequestParam(value = "filename", required = true) Object filename){

        byte[] file = null;
        System.out.println(filename)
        // get image file by it's filename
        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput.writeTo(baos);
        file =baos.toByteArray();
        return file;
    }
}

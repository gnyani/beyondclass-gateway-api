package com.engineering.Application.Controller

import api.Notes
import api.Subject
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
 * Created by GnyaniMac on 05/05/17.
 */
@RestController
class NotesRestController {
    @Autowired
    FilenameGenerator fg;

    @Autowired
    UserRepository repository;

    @Autowired
    @Qualifier("notes")
    GridFsTemplate gridFsTemplate



    @ResponseBody
    @RequestMapping(value="/users/notes/upload",method= RequestMethod.POST)
    public String uploadanotes (@RequestBody Notes notes, HttpServletRequest request, HttpServletResponse response)
    {
        User userLogin = request.getSession().getAttribute("LOGGEDIN_USER");
        User currentuser = repository.findByEmail(userLogin.getEmail());
        //Using generate assignment name since both notes and assignments need the same basic functionality
        String filename=fg.generateAssignmentName(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),currentuser.getSection(),currentuser.getYear(),currentuser.getSem(),notes.getSubject(),currentuser.getEmail())
        InputStream inputStream = new ByteArrayInputStream(notes.getFile())
        gridFsTemplate.store(inputStream,filename)

        return "File uploaded successfully with filename " + filename;
    }
    @RequestMapping(value="/users/noteslist" ,method= RequestMethod.POST)
    public  String[] retrievedefaultnotes (@RequestBody Subject subjects, HttpServletRequest request, HttpServletResponse response)
    {
        User userLogin = request.getSession().getAttribute("LOGGEDIN_USER");
        User currentuser = repository.findByEmail(userLogin.getEmail());

        //using generate syllabus because of generate assignment name has email
        String filename = fg.generateSyllabusName(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),currentuser.getSection(),currentuser.getYear(),currentuser.getSem(),subjects.getSubject())
        filename = filename + "-*";

        Query query = new Query().addCriteria(Criteria.where("filename").regex(filename))
        def list = gridFsTemplate.find(query)
        def filelist = []
        def links = []
        int i = 0
        list.each {
            filelist[i++] = it.getFilename();
        }
        i=0;
        filelist.each{
            println(it);
            links[i++] = "http://localhost:8080/users/notes?filename="+it;
        }
        println("links are "+links.toString());

        return links;
    }

    @RequestMapping(value="/users/notes",produces = "application/pdf" ,method= RequestMethod.GET)
    public byte[] retrieveNotes(@RequestParam(value = "filename", required = true) Object filename){

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

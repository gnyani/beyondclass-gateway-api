package com.engineering.Application.Controller

import api.Notes
import api.Subject
import api.User
import com.engineering.core.Service.FilenameGenerator
import com.engineering.core.repositories.UserRepository
import com.mongodb.BasicDBObject
import com.mongodb.DB
import com.mongodb.DBCursor
import com.mongodb.Mongo
import com.mongodb.gridfs.GridFS
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSInputFile
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
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
    public UserRepository repository;

    @Value('${mongodb.host}')
    private String mongodbhost

    @Value('${mongodb.port}')
    private Integer mongoport

    @Value('${mongo.notes.db}')
    private String db

    @Value('${mongo.notes.namespace}')
    private String namespace

    @ResponseBody
    @RequestMapping(value="/users/notes/upload",method= RequestMethod.POST)
    public String uploadanotes (@RequestBody Notes notes, HttpServletRequest request, HttpServletResponse response)
    {
        byte[] file = null;

        User userLogin = request.getSession().getAttribute("LOGGEDIN_USER");
        User currentuser = repository.findByEmail(userLogin.getEmail());
        //Using generate assignment name since both notes and assignments need the same basic functionality
        String filename=new FilenameGenerator().generateAssignmentName(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),currentuser.getSection(),currentuser.getYear(),currentuser.getSem(),notes.getSubject(),currentuser.getEmail())
        Mongo mongo = new Mongo(mongodbhost,mongoport);
        DB db = mongo.getDB(db);
        // create a "photo" namespace
        GridFS gfsPhoto = new GridFS(db, namespace);

        // get image file from local drive
        GridFSInputFile gfsFile = gfsPhoto.createFile(notes.getFile());

        // set a new filename for identify purpose
        gfsFile.setFilename(filename);

        // save the image file into mongoDB
        gfsFile.save();

        return "File uploaded successfully with filename " + filename;
    }
    @RequestMapping(value="/users/noteslist" ,method= RequestMethod.POST)
    public  String[] retrievedefaultnotes (@RequestBody Subject subjects, HttpServletRequest request, HttpServletResponse response)
    {
        User userLogin = request.getSession().getAttribute("LOGGEDIN_USER");
        User currentuser = repository.findByEmail(userLogin.getEmail());

        //using generate syllabus because of generate assignment name has email
        String filename = new FilenameGenerator().generateSyllabusName(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),currentuser.getSection(),currentuser.getYear(),currentuser.getSem(),subjects.getSubject())
        filename = filename + "-*";
        Mongo mongo = new Mongo(mongodbhost, mongoport);
        DB db = mongo.getDB(db);
        // create a "photo" namespace
        GridFS gfsPhoto = new GridFS(db, namespace);

        BasicDBObject regexQuery = new BasicDBObject();
        regexQuery.put("filename",
                new BasicDBObject('$regex', filename)
                        .append('$options', "i"));

        DBCursor cursor = gfsPhoto.getFileList(regexQuery);
        def filelist = []
        def links = []
        int i = 0
        def jsonSlurper = new JsonSlurper()
        while (cursor.hasNext()) {
            String file = cursor.next()
            def object = jsonSlurper.parseText(file);
            filelist[i++] = object.filename;
        }
        i=0;
        println("filelist" +filelist)
        for (String fl : filelist){
            println(fl);
            links[i++] = "http://localhost:8080/users/notes?filename="+fl;
        }
        println("links are "+links.toString());

        return links;
    }

    @RequestMapping(value="/users/notes",produces = "application/pdf" ,method= RequestMethod.GET)
    public byte[] retrieveNotes(@RequestParam(value = "filename", required = true) Object filename){

        byte[] file = null;
        Mongo mongo = new Mongo(mongodbhost, mongoport);
        DB db = mongo.getDB(db);
        // create a "photo" namespace
        GridFS gfsPhoto = new GridFS(db, namespace);
        System.out.println(filename)
        // get image file by it's filename
        GridFSDBFile imageForOutput = gfsPhoto.findOne(filename.toString());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput.writeTo(baos);
        file =baos.toByteArray();
        return file;
    }
}

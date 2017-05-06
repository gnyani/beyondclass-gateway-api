package com.engineering.Application.Controller

import com.engineering.core.Service.FilenameGenerator
import api.Questionpaper
import api.QuestionPaperSubject
import api.User
import com.mongodb.DB
import com.mongodb.DBCursor
import com.mongodb.Mongo
import com.mongodb.gridfs.GridFS
import com.mongodb.gridfs.GridFSDBFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
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
    public UserRepository repository;

    @Value('${mongodb.host}')
    private String mongodbhost

    @Value('${mongodb.port}')
    private Integer mongoport

    @Value('${mongo.qp.db}')
    private String db

    @Value('${mongo.qp.namespace}')
    private String namespace



    @ResponseBody
    @RequestMapping(value="/users/questionpapers/other",produces= "image/*e" ,method= RequestMethod.POST)
    public byte[] retriveothers (@RequestBody Questionpaper qp, HttpServletRequest request, HttpServletResponse response)
    {
        byte[] file = null;
        String filename= fg.generateQpName(qp.getUniversity(),qp.getCollege(),qp.getBranch(),qp.getYear(),qp.getSem(),qp.getSubject(),qp.getQpyear())
        Mongo mongo = new Mongo(mongodbhost,mongoport);
        DB db = mongo.getDB(db);
        // create a "photo" namespace
        GridFS gfsPhoto = new GridFS(db,namespace);

        DBCursor cursor = gfsPhoto.getFileList();
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
        //filename = filename.toUpperCase()

        // get image file by it's filename
        GridFSDBFile imageForOutput = gfsPhoto.findOne(filename);

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
        String filename = fg.generateQpName(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),currentuser.getYear(),currentuser.getSem(),subject.getSubject(),subject.getQpyear())
        Mongo mongo = new Mongo(mongodbhost, mongoport);
        DB db = mongo.getDB(db);
        // create a "photo" namespace
        GridFS gfsPhoto = new GridFS(db,namespace);

        DBCursor cursor = gfsPhoto.getFileList();
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
        System.out.println(filename)
        // get image file by it's filename
        GridFSDBFile imageForOutput = gfsPhoto.findOne(filename);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput.writeTo(baos);
        file=baos.toByteArray()

        return file
    }

}

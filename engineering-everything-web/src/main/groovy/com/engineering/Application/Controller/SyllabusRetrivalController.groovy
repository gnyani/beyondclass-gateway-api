package com.engineering.Application.Controller

import api.Subject
import api.Syllabus
import api.User
import com.engineering.core.Service.FilenameGenerator
import com.engineering.core.repositories.UserRepository
import com.mongodb.DB
import com.mongodb.DBCursor
import com.mongodb.Mongo
import com.mongodb.gridfs.GridFS
import com.mongodb.gridfs.GridFSDBFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
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

    @Value('${mongodb.host}')
    private String mongodbhost

    @Value('${mongodb.port}')
    private Integer mongoport

    @Value('${mongo.syllabus.db}')
    private String db

    @Value('${mongo.syllabus.namespace}')
    private String namespace


    @ResponseBody
    @RequestMapping(value="/users/syllabus/other",produces = "application/pdf" ,method= RequestMethod.POST)
    public byte[] retriveothers (@RequestBody Syllabus qp, HttpServletRequest request, HttpServletResponse response)
    {
        byte[] file = null;
        String filename=fg.generateSyllabusName(qp.getUniversity(),qp.getCollege(),qp.getBranch(),qp.getSection(),qp.getYear(),qp.getSem(),qp.getSubject())
        Mongo mongo = new Mongo(mongodbhost,mongoport);
        DB db = mongo.getDB(db);
        // create a "photo" namespace
        GridFS gfsPhoto = new GridFS(db, namespace);

        DBCursor cursor = gfsPhoto.getFileList();
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }

        // get image file by it's filename
        GridFSDBFile imageForOutput = gfsPhoto.findOne(filename);

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
        Mongo mongo = new Mongo(mongodbhost, mongoport);
        DB db = mongo.getDB(db);
        // create a "photo" namespace
        GridFS gfsPhoto = new GridFS(db, namespace);

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

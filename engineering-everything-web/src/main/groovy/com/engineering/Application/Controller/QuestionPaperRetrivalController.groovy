package com.engineering.Application.Controller

import com.engineering.core.Service.DetailsValidator
import com.engineering.core.Service.filenamegenerator
import api.Questionpaper
import api.Subject
import api.User
import com.mongodb.DB
import com.mongodb.DBCursor
import com.mongodb.Mongo
import com.mongodb.gridfs.GridFS
import com.mongodb.gridfs.GridFSDBFile
import org.springframework.beans.factory.annotation.Autowired
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
    public UserRepository repository;

    DetailsValidator validation


    @ResponseBody
    @RequestMapping(value="/users/questionpapers/other",produces= "image/*e" ,method= RequestMethod.POST)
    public byte[] retriveothers (@RequestBody Questionpaper qp, HttpServletRequest request, HttpServletResponse response)
    {
        byte[] file = null;
        String filename=new filenamegenerator().generateName(qp.getUniversity(),qp.getCollege(),qp.getBranch(),qp.getSection(),qp.getYear(),qp.getSem(),qp.getSubject())
        Mongo mongo = new Mongo("localhost", 27017);
        DB db = mongo.getDB("questionpapers");
        // create a "photo" namespace
        GridFS gfsPhoto = new GridFS(db, "papers");

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
    @RequestMapping(value="/users/questionpapers",produces= "image/*e" ,method= RequestMethod.POST)
    public byte[] retrievedefault (@RequestBody Subject subject,HttpServletRequest request, HttpServletResponse response)
    {
        byte[] file = null;
        User userLogin = request.getSession().getAttribute("LOGGEDIN_USER");
        User currentuser = repository.findByEmail(userLogin.getEmail());
        String filename = new filenamegenerator().generateName(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),currentuser.getSection(),currentuser.getYear(),currentuser.getSem(),subject.getSubject())
        Mongo mongo = new Mongo("localhost", 27017);
        DB db = mongo.getDB("questionpapers");
        // create a "photo" namespace
        GridFS gfsPhoto = new GridFS(db, "papers");

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

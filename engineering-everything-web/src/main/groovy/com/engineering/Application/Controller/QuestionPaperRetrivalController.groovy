package com.engineering.Application.Controller

import Service.filenamegenerator
import api.Questionpaper
import api.UserLogin
import com.mongodb.DB
import com.mongodb.DBCursor
import com.mongodb.Mongo
import com.mongodb.gridfs.GridFS
import com.mongodb.gridfs.GridFSDBFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.config.annotation.EnableWebMvc

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by GnyaniMac on 01/05/17.
 */

@RestController
class QuestionPaperRetrivalController {


    @ResponseBody
    @RequestMapping(value="/questionpapers",produces= "image/*e" ,method= RequestMethod.POST)
    public byte[] retrive(@RequestBody Questionpaper qp, HttpServletRequest request, HttpServletResponse response)
    {

        byte[] file = null;
        String filename=new filenamegenerator().generateName(qp.getUniversity(),qp.getCollegecode(),qp.getBranch(),qp.getYear(),qp.getSem(),qp.getSubject())
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
}

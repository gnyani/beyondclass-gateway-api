package com.engineering.Application.Controller


import api.TimelinePosts
import api.TimelinePostsmetaapi
import api.User
import com.engineering.core.Service.FilenameGenerator
import com.engineering.core.repositories.TimelineRepository
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
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.time.LocalDateTime

/**
 * Created by GnyaniMac on 09/05/17.
 */

@EnableMongoRepositories(basePackageClasses = [UserRepository.class,TimelineRepository.class])
@RestController
class TimelineRestController {

    @Autowired
    FilenameGenerator fg;

    @Autowired
    public UserRepository repository;

    @Autowired
    TimelineRepository timelineRepository;

    @Value('${engineering.everything.host}')
    private String servicehost;

    @Value('${mongodb.host}')
    private String mongodbhost

    @Value('${mongodb.port}')
    private Integer mongoport

    @Value('${mongo.timeline-files.db}')
    private String db

    @Value('${mongo.timeline-files.namespace}')
    private String namespace


    @ResponseBody
    @RequestMapping(value="/users/timeline/upload",method= RequestMethod.POST)
    public String uploadposts (@RequestBody TimelinePosts post, HttpServletRequest request, HttpServletResponse response)
    {
        //Timeline meta api
        TimelinePostsmetaapi timelinePostsmetaapi = new TimelinePostsmetaapi();
        User userLogin = request.getSession().getAttribute("LOGGEDIN_USER");
        User currentuser = repository.findByEmail(userLogin.getEmail());
        // setting meta data before uploading the file
        timelinePostsmetaapi.setOwner(currentuser.getFirstName())
        timelinePostsmetaapi.setPostdateTime(LocalDateTime.now())
        timelinePostsmetaapi.setLikes(0);
        timelinePostsmetaapi.setComments(null);

        String filename=fg.generatePostname(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),currentuser.getSection(),currentuser.getYear(),currentuser.getSem(),currentuser.getEmail(),System.currentTimeMillis())
        //storing filename in meta
        timelinePostsmetaapi.setFilename(filename)
        String postUrl = "http://"+servicehost+":8080/users/timeline/view/"+filename;
        timelinePostsmetaapi.setPostUrl(postUrl);

        Mongo mongo = new Mongo(mongodbhost,mongoport);
        DB db = mongo.getDB(db);
        // create a "photo" namespace
        GridFS gfsPhoto = new GridFS(db, namespace);

        // get image file from user
        GridFSInputFile gfsFile = gfsPhoto.createFile(post.getFile());

        // set a new filename for identify purpose
        gfsFile.setFilename(filename);

        // save the image file into mongoDB
        gfsFile.save();

        def object = timelineRepository.save(timelinePostsmetaapi);

        if(object)
             return "Your post has been updated successfully " + filename;
        else
             return "failure in uploading the post " +object.toString();
    }

    @RequestMapping(value = "/users/timeline/posts",produces = "application/json",method = RequestMethod.GET)
    @ResponseBody
    public List<TimelinePostsmetaapi> allposts(HttpServletRequest request,HttpServletResponse response){

        def objectlist = []

        User user = request.getSession().getAttribute("LOGGEDIN_USER");

        String filename=fg.generatePostnamewithouttime(user.getUniversity(),user.getCollege(),user.getBranch(),user.getSection(),user.getYear(),user.getSem(),user.getEmail())

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
        int i = 0
        def jsonSlurper = new JsonSlurper()
        while (cursor.hasNext()) {
            String file = cursor.next()
            def object = jsonSlurper.parseText(file);
            filelist.add(object.filename);
        }
        for (String fl : filelist){
            def temp = timelineRepository.findByFilename(fl);
            objectlist.add(temp);
        }

        return objectlist;
    }

    @ResponseBody
    @RequestMapping(value="/users/timeline/view/{filename:.+}",produces = "image/png" ,method= RequestMethod.GET)
    public byte[] retrievePost(@PathVariable(value = "filename", required = true) Object filename){

        byte[] file = null;
        Mongo mongo = new Mongo(mongodbhost, mongoport);
        DB db = mongo.getDB(db);
        // create a "photo" namespace
        GridFS gfsPhoto = new GridFS(db, namespace);
        System.out.println("file name is "+filename)
        // get image file by it's filename
        GridFSDBFile imageForOutput = gfsPhoto.findOne(filename.toString());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput.writeTo(baos);
        file =baos.toByteArray();
        return file;
    }

}

package com.engineering.Application.Controller

import api.user.User
import com.engineering.core.Service.ServiceUtilities
import com.engineering.core.repositories.UserRepository
import com.mongodb.DB
import com.mongodb.Mongo
import com.mongodb.gridfs.GridFS
import com.mongodb.gridfs.GridFSInputFile
import groovy.io.FileType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by GnyaniMac on 16/01/18.
 */

@RestController
class UtilityRestController {

    @Autowired
    ServiceUtilities serviceUtilities

    @Autowired
    UserRepository userRepository


    @GetMapping(value = '/deamons/admin/insert')
    public ResponseEntity<?> insertUsers(@RequestParam String path){
        def file = new File(path)
        String fileName = file.getName()
        def userInfo = fileName.split('-')
        def invalidEmails = []

        file.eachLine { String line ->
            String[] splits = line.tokenize(',')
            Date date = null
       //     if(splits[4])
            //   date = new Date(splits[4])
//            def stay = false
//            if(splits[5] == "Yes" || splits[5] == "yes")
//                stay = true
           if(isValid(splits[0].trim().toLowerCase())){
               def user = new User()
               user.with{
                   email = splits[0].trim().toLowerCase()
                   mobilenumber = splits[1]?.trim()
                   firstName = splits[2].trim().capitalize()
                   lastName = splits[3]?.trim()?.capitalize()
                   dob = date
//                   hostel = stay
                   userrole = "student"
                   university = userInfo[0]
                   college = userInfo[1]
                   branch = userInfo[2]
                   section = userInfo[3]
                   startYear = userInfo[4]
                   endYear = userInfo[5].tokenize('.')[0]
                   rollNumber = splits[5]
                   enabled = true
                   accountNonExpired = true
                   accountNonLocked = true
                   credentialsNonExpired = true
               }
               user.addRole("ROLE_USER")
               def uniqueid = serviceUtilities.generateFileName(user.getUniversity(), user.getCollege(), user.getBranch(), user?.getSection(), user.getStartYear(), user.getEndYear())
               user.uniqueclassid = uniqueid
               println(user)
               userRepository.save(user)
           }else{
               invalidEmails.add(splits[0].trim().toLowerCase())
           }

        }
        println("Invalid emails are ${invalidEmails}")
        new ResponseEntity<>("success",HttpStatus.OK)
    }

    @GetMapping(value ="/deamons/admin/insert/teacher")
    public ResponseEntity<?> insertTeacher(@RequestParam String path){
        def file = new File(path)
        String fileName = file.getName()
        def userInfo = fileName.tokenize('-')
        def invalidEmails = []
        def branchToken = userInfo[2].tokenize('.')[0]
        file.eachLine {String line ->
            String[] splits = line.split(',')
            if(isValid(splits[0].trim().toLowerCase())){
            def user = new User()
            user.with{
                email = splits[0].trim().toLowerCase()
                mobilenumber = splits[1].trim()
                firstName = splits[2].trim().capitalize()
                lastName = splits[3].trim().capitalize()
                batches = splits.size() > 4 ? splits[4]?.tokenize('@') : []
                university = userInfo[0]
                college = userInfo[1]
                branch = branchToken
                enabled = true
                accountNonExpired = true
                accountNonLocked = true
                credentialsNonExpired = true
                userrole = "teacher"
            }
            user.addRole("ROLE_USER")
            userRepository.save(user)
                println(user)
        }else{
                invalidEmails.add(splits[0].trim().toLowerCase())
            }
        }
        println("Invalid emails are ${invalidEmails}")
        new ResponseEntity<>("success",HttpStatus.OK)
    }

    @GetMapping(value ="/deamons/admin/insert/syllabus")
    public ResponseEntity<?> insertSyllabus(@RequestParam String path) {

        Mongo mongo = new Mongo("localhost", 27017);
        DB db = mongo.getDB("mydatabase");
        //	DBCollection collection = db.getCollection("images");

        def list = []

        def dir = new File(path)
        dir.eachFileRecurse (FileType.FILES) { file ->
            list << file
        }
        list.each{ File file ->

            String filename = path.split('/').last()

            String [] splits = filename.split('-')


            String filenameinDb = serviceUtilities.generateFileName(splits[0], splits[1], splits[2],file.getName().tokenize('.').first())


            File imageFile = new File("${path}/${file.getName()}");


            // create a "photo" namespace
            GridFS gfsPhoto = new GridFS(db, "syllabus");

            // get image file from local drive
            GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);

            // set a new filename for identify purpose
            gfsFile.setFilename(filenameinDb);


            // save the image file into mongoDB
            gfsFile.save();

        }

        new ResponseEntity<>("success",HttpStatus.OK)
    }

    @GetMapping(value ="/deamons/admin/insert/qp")
    public ResponseEntity<?> insertQp(@RequestParam String path) {

        Mongo mongo = new Mongo("localhost", 27017);
        DB db = mongo.getDB("mydatabase");
        //	DBCollection collection = db.getCollection("images");

        def list = []

        def dir = new File(path)
        dir.eachFileRecurse (FileType.FILES) { file ->
            list << file
        }
        list.each{ File file ->

            if(file.getName() != ".DS_Store")
            {
                String year = file.getName().split('-')[4].tokenize('.').first()

                String subject = path.split('/').last()

                String filename = file.getName().tokenize('.').first()

                File imageFile = new File("${path}/${file.getName()}");

                // create a "photo" namespace
                GridFS gfsPhoto = new GridFS(db, "questionpapers");

                // get image file from local drive
                GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);

                // set a new filename for identify purpose
                gfsFile.setFilename(filename);


                // save the image file into mongoDB
                gfsFile.save();
            }

        }
        new ResponseEntity<>("success",HttpStatus.OK)
    }
    public static boolean isValid(String email)
    {
        String expression = '^[\\w.+\\-&]+@gmail\\.com$'
        CharSequence inputStr = email
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr)
        return matcher.matches()
    }
}

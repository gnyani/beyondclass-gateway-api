package com.engineering.Application.Controller

import api.Comment
import api.TimelinePosts
import api.TimelinePostsmetaapi
import api.User
import com.engineering.Application.Configuration.SpringMongoConfig
import com.engineering.core.Service.FilenameGenerator
import com.engineering.core.repositories.TimelineRepository
import com.engineering.core.repositories.UserRepository
import com.mongodb.gridfs.GridFSDBFile
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Created by GnyaniMac on 09/05/17.
 */

@EnableMongoRepositories(basePackageClasses = [UserRepository.class,TimelineRepository.class])
@CrossOrigin(origins = ["http://localhost:8081","http://localhost:3000"])
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

    @Autowired
    @Qualifier("timeline-files")
    GridFsTemplate gridFsTemplate

    JsonSlurper jsonSlurper = new JsonSlurper();


    @ResponseBody
    @RequestMapping(value="/users/timeline/upload",method= RequestMethod.POST)
    public String uploadposts (@RequestBody TimelinePosts post, OAuth2Authentication auth)
    {
        //Timeline meta api
        TimelinePostsmetaapi timelinePostsmetaapi = new TimelinePostsmetaapi();
        //user loggedin
        def m = JsonOutput.toJson( auth.getUserAuthentication().getDetails())
        def Json = jsonSlurper.parseText(m);
        String email = Json."email"
        User currentuser = repository.findByEmail(email);
        // setting meta data before uploading the file
        String propicurl = currentuser.normalpicUrl ?: currentuser.googlepicUrl
        timelinePostsmetaapi.setPropicUrl(propicurl)
        timelinePostsmetaapi.setDescription(post.getDescription())
        timelinePostsmetaapi.setOwner(currentuser.getFirstName())
        timelinePostsmetaapi.setPostdateTime(LocalDateTime.now())
        timelinePostsmetaapi.setLikes(0);
        timelinePostsmetaapi.setComments(new ArrayList<Comment>());

        String filename=fg.generatePostname(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),currentuser.getSection(),currentuser.getYear(),currentuser.getSem(),LocalDate.now(),currentuser.getEmail(),System.currentTimeMillis())
        //storing filename in meta
        timelinePostsmetaapi.setFilename(filename)
        println("filename is "+filename)
        String postUrl = post.file ? "http://"+servicehost+":8080/users/timeline/view/"+filename : null ;
        String likeUrl =  "http://"+servicehost+":8080/users/timeline/view/"+filename+"/like"
        String commentUrl =  "http://"+servicehost+":8080/users/timeline/view/"+filename+ "/comment"
        timelinePostsmetaapi.setPostUrl(postUrl);
        timelinePostsmetaapi.setLikeUrl(likeUrl);
        timelinePostsmetaapi.setCommentUrl(commentUrl);
        timelinePostsmetaapi.setUploadeduser(currentuser);
        InputStream inputStream = new ByteArrayInputStream(post.getFile())
        gridFsTemplate.store(inputStream,filename)

        def object = timelineRepository.save(timelinePostsmetaapi);
        if(object)
             return "Your post has been updated successfully " + filename;
        else
             return "failure in uploading the post " +object.toString();
    }

    @RequestMapping(value = "/users/timeline/posts",produces = "application/json",method = RequestMethod.GET)
    @ResponseBody
    public List<TimelinePostsmetaapi> allposts(@RequestParam(value="date") String date, OAuth2Authentication auth){

        def objectlist = []
        def m = JsonOutput.toJson( auth.getUserAuthentication().getDetails())
        def Json = jsonSlurper.parseText(m);
        String email = Json."email"
        User user = repository.findByEmail(email);

        String filename=fg.generatePostnamewithouttime(user.getUniversity(),user.getCollege(),user.getBranch(),user.getSection(),user.getYear(),user.getSem())

        String filenamedate = filename+"-"+date
        Query query = new Query().with(new Sort(Sort.Direction.DESC, "uploadDate")).addCriteria(Criteria.where("filename").regex(filenamedate))

        System.out.println("filename is" + filename);
        System.out.print("query is"+query);


        def list = gridFsTemplate.find(query)
        System.out.println("list is"  + list);
        def filelist = []
        int i = 0
        for(GridFSDBFile fl : list)
            filelist[i++] = fl.getFilename();

        for (String fl : filelist){
            TimelinePostsmetaapi temp = timelineRepository.findByFilename(fl);
            objectlist.add(temp);
        }
        for(TimelinePostsmetaapi temp : objectlist ){
            def postemail = temp.getUploadeduser().getEmail()
            def userfromdb = repository.findByEmail(postemail)
            def propicUrl = userfromdb.getNormalpicUrl() ?: userfromdb.getGooglepicUrl()
            def firstNameinDB = userfromdb.getFirstName()
            System.out.println("from db ${propicUrl} and from timeline repo ${temp.getPropicUrl()}" )
            if(temp.getPropicUrl().equalsIgnoreCase(propicUrl) || temp.getOwner().equalsIgnoreCase(firstNameinDB)){
            }else{
                temp.setPropicUrl(propicUrl)
                temp.setOwner(firstNameinDB)
                temp.setUploadeduser(userfromdb)
                timelineRepository.save(temp)
            }
        }

        return objectlist;
    }

    @ResponseBody
    @RequestMapping(value="/users/timeline/view/{filename:.+}",produces = "image/png" ,method= RequestMethod.GET)
    public byte[] retrievePost(@PathVariable(value = "filename", required = true) Object filename){

        byte[] file = null;
        // get image file by it's filename
        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput.writeTo(baos);
        file =baos.toByteArray();
        return file;
    }

    @ResponseBody
    @RequestMapping(value="/users/timeline/view/{filename:.+}/like",method= RequestMethod.GET)
    public String likePost(@PathVariable(value = "filename", required = true) String filename ,OAuth2Authentication auth){

        def m = JsonOutput.toJson( auth.getUserAuthentication().getDetails())
        def Json = jsonSlurper.parseText(m);
        String email = Json."email"
        def loggeduser = repository.findByEmail(email)
        TimelinePostsmetaapi timelinePostsmetaapi = timelineRepository.findByFilename(filename)
        def likes = timelinePostsmetaapi.getLikes()
        def likedusers = timelinePostsmetaapi.getLikedUsers()
        println("liked users before" + likedusers);
        println("logged user" + loggeduser.getEmail())
        def flag = true
        likedusers.each {
            if(loggeduser.getEmail() == it.getEmail()){
                flag =false
            }
        }
        if(flag){
            likes = likes + 1;
            likedusers.add(loggeduser)
            println("liked users" + likedusers)
            timelinePostsmetaapi.setLikes(likes)
            timelinePostsmetaapi.setLikedUsers(likedusers)
            timelineRepository.save(timelinePostsmetaapi)
            return "like success current likes " + likes
        }
        return "already liked"

    }

    @ResponseBody
    @RequestMapping(value="/users/timeline/view/{filename:.+}/like/likedusers",method= RequestMethod.GET)
    public List<String> likedUsers(@PathVariable(value = "filename", required = true) String filename ){
        TimelinePostsmetaapi timelinePostsmetaapi = timelineRepository.findByFilename(filename)
        def likedusers = timelinePostsmetaapi.getLikedUsers()
        def usernamesarray = [];
        likedusers.each{
            usernamesarray.add(it.getFirstName())
        }
        return usernamesarray
    }

    @ResponseBody
    @RequestMapping(value="/users/timeline/view/{filename:.+}/like/unlike",method= RequestMethod.GET)
    public String unlikePost(@PathVariable(value = "filename", required = true) String filename ,OAuth2Authentication auth){

        def m = JsonOutput.toJson( auth.getUserAuthentication().getDetails())
        def Json = jsonSlurper.parseText(m);
        String email = Json."email"
        def loggeduser = repository.findByEmail(email)
        TimelinePostsmetaapi timelinePostsmetaapi = timelineRepository.findByFilename(filename)
        def likes = timelinePostsmetaapi.getLikes()
        def likedusers = timelinePostsmetaapi.getLikedUsers()
        println("liked users before" + likedusers);
        println("logged user" + loggeduser.getEmail())
        def flag = true
        likedusers.each {
            if(loggeduser.getEmail() == it.getEmail()){
                flag =true
            }
        }
        if(flag){
            likes = likes - 1;
            likedusers.remove(loggeduser)
            println("liked users" + likedusers)
            timelinePostsmetaapi.setLikes(likes)
            timelinePostsmetaapi.setLikedUsers(likedusers)
            timelineRepository.save(timelinePostsmetaapi)
            return "like success current likes " + likes
        }
        return "not liked"

    }

    @ResponseBody
    @RequestMapping(value="/users/timeline/view/{filename:.+}/comment",method= RequestMethod.POST)
    public String CommentPost(@PathVariable(value = "filename", required = true)String filename, @RequestBody Comment comment,OAuth2Authentication auth){
        def m = JsonOutput.toJson( auth.getUserAuthentication().getDetails())
        def Json = jsonSlurper.parseText(m);
        String email = Json."email"
        def loggeduser = repository.findByEmail(email)
        TimelinePostsmetaapi timelinePostsmetaapi = timelineRepository.findByFilename(filename)
        def commentscurrent = timelinePostsmetaapi.getComments()
        comment.setUser(loggeduser);
        commentscurrent.add(comment)
        timelinePostsmetaapi.setComments(commentscurrent)
        timelineRepository.save(timelinePostsmetaapi)
        def finalcomment = loggeduser.getFirstName() +":\n"+comment.getComment()
        return finalcomment

    }


}

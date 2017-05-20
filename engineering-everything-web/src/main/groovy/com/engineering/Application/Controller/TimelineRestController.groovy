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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.gridfs.GridFsTemplate
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

    @Autowired
    @Qualifier("timeline-files")
    GridFsTemplate gridFsTemplate


    @ResponseBody
    @RequestMapping(value="/users/timeline/upload",method= RequestMethod.POST)
    public String uploadposts (@RequestBody TimelinePosts post, HttpServletRequest request, HttpServletResponse response)
    {
        //Timeline meta api
        TimelinePostsmetaapi timelinePostsmetaapi = new TimelinePostsmetaapi();
        User userLogin = request.getSession().getAttribute("LOGGEDIN_USER");
        User currentuser = repository.findByEmail(userLogin.getEmail());
        // setting meta data before uploading the file
        timelinePostsmetaapi.setDescription(post.getDescription())
        timelinePostsmetaapi.setOwner(currentuser.getFirstName())
        timelinePostsmetaapi.setPostdateTime(LocalDateTime.now())
        timelinePostsmetaapi.setLikes(0);
        timelinePostsmetaapi.setComments(new ArrayList<Comment>());

        String filename=fg.generatePostname(currentuser.getUniversity(),currentuser.getCollege(),currentuser.getBranch(),currentuser.getSection(),currentuser.getYear(),currentuser.getSem(),currentuser.getEmail(),System.currentTimeMillis())
        //storing filename in meta
        timelinePostsmetaapi.setFilename(filename)
        println("filename is "+filename)
        String postUrl = "http://"+servicehost+":8080/users/timeline/view/"+filename;
        String likeUrl = postUrl +"/like"
        String commentUrl = postUrl + "/comment"
        timelinePostsmetaapi.setPostUrl(postUrl);
        timelinePostsmetaapi.setLikeUrl(likeUrl);
        timelinePostsmetaapi.setCommentUrl(commentUrl);
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
    public List<TimelinePostsmetaapi> allposts(HttpServletRequest request,HttpServletResponse response){

        def objectlist = []

        User user = request.getSession().getAttribute("LOGGEDIN_USER");

        String filename=fg.generatePostnamewithouttime(user.getUniversity(),user.getCollege(),user.getBranch(),user.getSection(),user.getYear(),user.getSem())

        filename = filename + "-*";
        Query query = new Query().addCriteria(Criteria.where("filename").regex(filename))

        def list = gridFsTemplate.find(query)
        def filelist = []
        int i = 0
        for(GridFSDBFile fl : list)
            filelist[i++] = fl.getFilename();

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
    public String likePost(@PathVariable(value = "filename", required = true) String filename ,HttpServletRequest request){

        User loggeduser = request.getSession().getAttribute("LOGGEDIN_USER");
        TimelinePostsmetaapi timelinePostsmetaapi = timelineRepository.findByFilename(filename)
        def likes = timelinePostsmetaapi.getLikes()
        def likedusers = timelinePostsmetaapi.getLikedUsers()
        def flag = true
        likedusers.each {
            if(loggeduser.getEmail() == it.getEmail()){
                flag =false
            }
        }
        if(flag){
            likes = likes + 1;
            likedusers.add(loggeduser)
            timelinePostsmetaapi.setLikes(likes)
            timelinePostsmetaapi.setLikedUsers(likedusers)
            timelineRepository.save(timelinePostsmetaapi)
            return "like success current likes " + likes
        }
        return "already liked"

    }

    @ResponseBody
    @RequestMapping(value="/users/timeline/view/{filename:.+}/comment",method= RequestMethod.POST)
    public String CommentPost(@PathVariable(value = "filename", required = true)String filename, @RequestBody Comment comment, HttpServletRequest request){

        User loggeduser = request.getSession().getAttribute("LOGGEDIN_USER");
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

package com.engineering.Application.Controller

import api.Timeline.Comment
import api.Timeline.TimelinePosts
import api.Timeline.TimelinePostsmetaapi
import api.user.User
import com.engineering.core.Service.ServiceUtilities
import com.engineering.core.repositories.TimelineRepository
import com.engineering.core.repositories.UserRepository
import com.mongodb.gridfs.GridFSDBFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Created by GnyaniMac on 09/05/17.
 */

@EnableMongoRepositories(basePackageClasses = [UserRepository.class,TimelineRepository.class])
@RestController
class TimelineRestController {

    @Autowired
    TimelineRepository timelineRepository;

    @Value('${engineering.everything.host}')
    private String servicehost;

    @Autowired
    ServiceUtilities serviceUtils;

    @Autowired
    @Qualifier("timeline-files")
    GridFsTemplate gridFsTemplate



    @ResponseBody
    @PostMapping(value="/users/timeline/upload")
    public ResponseEntity<?> uploadposts (@RequestBody TimelinePosts post, OAuth2Authentication auth)
    {
        TimelinePostsmetaapi timelinePostsmetaapi = new TimelinePostsmetaapi();
        String email = serviceUtils.parseEmail(auth)
        User currentuser = serviceUtils.findUserByEmail(email);
        // setting meta data before uploading the file
        String propicurl = currentuser.normalpicUrl ?: currentuser.googlepicUrl
        timelinePostsmetaapi.setPropicUrl(propicurl)
        timelinePostsmetaapi.setDescription(post.getDescription())
        timelinePostsmetaapi.setOwner(currentuser.getFirstName())
        timelinePostsmetaapi.setPostdateTime(LocalDate.now())
        timelinePostsmetaapi.setLikes(0);
        timelinePostsmetaapi.setComments(new ArrayList<Comment>());

        post.isprofilepicchange() ? timelinePostsmetaapi.setIsprofilepicchange(post.isprofilepicchange()) : timelinePostsmetaapi.setIsprofilepicchange(false)
        String postDate = LocalDate.now()
        String posttime = System.currentTimeMillis()
        String filename=serviceUtils.generateFileName(currentuser.getUniqueclassid(),postDate,currentuser.getEmail(),posttime)
        timelinePostsmetaapi.setFilename(filename)
        String postUrl = post.file ? "http://${servicehost}:8080/users/timeline/view/${filename}" : null ;
        String likeUrl =  "http://${servicehost}:8080/users/timeline/view/${filename}/like"
        String commentUrl =  "http://${servicehost}:8080/users/timeline/view/${filename}/comment"
        timelinePostsmetaapi.setPostUrl(postUrl);
        timelinePostsmetaapi.setLikeUrl(likeUrl);
        timelinePostsmetaapi.setCommentUrl(commentUrl);
        timelinePostsmetaapi.setUploadeduser(serviceUtils.toUserDetails(currentuser))
        InputStream inputStream = new ByteArrayInputStream(post.getFile())
        gridFsTemplate.store(inputStream,filename)

        def object = timelineRepository.save(timelinePostsmetaapi);
        if(object)
             new ResponseEntity<>("Your post has been updated successfully ${filename}",HttpStatus.OK)
        else
             new ResponseEntity<>("failure in uploading the post ${object.toString()}",HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @GetMapping(value = "/users/timeline/posts",produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> allposts(@RequestParam(value="date") String date, OAuth2Authentication auth){

        def objectlist = []
        String email = serviceUtils.parseEmail(auth)
        User user = serviceUtils.findUserByEmail(email);

        String filenamedate=serviceUtils.generateFileName(user.getUniqueclassid(),date)
        Query query = new Query().with(new Sort(Sort.Direction.DESC, "uploadDate")).addCriteria(Criteria.where("filename").regex(filenamedate))

        def list = gridFsTemplate.find(query)
        for(GridFSDBFile fl : list) {
            TimelinePostsmetaapi temp = timelineRepository.findByFilename(fl.getFilename());
            objectlist.add(temp);
        }

        for(TimelinePostsmetaapi temp : objectlist ){
            def postemail = temp.getUploadeduser().getEmail()
            def userfromdb = serviceUtils.findUserByEmail(postemail)
            def propicUrl = userfromdb.getNormalpicUrl() ?: userfromdb.getGooglepicUrl()
            def firstNameinDB = userfromdb.getFirstName()
            if((!temp.getPropicUrl().equalsIgnoreCase(propicUrl)) || (!temp.getOwner().equalsIgnoreCase(firstNameinDB))){
                temp.setPropicUrl(propicUrl)
                temp.setOwner(firstNameinDB)
                temp.setUploadeduser(serviceUtils.toUserDetails(userfromdb))
                timelineRepository.save(temp)
            }
        }

        new ResponseEntity<>(objectlist,HttpStatus.OK)
    }

    @ResponseBody
    @GetMapping(value="/users/timeline/posts/{filename:.+}/delete")
    public ResponseEntity<?> deletepost(@PathVariable(value = "filename" , required = true) Object filename,OAuth2Authentication auth){
        String email = serviceUtils.parseEmail(auth)
        TimelinePostsmetaapi timelinePostsmetaapi = timelineRepository.findByFilename(filename.toString())
        if(timelinePostsmetaapi.getUploadeduser().getEmail() == email) {
            Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
            try {
                def deletedmeta = timelineRepository.deleteByFilename(filename.toString())
                if(deletedmeta)
                gridFsTemplate.delete(query)
                else throw Exception
            } catch (Exception e) {
                println(e)
                new ResponseEntity<>("sorry something went wrong ${e}",HttpStatus.INTERNAL_SERVER_ERROR)
            }
            new ResponseEntity<>("successfully deleted",HttpStatus.OK)
        }else{
            new ResponseEntity<>("Not authorized",HttpStatus.UNAUTHORIZED)
        }

    }

    @ResponseBody
    @GetMapping(value="/users/timeline/view/{filename:.+}",produces = "image/png" )
    public ResponseEntity<?> retrievePost(@PathVariable(value = "filename", required = true) Object filename){

        byte[] file
        Query query = new Query().addCriteria(Criteria.where("filename").is(filename))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput ?. writeTo(baos);
        file =baos ?. toByteArray();
        new ResponseEntity<>(file,HttpStatus.OK)
    }

    @ResponseBody
    @GetMapping(value="/users/timeline/view/{filename:.+}/like")
    public String likePost(@PathVariable(value = "filename", required = true) String filename ,OAuth2Authentication auth){

        String email = serviceUtils.parseEmail(auth)
        def loggeduser = serviceUtils.findUserByEmail(email)
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
            likedusers.add(serviceUtils.toUserDetails(loggeduser))
            timelinePostsmetaapi.setLikes(likes)
            timelinePostsmetaapi.setLikedUsers(likedusers)
            timelineRepository.save(timelinePostsmetaapi)
            return "like success current likes ${likes}"
        }
        return "already liked"

    }

    @ResponseBody
    @GetMapping(value="/users/timeline/view/{filename:.+}/like/likedusers")
    public ResponseEntity<?> likedUsers(@PathVariable(value = "filename", required = true) String filename ){
        TimelinePostsmetaapi timelinePostsmetaapi = timelineRepository.findByFilename(filename)
        def likedusers = timelinePostsmetaapi.getLikedUsers()
        def usernamesarray = [];
        likedusers.each{
            usernamesarray << it.getFirstName()
        }
        new  ResponseEntity<>(usernamesarray,HttpStatus.OK)
    }

    @ResponseBody
    @GetMapping(value="/users/timeline/view/{filename:.+}/like/unlike")
    public ResponseEntity<?> unlikePost(@PathVariable(value = "filename", required = true) String filename ,OAuth2Authentication auth){

        String email = serviceUtils.parseEmail(auth)
        def loggeduser = serviceUtils.findUserByEmail(email)
        TimelinePostsmetaapi timelinePostsmetaapi = timelineRepository.findByFilename(filename)
        def likes = timelinePostsmetaapi.getLikes()
        def likedusers = timelinePostsmetaapi.getLikedUsers()
        def flag = true
        likedusers.each {
            if(loggeduser.getEmail() == it.getEmail()){
                flag =true
            }
        }
        if(flag){
            if(likes)
            likes = likes - 1;
            likedusers.remove(serviceUtils.toUserDetails(loggeduser))
            timelinePostsmetaapi.setLikes(likes)
            timelinePostsmetaapi.setLikedUsers(likedusers)
            timelineRepository.save(timelinePostsmetaapi)
            return new ResponseEntity<>("Unlike success current likes ${likes}",HttpStatus.OK)
        }
        new ResponseEntity<>("not liked",HttpStatus.OK)

    }

    @ResponseBody
    @PostMapping(value="/users/timeline/view/{filename:.+}/comment")
    public ResponseEntity<?> CommentPost(@PathVariable(value = "filename", required = true)String filename, @RequestBody Comment comment,OAuth2Authentication auth){
        String email = serviceUtils.parseEmail(auth)
        def loggeduser = serviceUtils.findUserByEmail(email)
        TimelinePostsmetaapi timelinePostsmetaapi = timelineRepository.findByFilename(filename)
        def commentscurrent = timelinePostsmetaapi.getComments()
        comment.setUser(serviceUtils.toUserDetails(loggeduser))
        commentscurrent.add(comment)
        timelinePostsmetaapi.setComments(commentscurrent)
        timelineRepository.save(timelinePostsmetaapi)
        def finalcomment = loggeduser.getFirstName() +":\n"+comment.getComment()
        new ResponseEntity<>(finalcomment,HttpStatus.OK)

    }


}

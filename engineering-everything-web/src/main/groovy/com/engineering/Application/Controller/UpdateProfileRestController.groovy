package com.engineering.Application.Controller

import api.updateprofile.AddClass
import api.updateprofile.FileData
import api.user.User
import api.updateprofile.updateprofile
import com.engineering.core.Service.ServiceUtilities
import com.engineering.core.Service.NotificationService
import com.engineering.core.repositories.UserRepository
import com.mongodb.gridfs.GridFSDBFile
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

/**
 * Created by GnyaniMac on 24/07/17.
 */
@RestController
class UpdateProfileRestController {

    @Autowired
    UserRepository userRepository

    @Autowired
    ServiceUtilities serviceUtils

    @Autowired
    @Qualifier("profilepictures")
    GridFsTemplate gridFsTemplate

    @Autowired
    NotificationService notificationService

    @Value('${engineering.everything.host}')
    private String servicehost

    private static Logger log = LoggerFactory.getLogger(UpdateProfileRestController.class)


    @PostMapping(value="/user/update/profilepic")
    public ResponseEntity<?> updateProfilepic(@RequestBody FileData fileText, OAuth2Authentication auth){
        String email = serviceUtils.parseEmail(auth)
        User user = serviceUtils.findUserByEmail(email)
        Query query = new Query().addCriteria(Criteria.where("filename").is(email))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)
        log.info("<ProfileUpdate>["+email+"](update profile pic)")
        try{
            gridFsTemplate.delete(query)
        }catch (Exception e){
            throw new Exception("Old picture failed to get deleted")
        }
        try {
            InputStream inputStream = new ByteArrayInputStream(fileText.getFile())
            gridFsTemplate.store(inputStream, email)
        }catch (Exception e){
          def x= getGridFsTemplate().store(imageForOutput.getInputStream(),imageForOutput.getFilename())
            if(!x){
                throw new Exception("Data lost exception")
            }
        }
        def message="${user.firstName} changed profile picture"
        notificationService.storeNotifications(user,message,"timeline")

        def normalProppicUrl = "http://${servicehost}:8080/user/profilepic/view/${email}"
        user.setNormalpicUrl(normalProppicUrl)
        def x = userRepository.save(user)
        x ? new ResponseEntity<>("success", HttpStatus.OK) : new ResponseEntity<>("something went wrong",HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ResponseBody
    @GetMapping(value = "/user/profilepic/view/{email:.+}")
    public ResponseEntity<?> viewPropic(@PathVariable(value = "email" , required = true) String email, OAuth2Authentication auth2Authentication){
        byte[] file
        Query query = new Query().addCriteria(Criteria.where("filename").is(email))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput ?. writeTo(baos);
        log.info("<UpdateProfile>["+serviceUtils.parseEmail(auth2Authentication)+"](view profile pic)")
        file=baos ?. toByteArray()
        new ResponseEntity<>(file,HttpStatus.OK)
    }

    @PostMapping(value = "/user/update/profile")
    public ResponseEntity<?> updateProfile(@RequestBody updateprofile updateprofile,OAuth2Authentication auth2Authentication){
        def email = serviceUtils.parseEmail(auth2Authentication)
        def user = userRepository.findByEmail(email)
        user.setFirstName(updateprofile.getFirstName() ?: user.getFirstName())
        user.setLastName(updateprofile.getLastName() ?: user.getLastName())
        user.setDob(updateprofile.getDob() ?: user.getDob())
        def changeduser = userRepository.save(user)
        log.info("<UpdateProfile>[${email}](profile updated)")
        changeduser ? new ResponseEntity<>('success',HttpStatus.OK) : new ResponseEntity<>('failure',HttpStatus.INTERNAL_SERVER_ERROR)
    }


// Added by pratap to add new batches for teacher role

    @PostMapping(value="/teacher/addclass")
    def AddBatchToTeacher(@RequestBody AddClass addClass, OAuth2Authentication auth){

    try{
        String email = serviceUtils.parseEmail(auth)
        User user=userRepository.findByEmail(email)
        List<String> ListOfBatches = new ArrayList<String>()
        ListOfBatches = user.batches
        ListOfBatches.add(serviceUtils.generateFileName(addClass.year,addClass.section))
        user.batches = ListOfBatches
        def newUser = userRepository.save(user)
        log.info("<UpdateProfile/teacher/addclass>[${email}](profile updated)")
        newUser ? new ResponseEntity<>('success',HttpStatus.OK) : new ResponseEntity<>('failure',HttpStatus.INTERNAL_SERVER_ERROR)
    }
    catch(Exception e){
        log.error("Exception found while adding an extra batch in teacher role ${e.printStackTrace()}")
        return new ResponseEntity<>('failure',HttpStatus.INTERNAL_SERVER_ERROR)
    }
    }

}




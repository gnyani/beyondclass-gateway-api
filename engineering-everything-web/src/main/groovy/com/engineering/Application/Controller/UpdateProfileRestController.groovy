package com.engineering.Application.Controller

import api.FileData
import api.User
import api.updateprofile
import com.engineering.core.Service.EmailGenerationService
import com.engineering.core.Service.NotificationService
import com.engineering.core.repositories.UserRepository
import com.mongodb.gridfs.GridFSDBFile
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.bouncycastle.cert.ocsp.Req
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by GnyaniMac on 24/07/17.
 */
@RestController
class UpdateProfileRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    EmailGenerationService emailGenerationService;

    @Autowired
    @Qualifier("profilepictures")
    GridFsTemplate gridFsTemplate;

    @Autowired
    NotificationService notificationService;

    def jsonSlurper = new JsonSlurper()

    @RequestMapping(value="/user/update/profilepic", method = RequestMethod.POST)
    public Object updateProfilepic(@RequestBody FileData fileText, OAuth2Authentication auth){
        String email = emailGenerationService.parseEmail(auth)
        User user = userRepository.findByEmail(email)
        Query query = new Query().addCriteria(Criteria.where("filename").is(email))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)
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
        def message="${user.firstName} changed his profile picture"
        notificationService.storeNotifications(user,message,"timeline")
        def normalProppicUrl = "http://localhost:8080/user/profilepic/view/"+email
        user.setNormalpicUrl(normalProppicUrl)
        def x = userRepository.save(user)
        return(x ? "success" : "something went wrong")
    }

    @RequestMapping(value = "/user/profilepic/view/{email:.+}",produces = "image/jpg" , method = RequestMethod.GET)
    public byte[] viewPropic(@PathVariable(value = "email" , required = true) Object email){
        byte[] file = null;
        Query query = new Query().addCriteria(Criteria.where("filename").is(email))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput ?. writeTo(baos);
        file=baos ?. toByteArray()
        return file
    }

    @RequestMapping(value = "/user/update/profile",method = RequestMethod.POST)
    public String updateProfile(@RequestBody updateprofile updateprofile,OAuth2Authentication auth2Authentication){
        def email = emailGenerationService.parseEmail(auth2Authentication)
        def user = userRepository.findByEmail(email)
        user.setFirstName(updateprofile.getFirstName() ?: user.getFirstName())
        user.setLastName(updateprofile.getLastName() ?: user.getLastName())
        user.setDob(updateprofile.getDob() ?: user.getDob())
        user.setYear(updateprofile.getYear() ?: user.getYear())
        user.setSem(updateprofile.getSem() ?: user.getSem())
        def changeduser = userRepository.save(user)
        return changeduser ? 'success' : 'failure'
    }
}




package com.engineering.Application.Controller

import api.FileData
import api.User
import com.engineering.core.repositories.UserRepository
import com.mongodb.gridfs.GridFSDBFile
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.gridfs.GridFsTemplate
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.CrossOrigin
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
    @Qualifier("profilepictures")
    GridFsTemplate gridFsTemplate;

    def jsonSlurper = new JsonSlurper()

    @CrossOrigin(origins = ["http://localhost:8081","http://localhost:3000"])
    @RequestMapping(value="/user/update/profilepic", method = RequestMethod.POST)
    public Object updateProfilepic(@RequestBody FileData fileText, OAuth2Authentication auth){
        def m = JsonOutput.toJson(auth)
        def Json = jsonSlurper.parseText(m);
        // println(" json is " + Json)
        String email = Json.userAuthentication.details.email
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

        def normalProppicUrl = "http://localhost:8080/user/profilepic/view"
        user.setNormalpicUrl(normalProppicUrl)
        def x = userRepository.save(user)
        return(x ? "success" : "something went wrong")
    }
    @CrossOrigin(origins = ["http://localhost:8081","http://localhost:3000"])
    @RequestMapping(value = "/user/profilepic/view",produces = "image/jpg" , method = RequestMethod.GET)
    public byte[] viewPropic(OAuth2Authentication auth){
        def m = JsonOutput.toJson(auth)
        def Json = jsonSlurper.parseText(m);
        String email = Json.userAuthentication.details.email
        byte[] file = null;
        Query query = new Query().addCriteria(Criteria.where("filename").is(email))
        GridFSDBFile imageForOutput = gridFsTemplate.findOne(query)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageForOutput.writeTo(baos);
        file=baos.toByteArray()
        return file
    }
}




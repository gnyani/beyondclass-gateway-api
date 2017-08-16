package com.engineering.Application.Controller

import api.Anouncements
import com.engineering.core.Service.FilenameGenerator
import com.engineering.core.Service.EmailGenerationService
import com.engineering.core.repositories.AnouncementRepository
import com.engineering.core.repositories.UserRepository
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Created by GnyaniMac on 01/07/17.
 */
@CrossOrigin(origins = ["http://localhost:8081","http://localhost:3000"])
@RestController
class AnouncementsRestController {

    @Autowired
    AnouncementRepository anouncementRepository;

    @Autowired
    FilenameGenerator fg;

    @Autowired
    EmailGenerationService emailgenerationservice

    @Autowired
    UserRepository  userRepository;

    JsonSlurper jsonSlurper = new JsonSlurper()

    def PAGE_SIZE = 5

    @RequestMapping(value="/user/announcements/insert", method=RequestMethod.POST)
    public String insertAnouncement(@RequestBody Anouncements anouncements, OAuth2Authentication oauth){

        def m = JsonOutput.toJson(oauth)
        def Json = jsonSlurper.parseText(m);
        String firstname = Json.userAuthentication.details.given_name
        String secondname = Json.userAuthentication.details.family_name
        String username = firstname + secondname.substring(0,1)
        anouncements.setUsername(username);
        String email = Json.userAuthentication.details.email
        def user = userRepository.findByEmail(email)
        anouncements.setPosteduser(user)
        def classId = fg.generateClassId(user.getUniversity(),user.getCollege(),user.getBranch(),user.getSection(),user.getYear(),user.getSem())
        anouncements.setClassId(classId)
        anouncements.setAid(classId+email+System.currentTimeMillis())
        def object = anouncementRepository.save(anouncements)
        return (object ? "anouncement saved successfully as " + anouncements : "something went wrong")
    }

    @RequestMapping(value = "/user/announcement/delete/{aid:.+}",method = RequestMethod.GET)
    public String  deleteAnnouncement(@PathVariable(value = "aid" , required = true) String aid){
        def deletedannouncement = anouncementRepository.deleteByAid(aid)
        println("delete announcement" + deletedannouncement)
        return(deletedannouncement ? "Success": "Something went wrong")
    }

    @RequestMapping(value="/user/announcements/list", method= RequestMethod.GET,produces = "application/json")
    public Page<Anouncements> getAnouncements(@RequestParam int pageNumber, OAuth2Authentication oauth){
        String email = emailgenerationservice.parseEmail(oauth)
        def user = userRepository.findByEmail(email)
        def classId = fg.generateClassId(user.getUniversity(),user.getCollege(),user.getBranch(),user.getSection(),user.getYear(),user.getSem())
        //Anouncements[] anouncements = anouncementRepository.findByClassId(classId);
        Pageable request =
                new PageRequest(pageNumber - 1, PAGE_SIZE,new Sort(Sort.Direction.DESC, "createdAt"));

        return  anouncementRepository.findByClassId(classId,request)
    }

}

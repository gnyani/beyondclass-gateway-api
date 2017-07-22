package com.engineering.Application.Controller

import api.Anouncements
import com.engineering.core.Service.FilenameGenerator
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
    UserRepository  userRepository;

    @Value('${engineering.everything.host}')
    private String servicehost;

    JsonSlurper jsonSlurper = new JsonSlurper()

    def PAGE_SIZE = 5

    @RequestMapping(value="/user/anouncements/insert", method=RequestMethod.POST)
    public String insertAnouncement(@RequestBody Anouncements anouncements, OAuth2Authentication oauth){

        def m = JsonOutput.toJson(oauth)
        def Json = jsonSlurper.parseText(m);
        String firstname = Json.userAuthentication.details.given_name
        anouncements.setUsername(firstname);
        String email = Json.userAuthentication.details.email
        def user = userRepository.findByEmail(email)
        def classId = fg.generateClassId(user.getUniversity(),user.getCollege(),user.getBranch(),user.getSection(),user.getYear(),user.getSem())
        anouncements.setClassId(classId)
        def object = anouncementRepository.save(anouncements)
        return  "anouncement saved successfully as " + anouncements.toString()
    }

    @RequestMapping(value="/user/anouncements/list", method= RequestMethod.GET,produces = "application/json")
    public Page<Anouncements> getAnouncements(@RequestParam int pageNumber, OAuth2Authentication oauth){
        def m = JsonOutput.toJson(oauth)
        def Json = jsonSlurper.parseText(m);
        String email = Json.userAuthentication.details.email
        def user = userRepository.findByEmail(email)
        def classId = fg.generateClassId(user.getUniversity(),user.getCollege(),user.getBranch(),user.getSection(),user.getYear(),user.getSem())
        //Anouncements[] anouncements = anouncementRepository.findByClassId(classId);
        Pageable request =
                new PageRequest(pageNumber - 1, PAGE_SIZE,new Sort(Sort.Direction.DESC, "createdAt"));

        return  anouncementRepository.findByClassId(classId,request)
    }

}

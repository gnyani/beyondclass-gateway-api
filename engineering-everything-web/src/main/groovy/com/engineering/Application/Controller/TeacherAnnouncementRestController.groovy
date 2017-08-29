package com.engineering.Application.Controller

import api.Anouncements
import api.TeacherAnnouncement
import com.engineering.core.Service.EmailGenerationService
import com.engineering.core.Service.FilenameGenerator
import com.engineering.core.Service.NotificationService
import com.engineering.core.repositories.TeacherAnnouncementRepository
import com.engineering.core.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
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
 * Created by GnyaniMac on 14/08/17.
 */

@RestController
class TeacherAnnouncementRestController {

    @Autowired
    TeacherAnnouncementRepository teacherAnnouncementRepository;

    @Autowired
    FilenameGenerator fg;

    @Autowired
    EmailGenerationService emailGenerationService;

    @Autowired
    UserRepository userRepository

    @Autowired
    NotificationService notificationService;


    def PAGE_SIZE = 5

    @RequestMapping(value="/teacher/announcements/insert", method=RequestMethod.POST)
    public String insertAnouncement(@RequestBody TeacherAnnouncement announcement, OAuth2Authentication oauth){
        def email = emailGenerationService.parseEmail(oauth)
        def user = userRepository.findByEmail(email)
        def announcementid =  fg.generateTeacherAnnouncementId(user.getUniversity(),user.getCollege(),user.getBranch(),announcement.getTeacherclass(),email)
        announcement.setAnnouncementid(announcementid)
        announcement.setUser(user)
        try {
            teacherAnnouncementRepository.save(announcement)
            def message ="You have a new announcement from your teacher ${user.firstName.toUpperCase()}"
            notificationService.storeNotifications(user,message,"teacherstudentspace",announcement.teacherclass)
        }
        catch(Exception e){
        return "sorry something went wrong please try again"
        }
        return "success"
    }

    @RequestMapping(value = "/teacher/announcement/delete/{announcementid:.+}",method = RequestMethod.GET)
    public String  deleteAnnouncement(@PathVariable(value = "announcementid" , required = true) String announcementid){
        println("annocementid is"+ announcementid)
        def deletedannouncement= teacherAnnouncementRepository.deleteByannouncementid(announcementid)
        return(deletedannouncement ? "Success": "Something went wrong")
    }

    @RequestMapping(value="/teacher/announcements/list/{teacherclass:.+}", method= RequestMethod.GET,produces = "application/json")
    public Page<Anouncements> getAnouncements(@PathVariable(value="teacherclass",required = true) String teacherclass,@RequestParam int pageNumber, OAuth2Authentication oauth){

        String email = emailGenerationService.parseEmail(oauth)
        def user = userRepository.findByEmail(email)
        def announcementid = fg.genericGenerator(user.getUniversity(),user.getCollege(),user.getBranch(),teacherclass,email)
        Pageable request =
                new PageRequest(pageNumber - 1, PAGE_SIZE,new Sort(Sort.Direction.DESC, "createdAt"));

        return  teacherAnnouncementRepository.findByAnnouncementidLike(announcementid,request)
    }

    @RequestMapping(value="/teacher/student/announcements/list/{teacherclass:.+}", method= RequestMethod.GET,produces = "application/json")
    public Page<Anouncements> getAnouncementsforstudents(@PathVariable(value="teacherclass",required = true) String teacherclass,@RequestParam int pageNumber, OAuth2Authentication oauth){

        String email = emailGenerationService.parseEmail(oauth)
        def user = userRepository.findByEmail(email)
        def announcementid = fg.genericGenerator(user.getUniversity(),user.getCollege(),user.getBranch(),teacherclass)
        Pageable request =
                new PageRequest(pageNumber - 1, PAGE_SIZE,new Sort(Sort.Direction.DESC, "createdAt"));

        return  teacherAnnouncementRepository.findByAnnouncementidLike(announcementid,request)
    }


}

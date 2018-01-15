package com.engineering.Application.Controller

import api.teacherstudentspace.TeacherAnnouncement
import api.user.User
import com.engineering.core.Service.EmailUtils
import com.engineering.core.Service.MailService
import com.engineering.core.Service.ServiceUtilities
import com.engineering.core.Service.NotificationService
import com.engineering.core.constants.EmailTypes
import com.engineering.core.repositories.TeacherAnnouncementRepository
import com.engineering.core.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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
    ServiceUtilities serviceUtils;

    @Autowired
    UserRepository userRepository

    @Autowired
    NotificationService notificationService;

    @Autowired
    EmailUtils emailUtils

    @Autowired
    MailService mailService

    def PAGE_SIZE = 5

    @PostMapping(value="/teacher/announcements/insert")
    public ResponseEntity<?> insertAnouncement(@RequestBody TeacherAnnouncement announcement, OAuth2Authentication oauth){
        def email = serviceUtils.parseEmail(oauth)
        def user = serviceUtils.findUserByEmail(email)

        def splits = announcement.batch.split('-')
        String startyear = splits[0]
        String section = splits[1]
        String endyear = Integer.parseInt(startyear)+ 4

        String time = System.currentTimeMillis()
        def announcementid =  serviceUtils.generateFileName(user.getUniversity(),user.getCollege(),user.getBranch(),announcement.getBatch(),email,time)
        announcement.setAnnouncementid(announcementid)
        announcement.setPosteduser(serviceUtils.toUserDetails(user))
        try {
            teacherAnnouncementRepository.save(announcement)
            def message ="You have a new announcement from your teacher ${user.firstName.toUpperCase()}"
            notificationService.storeNotifications(user,message,"teacherstudentspace",announcement.batch)

            String classId = serviceUtils.generateFileName(user.university,user.college,user.branch,section,startyear,endyear)
            findUsersAndSendEmail(classId,EmailTypes.ANNOUNCEMENT,user.email)
        }
        catch(Exception e){
         new ResponseEntity<>("sorry something went wrong please try again",HttpStatus.INTERNAL_SERVER_ERROR)
        }
        new ResponseEntity<>("success",HttpStatus.OK)
    }

    @GetMapping(value = "/teacher/announcement/delete/{announcementid:.+}")
    public ResponseEntity<?>  deleteAnnouncement(@PathVariable(value = "announcementid" , required = true) String announcementid){
        def deletedannouncement= teacherAnnouncementRepository.deleteByAnnouncementid(announcementid)
        deletedannouncement ? new ResponseEntity<>("Success",HttpStatus.OK): new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @GetMapping(value="/teacher/announcements/list/{batch:.+}",produces = "application/json")
    public ResponseEntity<?> getAnouncements(@PathVariable(value="batch",required = true) String batch, @RequestParam int pageNumber, OAuth2Authentication oauth){

        String email = serviceUtils.parseEmail(oauth)
        def user = serviceUtils.findUserByEmail(email)
        def announcementid = serviceUtils.generateFileName(user.getUniversity(),user.getCollege(),user.getBranch(),batch,email)
        Pageable request =
                new PageRequest(pageNumber - 1, PAGE_SIZE,new Sort(Sort.Direction.DESC, "createdAt"));

        new  ResponseEntity<>(teacherAnnouncementRepository.findByAnnouncementidStartingWith(announcementid,request),HttpStatus.OK)
    }

    @GetMapping(value="/teacher/student/announcements/list/{batch:.+}",produces = "application/json")
    public ResponseEntity<?> getAnouncementsforstudents(@PathVariable(value="batch",required = true) String batch, @RequestParam int pageNumber, OAuth2Authentication oauth){

        String email = serviceUtils.parseEmail(oauth)
        def user = serviceUtils.findUserByEmail(email)
        def announcementid = serviceUtils.generateFileName(user.getUniversity(),user.getCollege(),user.getBranch(),batch)
        Pageable request =
                new PageRequest(pageNumber - 1, PAGE_SIZE,new Sort(Sort.Direction.DESC, "createdAt"));

        new ResponseEntity<>(teacherAnnouncementRepository.findByAnnouncementidStartingWith(announcementid,request),HttpStatus.OK)
    }

    void findUsersAndSendEmail(String classId, EmailTypes emailTypes, String sender){
        List<User> users = userRepository.findByUniqueclassid(classId)
        def toEmails = []
        users.each {
            toEmails.add(it.email)
        }
        String[] emails = new String[toEmails.size()]
        emails = toEmails.toArray(emails)
        String htmlMessage = emailUtils.createEmailMessage(emailTypes,sender)
        String subject = emailUtils.createSubject(emailTypes)

        mailService.sendHtmlMail(emails,subject,htmlMessage)
    }


}

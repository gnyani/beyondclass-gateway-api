package com.engineering.Application.Controller

import api.user.User
import api.announcements.Announcements

import com.engineering.core.Service.ServiceUtilities
import com.engineering.core.Service.NotificationService
import com.engineering.core.repositories.AnnouncementRepository
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
 * Created by GnyaniMac on 01/07/17.
 */
@RestController
class AnnouncementsRestController {

    @Autowired
    AnnouncementRepository announcementRepository;

    @Autowired
    ServiceUtilities serviceUtils

    @Autowired
    NotificationService notificationService;


    def PAGE_SIZE = 4

    @PostMapping(value="/user/announcements/insert")
    public ResponseEntity<?> insertAnouncement(@RequestBody Announcements anouncements, OAuth2Authentication oauth){

        String email = serviceUtils.parseEmail(oauth)
        User user = serviceUtils.findUserByEmail(email)
        String firstname = user?.getFirstName()
        String secondname = user?.getLastName()
        String username = firstname + secondname.substring(0,1)
        String time = System.currentTimeMillis()
        String announcementid = serviceUtils.generateFileName(user.uniqueclassid,email,time)

        anouncements.setUsername(username);
        anouncements.setPosteduser(serviceUtils.toUserDetails(user))
        anouncements.setAnnouncementid(announcementid)
        def object = announcementRepository.save(anouncements)

        String message = user.firstName+" posted an announcement"
        notificationService.storeNotifications(user,message,"announcements")

        object ? new ResponseEntity<>("anouncement saved successfully as ${anouncements}", HttpStatus.CREATED)
                : new ResponseEntity<>("something went wrong",HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @GetMapping(value = "/user/announcement/delete/{announcementid:.+}")
    public ResponseEntity<?>  deleteAnnouncement(@PathVariable(value = "announcementid" , required = true) String announcementid){
        def deletedannouncement = announcementRepository.deleteByAnnouncementid(announcementid)
        println("delete announcement ${deletedannouncement}")
        deletedannouncement ? new ResponseEntity<>("Success",HttpStatus.OK):new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR)
    }


    @GetMapping(value="/user/announcements/list",produces = "application/json")
    public ResponseEntity<?> getAnouncements(@RequestParam int pageNumber, OAuth2Authentication oauth){
        String email = serviceUtils.parseEmail(oauth)
        def user = serviceUtils.findUserByEmail(email)
        Pageable request =
                new PageRequest(pageNumber - 1, PAGE_SIZE,new Sort(Sort.Direction.DESC, "createdAt"));

         new ResponseEntity<>(announcementRepository.findByAnnouncementidStartingWith(user.getUniqueclassid(),request),HttpStatus.OK)
    }

}

package com.engineering.Application.Controller

import api.notifications.Notifications
import api.notifications.NotificationsReadStatus
import com.engineering.core.Service.ServiceUtilities
import com.engineering.core.repositories.NotificationsRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by GnyaniMac on 17/08/17.
 */

@RestController
class NotificationsRestController {

    @Autowired
    ServiceUtilities serviceUtils

    @Autowired
    NotificationsRepository notificationsRepository

    private static Logger log = LoggerFactory.getLogger(NotificationsRestController.class)


    @GetMapping(value="/user/notifications" )
    public List<Notifications> getNotifications(OAuth2Authentication oAuth2Authentication){
        def email = serviceUtils.parseEmail(oAuth2Authentication)
        log.info("<Notifications>["+email+"](retrieved notifications)")
        def user = serviceUtils.findUserByEmail(email)
        if(user.userrole == "student") {
            def userReadStatus = new NotificationsReadStatus()
            userReadStatus.setEmail(email)
            userReadStatus.setRead(false)
            def unreadnotifications = notificationsRepository.findByNotificationIdStartingWithAndUsersContainingOrderByNotificationIdDesc(user.uniqueclassid, userReadStatus)
            userReadStatus.setRead(true)
            def readnotifications = notificationsRepository.findByNotificationIdStartingWithAndUsersContainingOrderByNotificationIdDesc(user.uniqueclassid, userReadStatus)
            return unreadnotifications + readnotifications
        }
    }
    @RequestMapping(value="/user/notifications/read" , method = RequestMethod.POST)
    public String markAsread(@RequestBody String id,OAuth2Authentication auth2Authentication){
        def email = serviceUtils.parseEmail(auth2Authentication)
        def notification = notificationsRepository.findByNotificationId(id)
        log.info("<Notifications>["+email+"](marked as Read for notification " + notification + ")")
        def userReadStatus = new NotificationsReadStatus()
        userReadStatus.setEmail(email)
        userReadStatus.setRead(false)
        def index= notification.users.indexOf(userReadStatus)
        notification.users[index].setRead(true)
        notificationsRepository.save(notification)
    }
    @RequestMapping(value="/user/notifications/unread" , method = RequestMethod.GET)
    public int getNotificationscount(OAuth2Authentication oAuth2Authentication){
        def unreadNotifications = getUnreadNotifications(oAuth2Authentication)
        unreadNotifications.size()
    }


    @RequestMapping(value="/user/notifications/markallasread" , method = RequestMethod.GET)
    public ResponseEntity<?> markAllNotificationsRead(OAuth2Authentication oAuth2Authentication){
        log.info("<Notifications>["+serviceUtils.parseEmail(oAuth2Authentication)+"](markAllNotificationsRead)")
        def unreadNotifications = getUnreadNotifications(oAuth2Authentication)
        unreadNotifications.each{
            markAsread(it.notificationId,oAuth2Authentication)
        }
        new ResponseEntity<>("Success",HttpStatus.OK)
    }

    @RequestMapping(value="/user/notifications/delete" , method = RequestMethod.POST)
    public boolean deleteUserfromNotification(@RequestBody String id,OAuth2Authentication oAuth2Authentication){
        log.info("<Notifications>["+serviceUtils.parseEmail(oAuth2Authentication)+"](deleted user from notification)")
        def email = serviceUtils.parseEmail(oAuth2Authentication)
        def x = new NotificationsReadStatus()
        x.setEmail(email)
        x.setRead(false)
        def y = new NotificationsReadStatus()
        y.setEmail(email)
        y.setRead(true)
        def notification = notificationsRepository.findByNotificationId(id)
        notification.users.remove(x)
        notification.users.remove(y)
        notificationsRepository.save(notification)
    }
    private def getUnreadNotifications(OAuth2Authentication oAuth2Authentication) {
        def email = serviceUtils.parseEmail(oAuth2Authentication)
        def user = serviceUtils.findUserByEmail(email)
        def userReadStatus = new NotificationsReadStatus()
        userReadStatus.setEmail(email)
        userReadStatus.setRead(false)
        if (user.userrole == "student") {
            def unreadnotifications = notificationsRepository.findByNotificationIdStartingWithAndUsersContainingOrderByNotificationIdDesc(user.uniqueclassid, userReadStatus)
            return unreadnotifications
        }
    }
}

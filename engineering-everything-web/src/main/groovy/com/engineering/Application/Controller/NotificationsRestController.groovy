package com.engineering.Application.Controller

import api.Notifications
import api.NotificationsReadStatus
import com.engineering.core.Service.EmailGenerationService
import com.engineering.core.repositories.NotificationsRepository
import com.engineering.core.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by GnyaniMac on 17/08/17.
 */
@CrossOrigin(origins = ["http://localhost:8081","http://localhost:3000"])
@RestController
class NotificationsRestController {

    @Autowired
    EmailGenerationService emailGenerationService

    @Autowired
    NotificationsRepository notificationsRepository

    @Autowired
    UserRepository userRepository

    @RequestMapping(value="/user/notifications" , method = RequestMethod.GET)
    public List<Notifications> getNotifications(OAuth2Authentication oAuth2Authentication){
        def email = emailGenerationService.parseEmail(oAuth2Authentication)
        def user = userRepository.findByEmail(email)
        def x = new NotificationsReadStatus()
        x.setEmail(email)
        x.setRead(false)
        def unreadnotifications = notificationsRepository.findByNotificationIdLikeAndUsersContainingOrderByNotificationIdDesc(user.notificationId,x)
        x.setRead(true)
        def readnotifications = notificationsRepository.findByNotificationIdLikeAndUsersContainingOrderByNotificationIdDesc(user.notificationId,x)
        return  unreadnotifications+readnotifications
    }
    @RequestMapping(value="/user/notifications/read" , method = RequestMethod.POST)
    public String markAsread(@RequestBody String id,OAuth2Authentication auth2Authentication){
        def email = emailGenerationService.parseEmail(auth2Authentication)
        println("notification id is" + id);
        def notification = notificationsRepository.findByNotificationId(id)
        def x = new NotificationsReadStatus()
        x.setEmail(email)
        x.setRead(false)
        def index= notification.users.indexOf(x)
        notification.users[index].setRead(true)
        notificationsRepository.save(notification)
    }
    @RequestMapping(value="/user/notifications/unread" , method = RequestMethod.GET)
    public int getNotificationscount(OAuth2Authentication oAuth2Authentication){
        def email = emailGenerationService.parseEmail(oAuth2Authentication)
        def user = userRepository.findByEmail(email)
        def x = new NotificationsReadStatus()
        x.setEmail(email)
        x.setRead(false)
        def unreadnotifications = notificationsRepository.findByNotificationIdLikeAndUsersContainingOrderByNotificationIdDesc(user.notificationId,x)
        return  unreadnotifications.size()
    }

    @RequestMapping(value="/user/notifications/delete" , method = RequestMethod.POST)
    public boolean deleteUserfromNotification(@RequestBody String id,OAuth2Authentication oAuth2Authentication){
        def email = emailGenerationService.parseEmail(oAuth2Authentication)
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
}

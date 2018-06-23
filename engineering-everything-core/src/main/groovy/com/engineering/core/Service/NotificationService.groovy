package com.engineering.core.Service

import api.notifications.Notifications
import api.notifications.NotificationsReadStatus
import api.user.User
import com.engineering.core.repositories.NotificationsRepository
import com.engineering.core.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by GnyaniMac on 17/08/17.
 */
@Component
class NotificationService {

    @Autowired
    ServiceUtilities serviceUtilities

    @Autowired
    UserRepository userRepository

    @Autowired
    NotificationsRepository notificationsRepository

    public boolean  storeNotifications(User user,String content,String type){
        Notifications notifications = new Notifications();
        notifications.setNotificationId(user.uniqueclassid +'-'+user.email+'-'+System.currentTimeMillis())
        def users = userRepository.findByUniqueclassid(user.uniqueclassid)
        users.removeAll(user)
        def usersNotificationsReadStatus =  []
        users.each {
            def userReadStatus = new NotificationsReadStatus()
            userReadStatus.setEmail(it.email)
            userReadStatus.setRead(false)
            usersNotificationsReadStatus.add(userReadStatus)
        }
        notifications.setUsers(usersNotificationsReadStatus)
        notifications.setContent(content)
        notifications.setPicurl(user.normalpicUrl ?: user.googlepicUrl)
        notifications.setNotificationType(type)
        def notification = notificationsRepository.save(notifications)
        notification ? true:false
    }

    public boolean storeNotifications(User user,String content,String type,String batch){
        Notifications notifications = new Notifications();
        String endyear = batch.substring(0,4).toInteger() + 4
        def notificationId = serviceUtilities.generateFileName(user.university,user.college,user.branch,batch.substring(5),batch.substring(0,4),endyear)
        def users = userRepository.findByUniqueclassid(notificationId)
        String time = System.currentTimeMillis()
        notifications.setNotificationId(serviceUtilities.generateFileName(notificationId,user.email,time))
        def usersNotificationsReadStatus =  []
        users.each {
            def userReadStatus = new NotificationsReadStatus()
            userReadStatus.setEmail(it.email)
            userReadStatus.setRead(false)
            usersNotificationsReadStatus.add(userReadStatus)
        }
        notifications.setUsers(usersNotificationsReadStatus)
        notifications.setContent(content)
        notifications.setPicurl(user.normalpicUrl ?: user.googlepicUrl)
        notifications.setNotificationType(type)
        def notification = notificationsRepository.save(notifications)
        notification ? true:false
    }
}

package com.engineering.core.Service

import api.Notifications
import api.NotificationsReadStatus
import api.User
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
    UserRepository userRepository

    @Autowired
    NotificationsRepository notificationsRepository

    public boolean  storeNotifications(User user,String content,String type){
        Notifications notifications = new Notifications();
        notifications.setNotificationId(user.notificationId+'-'+user.email+'-'+System.currentTimeMillis())
        def users = userRepository.findByNotificationId(user.notificationId)
        users.removeAll(user)
        def NotificationsReadStatus =  []
        users.each {
            def x = new NotificationsReadStatus()
            x.setEmail(it.email)
            x.setRead(false)
            NotificationsReadStatus.add(x)
        }
        notifications.setUsers(NotificationsReadStatus)
        notifications.setContent(content)
        notifications.setPicurl(user.normalpicUrl ?: user.googlepicUrl)
        notifications.setNotificationType(type)
        def notification = notificationsRepository.save(notifications)
        return notification ? true:false
    }

    public boolean storeNotifications(User user,String content,String type,String teacherclass){
        Notifications notifications = new Notifications();
        def notificationIdsem1 = user.notificationId.replace('- ','')+'-'+teacherclass.charAt(0)+'-'+'1'+'-'+teacherclass.charAt(2)
        def notificationIdsem2 = user.notificationId.replace('- ','')+'-'+teacherclass.charAt(0)+'-'+'2'+'-'+teacherclass.charAt(2)
        def userssem1 = userRepository.findByNotificationId(notificationIdsem1)
        def userssem2 = userRepository.findByNotificationId(notificationIdsem2)
        def users = userssem1 + userssem2
        notifications.setNotificationId(notificationIdsem1+'-'+user.email+'-'+System.currentTimeMillis())
        def NotificationsReadStatus =  []
        users.each {
            def x = new NotificationsReadStatus()
            x.setEmail(it.email)
            x.setRead(false)
            NotificationsReadStatus.add(x)
        }
        notifications.setUsers(NotificationsReadStatus)
        notifications.setContent(content)
        notifications.setPicurl(user.normalpicUrl ?: user.googlepicUrl)
        notifications.setNotificationType(type)
        notificationsRepository.save(notifications)
        notifications.setNotificationId(notificationIdsem2+'-'+user.email+'-'+System.currentTimeMillis())
        def notification = notificationsRepository.save(notifications)
        return notification ? true:false
    }
}

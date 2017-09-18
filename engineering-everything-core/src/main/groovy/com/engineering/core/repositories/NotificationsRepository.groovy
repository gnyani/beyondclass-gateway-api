package com.engineering.core.repositories

import api.notifications.Notifications
import api.notifications.NotificationsReadStatus
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * Created by GnyaniMac on 17/08/17.
 */
public interface NotificationsRepository extends MongoRepository<Notifications,String>{

    List<Notifications> findByNotificationIdStartingWithAndUsersContainingOrderByNotificationIdDesc(String notificationid, NotificationsReadStatus user)

    Notifications findByNotificationId(String notificationid)

}
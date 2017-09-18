package com.engineering.core.repositories

import api.announcements.Announcements
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * Created by GnyaniMac on 01/07/17.
 */
public interface AnnouncementRepository extends MongoRepository<Announcements,String>{



    Page<Announcements> findByAnnouncementidStartingWith(String uniqueclassid, Pageable pageable);

    List<Announcements> deleteByAnnouncementid(String announcementid)

}

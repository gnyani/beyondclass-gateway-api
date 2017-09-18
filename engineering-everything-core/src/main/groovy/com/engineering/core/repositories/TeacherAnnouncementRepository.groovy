package com.engineering.core.repositories

import api.teacherstudentspace.TeacherAnnouncement
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * Created by GnyaniMac on 14/08/17.
 */
public interface TeacherAnnouncementRepository extends MongoRepository<TeacherAnnouncement,String> {

    Page<TeacherAnnouncement> findByAnnouncementidStartingWith(String announcementid, Pageable pageable);

    List<TeacherAnnouncement> deleteByAnnouncementid(String announcementid)

}
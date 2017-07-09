package com.engineering.core.repositories

import api.Anouncements
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * Created by GnyaniMac on 01/07/17.
 */
public interface  AnouncementRepository extends MongoRepository<Anouncements,String>{

//    public Anouncements[] findByClassId(String classId);

    Page<Anouncements> findByClassId(String classId, Pageable pageable);

}

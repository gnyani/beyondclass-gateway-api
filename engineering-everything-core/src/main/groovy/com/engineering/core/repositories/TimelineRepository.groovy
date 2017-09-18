package com.engineering.core.repositories

import api.Timeline.TimelinePostsmetaapi
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * Created by GnyaniMac on 27/04/17.
 */

public interface TimelineRepository extends MongoRepository<TimelinePostsmetaapi,String> {

    public TimelinePostsmetaapi findByFilename(String filename);

    public List<TimelinePostsmetaapi> deleteByFilename(String filename);


}

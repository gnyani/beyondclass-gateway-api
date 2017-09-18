package com.engineering.core.repositories

import api.coachingcentres.Coachingcentre
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * Created by GnyaniMac on 13/07/17.
 */
public interface CoachingCentresRepository extends  MongoRepository<Coachingcentre , String> {

    public List<Coachingcentre> findBycoachingcentreIdStartingWith(String caochingcentreId);

    public Coachingcentre findBycoachingcentreId(String caochingcentreId)
}

package com.engineering.core.repositories

import api.coachingcentres.Rating
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * Created by GnyaniMac on 16/07/17.
 */
public interface RatingRepository extends MongoRepository<Rating,String> {

    public List<Rating> findBycoachingcentreId(String coachingcentreId)

    public List<Rating> findByCoachingcentreIdAndEmail(String coachingcentreId,String email)
}

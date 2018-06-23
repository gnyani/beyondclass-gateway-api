package com.engineering.core.repositories

import api.handouts.Handouts
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * Created by Gnyani on 20/06/18.
 */
interface HandoutsRepository extends MongoRepository<Handouts, String> {

    List<Handouts> findByfilenameRegex(String filename)
}
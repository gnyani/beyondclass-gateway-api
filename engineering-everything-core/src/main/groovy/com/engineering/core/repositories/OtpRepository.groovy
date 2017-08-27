package com.engineering.core.repositories

import api.Otp
import org.springframework.data.mongodb.repository.MongoRepository

/**
 * Created by GnyaniMac on 27/08/17.
 */
public interface OtpRepository extends MongoRepository<Otp,String>{

    public Otp findByEmail(String email)
}

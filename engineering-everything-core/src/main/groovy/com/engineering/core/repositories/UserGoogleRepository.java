package com.engineering.core.repositories;

import api.UserGoogle;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserGoogleRepository extends MongoRepository<UserGoogle, String> {
    UserGoogle findByMailId(String mailId);

}
package com.engineering.core.repositories;


import api.User
import api.UserLogin
import org.springframework.data.mongodb.repository.MongoRepository;



/**
 * Created by GnyaniMac on 27/04/17.
 */

public interface UserRepository extends MongoRepository<User,String> {
    public User findByEmail(String email);

    public List<User> findByNotificationId(String notificationId);

    public Long deleteUserByEmail(String email);
}

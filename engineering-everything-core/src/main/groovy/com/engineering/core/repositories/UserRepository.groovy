package com.engineering.core.repositories;


import api.user.User
import org.springframework.data.mongodb.repository.MongoRepository;



/**
 * Created by GnyaniMac on 27/04/17.
 */

public interface UserRepository extends MongoRepository<User,String> {
    public User findByEmail(String email);

    public List<User> findByUniqueclassid(String uniqueclassid);
    public long countByUniqueclassid(String uniqueclassid)
    public Long deleteUserByEmail(String email);

}

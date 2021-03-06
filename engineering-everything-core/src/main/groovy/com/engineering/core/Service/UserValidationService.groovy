package com.engineering.core.Service

import api.user.User
import com.engineering.core.repositories.UserRepository
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

/**
 * Created by GnyaniMac on 04/06/17.
 */
@Service
class UserValidationService {

    @Autowired
    private UserRepository userRepository

    private static Logger log = LoggerFactory.getLogger(UserValidationService.class)

    JsonSlurper jsonSlurper = new JsonSlurper();

    public User validateuserexistence(Authentication auth) {

        def m = JsonOutput.toJson(auth)

        def Json = jsonSlurper.parseText(m);
        // println(" json is " + Json)
        String email = Json.userAuthentication.details.email
        String googlepic = Json.userAuthentication.details.picture
        String firstName = Json.userAuthentication.details.given_name
        User present = userRepository.findByEmail(email);
        if (present)
        {
            present.setGooglepicUrl(googlepic ?: '')
            present.setFirstName(firstName ?: '')
            userRepository.save(present)
            return  present
        }
        else {
            log.info("[Registration] new user "+email)
            return null
        }
    }
}

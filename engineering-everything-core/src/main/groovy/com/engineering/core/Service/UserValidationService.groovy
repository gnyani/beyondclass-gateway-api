package com.engineering.core.Service

import api.User
import com.engineering.core.repositories.UserRepository
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
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

    JsonSlurper jsonSlurper = new JsonSlurper();

    public String validateuserexistence(Authentication auth) {

        def m = JsonOutput.toJson(auth)

        def Json = jsonSlurper.parseText(m);
        // println(" json is " + Json)
        String email = Json.userAuthentication.details.email
        println("email is ${email}")
        User present = userRepository.findByEmail(email);
        println("User is ${present}")
        if (present != null)
        {
            return "true"
        }
        else {
            return "false"
        }
    }
}

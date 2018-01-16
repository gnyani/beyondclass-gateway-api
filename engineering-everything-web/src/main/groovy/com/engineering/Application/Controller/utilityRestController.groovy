package com.engineering.Application.Controller

import api.user.User
import com.engineering.core.Service.ServiceUtilities
import com.engineering.core.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by GnyaniMac on 16/01/18.
 */

@RestController
class utilityRestController {

    @Autowired
    ServiceUtilities serviceUtilities

    @Autowired
    UserRepository userRepository

    @GetMapping(value = '/deamons/admin/insert')
    public ResponseEntity<?> insertUsers(){
        println("This is called")
        def file = new File("/Users/GnyaniMac/Desktop/mywork/OU-VASV-CSE-A-2016-2020.csv")
        file.eachLine { String line ->
            String[] splits = line.split(',')
            Date date = null
            if(splits[4])
               date = new Date(splits[4])

            def user = new User()
            user.with{
                email = splits[0]
                mobilenumber = splits[1]
                firstName = splits[2]
                lastName = splits[3]
                dob = date
                hostel = splits[5]
                userrole = "student"
                university = "OU"
                college = "VASV"
                branch = "CSE"
                section = "A"
                startYear = "2016"
                endYear = "2020"
                enabled = true
                accountNonExpired = true
                accountNonLocked = true
                credentialsNonExpired = true
            }
            user.addRole("ROLE_USER")
            def uniqueid = serviceUtilities.generateFileName(user.getUniversity(), user.getCollege(), user.getBranch(), user?.getSection(), user.getStartYear(), user.getEndYear())
            user.uniqueclassid = uniqueid
            userRepository.save(user)
            println("user created is ${user}")
        }
        new ResponseEntity<>("success",HttpStatus.OK)
    }
}

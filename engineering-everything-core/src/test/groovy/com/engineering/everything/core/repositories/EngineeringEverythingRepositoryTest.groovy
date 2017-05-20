package com.engineering.everything.core.repositories

import api.User
import com.engineering.core.repositories.UserRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.test.context.junit4.SpringRunner
import spock.lang.Specification

import java.time.LocalDateTime

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * Created by GnyaniMac on 13/05/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EngineeringEverythingRepositoryTest.class)
@EnableMongoRepositories(basePackageClasses = UserRepository.class)
@ComponentScan
class EngineeringEverythingRepositoryTest extends Specification{

    @Autowired
    UserRepository userRepository

    @Test
    public void TestUserRepo1(){
        Date dob = new Date(1994,25,10);
        def User1 = new User("test123456@gmailcom","destro123","destro123","gnyani","nath","ou","vasv","cse","071","B","4","2","No",dob,"741600585",LocalDateTime.now());
        userRepository.insert(User1);
        def deleteUser = userRepository.findByEmail(User1.getEmail())
        deleteUser.setConfirmpassword(User1.getConfirmpassword())
        assertEquals("User registered and user retrieved are not equal",User1,deleteUser)
        def numberofrecords = userRepository.deleteUserByEmail(deleteUser.getEmail())
        assertNotNull("number of records deleted are null",numberofrecords)
    }

    @Test
    public void TestUserRepo2(){
        Date dob = new Date(1994,25,10);
        def flag = false
        def User1 = new User("test123456@gmailcom","destro123","destro123","gnyani","nath","ou","vasv","cse","071","B","4","2","No",dob,"741600585",LocalDateTime.now());
        userRepository.insert(User1);
        def User2 = new User("test123456@gmailcom","destro123","destro12","gnyani","nath","ou","vasv","cse","071","B","4","2","No",dob,"741600585",LocalDateTime.now());
        try{
            userRepository.insert(User2)
        }
        catch(Exception e){
             flag = true
        }
        def deleteUser = userRepository.findByEmail(User1.getEmail())
        def numberofrecords = userRepository.deleteUserByEmail(deleteUser.getEmail())
        assertNotNull("number of records deleted are null",numberofrecords)
        assertEquals("Expected an Exception but got succeded",flag, true)
    }
}

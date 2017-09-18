package com.engineering.Application.Controller

import api.user.Otp
import api.user.User
import com.engineering.core.Service.ServiceUtilities
import com.engineering.core.Service.FilenameGenerator
import com.engineering.core.Service.SendSMS
import com.engineering.core.repositories.OtpRepository
import com.engineering.core.repositories.UserRepository
import com.mongodb.DuplicateKeyException
import constants.UserRoles
import groovy.json.JsonOutput
import groovy.json.JsonSlurper;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid;


/**
 * Created by GnyaniMac on 03/06/17.
 */
@RestController
class UserRegGoogleContoller {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ServiceUtilities serviceUtilities;

    @Autowired
    private SendSMS sendSMS;

    @Autowired
    private OtpRepository otpRepository;


    def jsonSlurper = new JsonSlurper()


    @GetMapping(value="/user/loggedin" ,produces = "application/json")
    public ResponseEntity<?> loggeduser(Authentication auth) {
        String email = serviceUtilities.parseEmail(auth)
        User user = userRepository.findByEmail(email)
        new ResponseEntity<>(user,HttpStatus.OK)
    }


    @GetMapping(value="/user/google/auth")
    public ResponseEntity<?> auth(OAuth2Authentication auth)
    {
         new ResponseEntity<>(auth,HttpStatus.OK)
    }

    @PostMapping(value="/users/registration", produces ="application/json" )
    public ResponseEntity<?> userRegistration(@Valid @RequestBody User user,OAuth2Authentication auth)
    {
        User userinserted
        def authJson= JsonOutput.toJson( auth.getUserAuthentication().getDetails())
        def Json = jsonSlurper.parseText(authJson);
        String email = Json."email"
        String propicurl = Json.picture
        if(userRepository.findByEmail(email))
        {
            return new ResponseEntity<>("Please register with other email address,this email already exists",HttpStatus.BAD_REQUEST)
        }
        user.setEmail(email)
        if(user.userrole == UserRoles.STUDENT.value) {
            user.setEndYear(((user.getStartYear() as Integer) + 4).toString())
            String uniqueClassId = serviceUtilities.generateFileName(user.getUniversity(), user.getCollege(), user.getBranch(), user?.getSection(), user.getStartYear(), user.getEndYear())
            user.setUniqueclassid(uniqueClassId);
        }
        try {
            user.setEnabled(true);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.addRole("ROLE_USER");
            user.setGooglepicUrl(propicurl);
            userinserted = userRepository.save(user);
        }
        catch(Exception e){
            if(e instanceof  org.springframework.dao.DuplicateKeyException)
                return new ResponseEntity<>("Mobile Number already exists",HttpStatus.BAD_REQUEST)
            else
                return new ResponseEntity<>("Error occurred while registering user please try again after sometime${e.getClass()}",HttpStatus.INTERNAL_SERVER_ERROR)
        }

        userinserted ? new ResponseEntity<>("User registration successful",HttpStatus.CREATED) :  new ResponseEntity<>("Something went wrong ",HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @PostMapping(value="/users/details/updateprofile", produces ="application/json" )
    public ResponseEntity<?> userDetailsUpdate( @RequestBody User updateduser,OAuth2Authentication auth) {

        String email = serviceUtilities.parseEmail(auth)
        User userTest1 = userRepository.findByEmail(email);

        if(updateduser.getDob() != null)
        {
            userTest1.setDob(updateduser.getDob())
        }
        if (updateduser.getFirstName() != null) {
            userTest1.setFirstName(updateduser.getFirstName())
        }
        if (updateduser.getLastName() != null) {
            userTest1.setLastName(updateduser.getLastName())
        }

        try {
            userRepository.save(userTest1)
        }
        catch(Exception e){
             new ResponseEntity<>("Error occurred while registering user please try again after sometime" + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR)
        }

         new ResponseEntity<>("User updation successful for user "+userTest1.getEmail(),HttpStatus.CREATED)


    }

    @PostMapping("/user/generate/otp")
    public ResponseEntity<?> generateOtp(@RequestBody String number,OAuth2Authentication auth2Authentication) {
        String email = serviceUtilities.parseEmail(auth2Authentication)
        //generating OTP
        Otp otp = new Otp()
        otp.setEmail(email)
        int rand = 100000 + (int) (Math.random() * ((999999 - 100000) + 1))
        otp.setOtp(rand)
        otpRepository.save(otp)
        //sending SMS
        // def status = sendSMS.sendSms(number.substring(1), otp.getOtp())
        //println("status is${status}")
        //check whether status is successful or not
        new ResponseEntity<?>("success",HttpStatus.OK)
    }

    @PostMapping(value="/user/validate/otp")
    public ResponseEntity<?> validateOtp(@RequestBody int otp,OAuth2Authentication auth2Authentication){
        def email = serviceUtilities.parseEmail(auth2Authentication)
        def otpfromrepo = otpRepository.findByEmail(email)
        println("otp is ${otp} from repo is ${otpfromrepo.getOtp()}")
        if(otpfromrepo.getOtp() == otp)
             new ResponseEntity<>("success",HttpStatus.OK)
        else
            new ResponseEntity<>("failure",HttpStatus.BAD_REQUEST)
    }



    @GetMapping(value="/user/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response)
    {
        request.getSession().invalidate();
        SecurityContextHolder.getContext().setAuthentication(null);
        new ResponseEntity<>("logout successful",HttpStatus.OK)
    }

}





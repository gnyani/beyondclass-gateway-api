package com.engineering.Application.Controller

import api.user.Otp
import api.user.User
import com.engineering.core.Service.ServiceUtilities
import com.engineering.core.Service.SendSMS
import com.engineering.core.repositories.OtpRepository
import com.engineering.core.repositories.UserRepository
import constants.UserRoles
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.slf4j.Logger
import org.slf4j.LoggerFactory;
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

    private static Logger log = LoggerFactory.getLogger(UserRegGoogleContoller.class)


    def jsonSlurper = new JsonSlurper()


    @GetMapping(value="/user/loggedin" ,produces = "application/json")
    public ResponseEntity<?> loggeduser(Authentication auth) {

        String email = serviceUtilities.parseEmail(auth)
        User user = userRepository.findByEmail(email)
       try{
        if (user.userrole.compareTo("teacher") == 0) {
            HashMap<String, String> StudentCountList = new HashMap<String, String>();
            for (int i = 0; i < user.batches.length ; i++) {
                String UniqueClassId = serviceUtilities.generateUniqueClassIdForTeacher(user.batches[i], user.email)
                String NumberOfStudents = userRepository.countByUniqueclassid(UniqueClassId).toString()
                StudentCountList.put(user.batches[i], NumberOfStudents)
            }
            user.StudentCountList = StudentCountList
        }}
       catch(Exception e){
           log.error("Exception while counting the number of students per batch for teacher role")
           return new ResponseEntity<>("not found", HttpStatus.INTERNAL_SERVER_ERROR)
       }

        user ? new ResponseEntity<>(user, HttpStatus.OK) : new ResponseEntity<>("not found", HttpStatus.NOT_FOUND)
    }






    @GetMapping(value="/user/google/auth")
    public ResponseEntity<?> auth(OAuth2Authentication auth)
    {
        log.info("<UserRegistration>["+serviceUtilities.parseEmail(auth)+"](authenticated)")
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
        log.info("<userRegistration>["+serviceUtilities.parseEmail(auth)+"](user registered)")
        userinserted ? new ResponseEntity<>("User registration successful",HttpStatus.CREATED) :  new ResponseEntity<>("Something went wrong ",HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @PostMapping(value="/users/details/updateprofile", produces ="application/json" )
    public ResponseEntity<?> userDetailsUpdate( @RequestBody User updateduser,OAuth2Authentication auth) {

        String email = serviceUtilities.parseEmail(auth)
        User userTest1 = userRepository.findByEmail(email);
        log.info("<Questions>["+email+"](get all Questions)")
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
        log.info("<userRegistration>["+serviceUtilities.parseEmail(auth)+"](user details update)")
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
        //def status = sendSMS.sendSms(number.substring(1), otp.getOtp())
        //log.info("<userRegistration>["+serviceUtilities.parseEmail(auth2Authentication)+"](otp statis is " +status + ")")
        //check whether status is successful or not
        new ResponseEntity<?>("success",HttpStatus.OK)
    }

    @PostMapping(value="/user/validate/otp")
    public ResponseEntity<?> validateOtp(@RequestBody int otp,OAuth2Authentication auth2Authentication){
        def email = serviceUtilities.parseEmail(auth2Authentication)
        def otpfromrepo = otpRepository.findByEmail(email)
        log.info("<userRegistration>["+serviceUtilities.parseEmail(auth2Authentication)+"](otp is " + otpfromrepo.Otp + ")")
        if(otpfromrepo.getOtp() == otp)
             new ResponseEntity<>("success",HttpStatus.OK)
        else
            new ResponseEntity<>("failure",HttpStatus.BAD_REQUEST)
    }



    @GetMapping(value="/user/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, OAuth2Authentication auth2Authentication)
    {
        request.getSession().invalidate();
        SecurityContextHolder.getContext().setAuthentication(null);
        log.info("<userRegistration>["+serviceUtilities.parseEmail(auth2Authentication)+"](user logged out)")
        new ResponseEntity<>("logout successful",HttpStatus.OK)
    }
}





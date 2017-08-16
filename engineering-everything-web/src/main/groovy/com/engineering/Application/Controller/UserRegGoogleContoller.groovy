package com.engineering.Application.Controller;

import api.Location;
import api.Organisation
import api.User;
import api.UserGoogle
import com.engineering.core.Service.DetailsValidator
import com.engineering.core.Service.EmailGenerationService;
import com.engineering.core.Service.LocationService;
import com.engineering.core.repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper
import constants.BranchNames
import constants.Colleges
import constants.Sections
import constants.Semester
import constants.Universities
import constants.year
import groovy.json.JsonOutput
import groovy.json.JsonSlurper;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.gridfs.GridFsTemplate
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
    EmailGenerationService emailGenerationService;


    @Autowired
    private DetailsValidator validation;

    @Autowired
    private LocationService locationUtility;


    def jsonSlurper = new JsonSlurper()


    @RequestMapping("/user")
    public UserGoogle sayHello(OAuth2Authentication auth) throws IOException {
        LinkedHashMap m = (LinkedHashMap) auth.getUserAuthentication().getDetails();

        ObjectMapper mapper = new ObjectMapper();
        URL url = new URL("http://ip-api.com/json");
        URLConnection uc = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        String line = reader.readLine();
        JsonNode array = mapper.readValue(line, JsonNode.class);
        Location location = mapper.readValue(array.traverse(), Location.class);

        m.put("location",location);
        UserGoogle u = new UserGoogle(m);
        userRepository.save(u);

        return u ;
    }

    //This method is important not unused.
    @RequestMapping("/user/validate")
    public String validateuserexistence(Authentication auth) {
        String email = emailGenerationService.parseEmail(auth)
        User present = userRepository.findByEmail(email);
        if (present != null)
        {
            return true
        }
        else {
            return false
        }
    }

    @CrossOrigin(origins = ["http://localhost:8081","http://localhost:3000"])
    @RequestMapping(value="/user/loggedin" ,produces = "application/json")
    public Object loggeduser(Authentication auth) {
        String email = emailGenerationService.parseEmail(auth)
        User user = userRepository.findByEmail(email)
       return user
    }

    @CrossOrigin(origins = ["http://localhost:8081","http://localhost:3000"])
    @RequestMapping(value="/user/isloggedin" ,method = RequestMethod.GET)
    public Object isloggedin(Authentication auth)
    {
        if(auth){
            return true
        }else{
            return  false
        }
    }

    @CrossOrigin(origins = ["http://localhost:8081","http://localhost:3000"])
    @RequestMapping(value="/user/google/auth" ,method = RequestMethod.GET)
    public Object auth(OAuth2Authentication auth)
    {
        return auth
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value="/user/dummy" , produces ="application/json" , method = RequestMethod.POST)
    public String dummy()
    {
        return "test";
    }


    @CrossOrigin(origins = ["http://localhost:8081","http://localhost:3000"])
    @RequestMapping(value="/user/propic" ,method = RequestMethod.GET)
    public Object propicUrl(Authentication auth)
    {
        def m = JsonOutput.toJson(auth)
        def Json = jsonSlurper.parseText(m);
        String email = Json.userAuthentication.details.email
        String propicurl = Json.userAuthentication.details.picture
        User user = userRepository.findByEmail(email)
        if(user)
        return (user.getNormalpicUrl()?:user.getGooglepicUrl())
        else
         return propicurl
    }
    @RequestMapping(value="/user/authobj" ,method = RequestMethod.GET)
    public Object giveauthobj(Authentication auth)
    {
        return  auth;
    }

    @CrossOrigin(origins = ["http://localhost:8081","http://localhost:3000"])
    @RequestMapping(value="/users/registration", produces ="application/json" ,method = RequestMethod.POST)
    public String userRegistration(@Valid @RequestBody User user,OAuth2Authentication auth)
    {
        User registered
        String response
        // System.out.println("USer is"+user + "email is " + user.getEmail())
        //***************************VALIDATION****************************\\

//        // code for filtering universities
//        def validUniv = validation.refineUniv(user.getUniversity());
//        if(validUniv.getValid())
//        {
//            user.setUniversity(validUniv.getResult())
//        } else{
//            return "please enter valid University names from "+Universities.values();
//        }
//        // code for refining college names
//        def validCol = validation.refineCollege(user.getCollege());
//        if(validCol.getValid())
//        {
//            user.setCollege(validCol.getResult())
//        } else{
//            return "please enter valid College names from "+Colleges.values();
//        }
//        // code for refining year
//        def validYear = validation.refineYear(user.getYear());
//        if(validYear.getValid())
//        {
//            user.setYear(validYear.getResult())
//        } else{
//            return "please enter valid year from"+ year.values();
//        }
//        //code for refining semester
//        def validSem = validation.refineSemester(user.getSem());
//        if(validSem.getValid())
//        {
//            user.setSem(validSem.getResult())
//        } else{
//            return "please enter valid Semester "+ Semester.values();
//        }
//        // code for filtering branch names
//        def validBranch = validation.refineBranch(user.getBranch());
//        if(validBranch.getValid())
//        {
//            user.setBranch(validBranch.getResult())
//        } else{
//            return "please enter valid branch names from "+BranchNames.values();
//        }
//        // code for refining section
//        def validSec = validation.refineSection(user.getSection());
//        if(validSec.getValid())
//        {
//            user.setSection(validSec.getResult())
//        } else{
//            return "please enter valid section from "+ Sections.values();
//        }

        def m = JsonOutput.toJson( auth.getUserAuthentication().getDetails())

        def Json = jsonSlurper.parseText(m);
        // println(" json is " + Json)
        String email = Json."email"
        String propicurl = Json.picture
        if(userRepository.findByEmail(email))
        {
            return "Please register with other email address,this email already exists"
        }
        user.setEmail(email)
        //******************************VALIDATION DONE **********************\\
        try {
            user.setUsername(email)
            user.setEnabled(true);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.addRole("ROLE_USER");
            user.setGooglepicUrl(propicurl);
            //saving to collection
            registered = userRepository.insert(user);
        }
        catch(Exception e){
            return "Error occurred while registering user please try again after sometime" + e.getMessage()
        }
        response="User registration successful"
        return response
    }

    @CrossOrigin(origins = ["http://localhost:8081","http://localhost:3000"])
    @RequestMapping(value="/users/details/updateprofile", produces ="application/json" ,method = RequestMethod.POST)
    public String userDetailsUpdate( @RequestBody User updateduser,HttpServletRequest request, HttpServletResponse response,OAuth2Authentication auth) {

        String email = emailGenerationService.parseEmail(auth)
        User userTest1 = userRepository.findByEmail(email);

        //  println("retrieved object is"+userTest1)
        //Should improve validations for both mobile number and DOB
        if(updateduser.getMobilenumber() != null){
            if(updateduser.getMobilenumber().size() == 10){
                userTest1.setMobilenumber(updateduser.getMobilenumber())
            }
            else{
                return "please enter a valid mobile number"
            }
        }

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
        if (updateduser.getYear() != null) {
            def validYear = validation.refineYear(updateduser.getYear());
            if(validYear.getValid())
            {
                userTest1.setYear(validYear.getResult())
            } else{
                return "please enter valid year from"+ year.values();
            }
        }
        if(updateduser.getSem() != null){

            def validSem = validation.refineSemester(updateduser.getSem());
            if(validSem.getValid())
            {
                userTest1.setSem(validSem.getResult())
            } else{
                return "please enter valid Semester "+ Semester.values();
            }
        }


        if (updateduser.getSection() != null) {
            def validSec = validation.refineSection(updateduser.getSection());
            if (validSec.getValid()) {
                userTest1.setSection(validSec.getResult())
            } else {
                return "please enter valid section from " + Sections.values();
            }
        }

        try {
            userRepository.save(userTest1)
        }
        catch(Exception e){
            return "Error occurred while registering user please try again after sometime" + e.getMessage()
        }

        return "User updation successful for user "+userTest1.getEmail()


    }



    @CrossOrigin(origins = ["http://localhost:8081","http://localhost:3000"])
    @RequestMapping(value="/user/logout")
    public Object logout(HttpServletRequest request, HttpServletResponse response)
    {
        request.getSession().invalidate();
        SecurityContextHolder.getContext().setAuthentication(null);
        return "logout successful"
    }



    @RequestMapping(value = "/storeHangout",produces = "application/json",method = RequestMethod.POST)
    @ResponseBody
    public Object post(@RequestBody Organisation org){
        System.out.print("organization" + org);
        return org;
    }
}




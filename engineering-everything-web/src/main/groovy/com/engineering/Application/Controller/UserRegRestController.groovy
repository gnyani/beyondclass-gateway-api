package com.engineering.Application.Controller

import com.engineering.core.Service.DetailsValidator
import api.UserLogin
import constants.BranchNames

import api.User
import constants.Colleges
import constants.Sections
import constants.Semester
import constants.Universities
import constants.year;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import com.engineering.core.repositories.UserRepository

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid


/**
 * Created by GnyaniMac on 27/04/17.
 */

@EnableMongoRepositories(basePackageClasses = UserRepository.class)
@RestController
class UserRegRestController {

    @Autowired
    public UserRepository repository;


    DetailsValidator validation = new DetailsValidator();

    @RequestMapping(value="/users/registration", produces ="application/json" ,method = RequestMethod.POST)
    public String userRegistration(@Valid @RequestBody User user)
    {
        User registered
        String response
       // System.out.println("USer is"+user + "email is " + user.getEmail())
        //***************************VALIDATION****************************\\
        //code for checking password and confirm password might have to implement this from UI side
        if(!(user.getPassword().equals(user.getConfirmpassword()))){
            return "Confirm password must match password"
        }
        // code for filtering universities
        def validUniv = validation.refineUniv(user.getUniversity());
        if(validUniv.getValid())
        {
            user.setUniversity(validUniv.getResult())
        } else{
            return "please enter valid University names from "+Universities.values();
        }
        // code for refining college names
        def validCol = validation.refineCollege(user.getCollege());
        if(validUniv.getValid())
        {
            user.setCollege(validCol.getResult())
        } else{
            return "please enter valid College names from "+Colleges.values();
        }
        // code for refining year
        def validYear = validation.refineYear(user.getYear());
        if(validYear.getValid())
        {
            user.setYear(validYear.getResult())
        } else{
            return "please enter valid year from"+ year.values();
        }
        //code for refining semester
        def validSem = validation.refineSemester(user.getSem());
        if(validSem.getValid())
        {
            user.setSem(validSem.getResult())
        } else{
            return "please enter valid Semester "+ Semester.values();
        }
        // code for filtering branch names
        def validBranch = validation.refineBranch(user.getBranch());
        if(validBranch.getValid())
        {
            user.setBranch(validBranch.getResult())
        } else{
            return "please enter valid branch names from "+BranchNames.values();
        }
        // code for refining section
        def validSec = validation.refineSection(user.getSection());
        if(validSec.getValid())
        {
            user.setSection(validSec.getResult())
        } else{
            return "please enter valid section from "+ Sections.values();
        }
        //code for validating email addresses
        def validEmail = validation.refineEmail(user.getEmail())
        if(repository.findByEmail(validEmail.getResult()))
        {
            return "Please register with other email address,this email already exists"
        }
        user.setEmail(validEmail.getResult())
        //******************************VALIDATION DONE **********************\\
        try {
            //saving to collection
            registered = repository.save(user);
        }
        catch(Exception e){
            return "Error occurred while registering user please try again after sometime" + e.getMessage()
    }
        response="User registration successful for "+registered.getFirstName()+ " with email " + registered.getEmail()
        return response
    }
    //should be deprecated vulnerable end point
    @RequestMapping(value="/users" ,method = RequestMethod.GET)
    public String listUsers()
    {   System.out.println("lsiting users now");
        String name =repository.findAll()
        return  name;
    }

    @RequestMapping(value="/users/login",method = RequestMethod.POST)
   public String logIn(@RequestBody UserLogin person, HttpServletRequest request, HttpServletResponse response)
    {
        println(person.getEmail())
        if (!((person.getEmail().isEmpty()) || (person.getPassword().isEmpty()))) {

            User u = repository.findByEmail(person.getEmail());
            println(u.toString());

            if (u != null) {
                if (u.getPassword().equals(person.getPassword())) {
                    request.getSession().setAttribute("LOGGEDIN_USER", u);
                    return "Login successful"
                }else{
                    return "please enter correct password"
                }
            }else{
               return "please signup before Logining in"
            }

        }else{
            return "enter both username and passoword"
        }

    }

    @RequestMapping(value="/users/logout",method = RequestMethod.GET)
    public String logout(HttpServletRequest request, HttpServletResponse response)
    {
        User userLogin = request.getSession().getAttribute("LOGGEDIN_USER");
        request.getSession().invalidate();
        return "Logout successful for user " +userLogin.getEmail();
    }

}

package com.engineering.Application.Controller

import api.User
import api.UserLogin
import constants.BranchNames
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import repositories.UserRepository

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
     String response


    @RequestMapping(value="/users/registration", produces ="application/json" ,method = RequestMethod.POST)
    public String userRegistration(@Valid @RequestBody User user)
    {   User registered
        Boolean flag = false
        //code for checking password and confirm password
        if(!(user.getPassword().equals(user.getConfirmpassword()))){
            return "Confirm password must match password"
        }

        // code for filtering branch names
        def branch = user.getBranch()
        for (BranchNames type : BranchNames.values()) {
            if ((type.name().equalsIgnoreCase(branch))) {
                flag=true
            }
        }
        if (!flag){
            return "please enter a valid branch name in"+ BranchNames.values();
        }

        //code for filtering "." from email addresses

        try {
            if(repository.findByEmail(user.getEmail())) {
                return "User already exists please sign up with other email address"
            }
            user.setBranch(branch.toUpperCase())
            registered = repository.save(user);
        }
        catch(Exception e){
            return "Error occurred while registering user please try again after sometime" + e.getMessage()
    }
        response="User registration successful for "+registered.getFirstName()+ " with email " + registered.getEmail()
        return response
    }
    @RequestMapping(value="/users" ,method = RequestMethod.GET)
    public String listUsers()
    {   System.out.println("lsiting users now");
        String name =repository.findAll()
        return  name;
    }
    @RequestMapping(value="/users/login",method = RequestMethod.POST)
   public String logIn(@RequestBody UserLogin person, HttpServletRequest request, HttpServletResponse response)
    {
        if (!((person.getEmail().isEmpty()) || (person.getPassword().isEmpty()))) {

            UserLogin u = repository.findByEmail(person.getEmail());

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

}

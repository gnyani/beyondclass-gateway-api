package Controller

import api.User
import constants.BranchNames
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import repositories.UserRepository

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
            registered = repository.insert(user);
        }
        catch(Exception e){
            return "Error occurred while registering user please try again after sometime"
    }
        response="User registration successful for "+registered.getFirstName()+ " with email " + registered.getEmail()
        return response
    }
    @RequestMapping(value="/users" ,method = RequestMethod.GET)
    public String listUsers()
    {
        String name =repository.findAll()
        return  name;
    }

}

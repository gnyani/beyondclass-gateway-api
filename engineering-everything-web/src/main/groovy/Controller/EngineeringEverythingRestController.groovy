package Controller

import api.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import repositories.UserRepository

/**
 * Created by GnyaniMac on 27/04/17.
 */
@EnableMongoRepositories(basePackageClasses = UserRepository.class)
@RestController
class EngineeringEverythingRestController {

    @Autowired
    public UserRepository repository;
     String response


    @RequestMapping(value="/users/registration", produces ="application/json" ,method = RequestMethod.POST)
    public String userRegistration(@RequestBody User user)
    {   User registered
        registered=repository.save(user);
        response="User registration successful for "+registered.firstName
        return response
    }
    @RequestMapping(value="/users" ,method = RequestMethod.GET)
    public String listUsers()
    {
        String name =repository.findAll()
        return  name;
    }

}

package repositories;


import api.User
import org.springframework.data.mongodb.repository.MongoRepository;



/**
 * Created by GnyaniMac on 27/04/17.
 */

public interface UserRepository extends MongoRepository<User,String> {
    public User findByFirstName(String firstName);
    public List<User> findByLastName(String lastName);
}

package api.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by GnyaniMac on 27/08/17.
 */

@Document(collection = "otp")
public class Otp {

    @Id
    String email;

    int Otp;
}
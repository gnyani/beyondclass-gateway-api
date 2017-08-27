package api;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by GnyaniMac on 27/08/17.
 */

@Document(collection = "otp")
public class Otp {

    @Id
    public  String email;

    public int Otp;

    public String getEmail() {
        return email;
    }

   public void setEmail(String email) {
        this.email = email;
    }

    public int getOtp() {
        return Otp;
    }

   public void setOtp(int otp) {
        Otp = otp;
    }
}
package api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by GnyaniMac on 23/08/17.
 */
public class updateprofile {

    @JsonProperty
    private  String firstName;

    @JsonProperty
    private  String lastName;

    @JsonProperty
    private  String year;

    @JsonProperty
    private  String sem;

    @JsonProperty
    private Date dob;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getYear() {
        return year;
    }

    public String getSem() {
        return sem;
    }

    public Date getDob() {
        return dob;
    }
}

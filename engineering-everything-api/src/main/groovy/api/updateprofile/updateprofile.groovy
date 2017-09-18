package api.updateprofile;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by GnyaniMac on 23/08/17.
 */
public class updateprofile {

    @JsonProperty
    String firstName;

    @JsonProperty
    String lastName;

    @JsonProperty
    Date dob;
}

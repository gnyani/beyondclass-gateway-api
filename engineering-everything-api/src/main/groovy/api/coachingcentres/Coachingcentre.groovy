package api.coachingcentres

import com.fasterxml.jackson.annotation.JsonProperty;

import constants.Area;
import constants.City;
import constants.CoachingCentreType;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * Created by GnyaniMac on 09/07/17.
 */
@Document(collection = "coachingcentres")

public class Coachingcentre {


    @Id
    String coachingcentreId;

    @JsonProperty
    @NotNull
    @NotEmpty(message = "type cannot be null")
    CoachingCentreType type;

    @NotEmpty(message = "plese provide city ")
    @JsonProperty
    City city;

    @NotNull
    @NotEmpty(message = "please fill in the area code " )
    @JsonProperty
    Area area;


    @JsonProperty
    @NotEmpty(message = "Name cannot be empty")
    @NotNull
    String orgname;

    @JsonProperty
    @NotEmpty(message = "it is better to have some good description")
    @NotNull
    String description;

    @JsonProperty
    @NotEmpty(message = "contact info cannot be null")
    @NotNull
    ContactInfo contactinfo;

    float rating = 4;

    @JsonProperty
    @Transient
    byte[] feedetails;

    String feesdetailsUrl;
}

package api;

import com.fasterxml.jackson.annotation.JsonProperty;

import constants.Area;
import constants.City;
import constants.CoachingCentreType;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * Created by GnyaniMac on 09/07/17.
 */
@Document(collection = "coachingcentres")
public class Coachingcentre {

    @JsonProperty
    @NotNull
    @NotEmpty(message = "type cannot be null")
    public CoachingCentreType type;

    @NotEmpty(message = "plese provide city ")
    @JsonProperty
    public City city;

    @NotNull
    @NotEmpty(message = "please fill in the area code " )
    @JsonProperty
    public Area area;


    @JsonProperty
    @NotEmpty(message = "Name cannot be empty")
    @NotNull
    public  String orgname;

    @JsonProperty
    @NotEmpty(message = "it is better to have some good description")
    @NotNull
    public String description;

    @JsonProperty
    @NotEmpty(message = "contact info cannot be null")
    @NotNull
    public ContactInfo contactinfo;

    //public Review review;

    @JsonProperty
    public byte[] feedetails;

    public CoachingCentreType getType() {
        return type;
    }

    public void setType(CoachingCentreType type) {
        this.type = type;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ContactInfo getContactinfo() {
        return contactinfo;
    }

    public void setContactinfo(ContactInfo contactinfo) {
        this.contactinfo = contactinfo;
    }

//    public Review getReview() {
//        return review;
//    }
//
//    public void setReview(Review review) {
//        this.review = review;
//    }

    public byte[] getFeedetails() {
        return feedetails;
    }

    public void setFeedetails(byte[] feedetails) {
        this.feedetails = feedetails;
    }
}

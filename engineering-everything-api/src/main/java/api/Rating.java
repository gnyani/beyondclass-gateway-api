package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * Created by GnyaniMac on 16/07/17.
 */
@Document(collection = "rating")
public class Rating {

    public String coachingcentreId;

    @JsonProperty
    @NotNull
    @NotEmpty(message = "please give star rating")
    public float rating;

    public String email;

    @JsonProperty
    public String review;

    @Id
    public  String ReviewID ;

    public String getReviewID() {
        return ReviewID;
    }

    public void setReviewID(String UID) {
        this.ReviewID = UID;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getCoachingcentreId() {
        return coachingcentreId;
    }

    public void setCoachingcentreId(String coachingcentreId) {
        this.coachingcentreId = coachingcentreId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

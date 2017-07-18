package api;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by GnyaniMac on 16/07/17.
 */
@Document(collection = "rating")
@CompoundIndex(name = "Rating", def = "{'coachingcentreId' : 1, 'email': 1}")
public class Rating {

    public String coachingcentreId;

    public float rating;

    public String email;

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

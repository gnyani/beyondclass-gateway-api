package api.coachingcentres;

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

     String coachingcentreId;

    @JsonProperty
    @NotNull
    @NotEmpty(message = "please give star rating")
    float rating;

    String email;

    @JsonProperty
    String review;

    @Id
    String ReviewID ;
}

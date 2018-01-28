package api.announcements

import api.user.UserDetails;
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull

/**
 * Created by GnyaniMac on 01/07/17.
 */

@EqualsAndHashCode
@ToString
@Document(collection = "announcements")

public class Announcements {

    public static final int expireAfterSeconds = 604800 //7days

    @Id
    String announcementid;

    String username;

    UserDetails posteduser;

    @Indexed(expireAfterSeconds = Announcements.expireAfterSeconds)
    Date createdAt = new Date();

    @JsonProperty
    @NotNull
    @NotEmpty
    String message;

}

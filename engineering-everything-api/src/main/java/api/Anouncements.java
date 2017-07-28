package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by GnyaniMac on 01/07/17.
 */

@Document(collection = "announcements")

public class Anouncements {

    @Id
    private String aid;

    private String username;

    private User posteduser;

    @Indexed(expireAfterSeconds = 24*60*60*7)
    private Date createdAt = new Date();

    @Indexed
    private String classId;


    @JsonProperty
    @NotNull
    @NotEmpty
    private String message;

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public User getPosteduser() {
        return posteduser;
    }

    public void setPosteduser(User posteduser) {
        this.posteduser = posteduser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

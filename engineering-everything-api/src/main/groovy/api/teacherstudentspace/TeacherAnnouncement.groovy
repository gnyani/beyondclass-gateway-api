package api.teacherstudentspace

import api.user.UserDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Created by GnyaniMac on 14/08/17.
 */
@Document(collection = "teacher-announcements")
public class TeacherAnnouncement {

    @Id
    String announcementid;

    @JsonProperty
    String message;

    @JsonProperty
    String batch;

    UserDetails posteduser;


    Date createdAt = new Date();

}

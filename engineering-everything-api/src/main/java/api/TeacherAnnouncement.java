package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by GnyaniMac on 14/08/17.
 */
@Document(collection = "teacher-announcements")
public class TeacherAnnouncement {

    @Id
    private String announcementid;

    @JsonProperty
    private String message;


    @JsonProperty
    private String teacherclass;

    private User user;

    private Date createdAt = new Date();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAnnouncementid() {
        return announcementid;
    }

    public void setAnnouncementid(String announcementid) {
        this.announcementid = announcementid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getTeacherclass() {
        return teacherclass;
    }

    public void setTeacherclass(String teacherclass) {
        this.teacherclass = teacherclass;
    }
}

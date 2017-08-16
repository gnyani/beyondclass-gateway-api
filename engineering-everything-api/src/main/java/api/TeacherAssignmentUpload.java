package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by GnyaniMac on 15/08/17.
 */
@Document(collection = "teacher-assignments")
public class TeacherAssignmentUpload {

    @Id
    private String assignmentid;

    @JsonProperty
    private String teacherclass;

    @JsonProperty
    private  String subject;

    private User user;

    @JsonProperty
    private byte[] file;

    private Date date = new Date();

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAssignmentid() {
        return assignmentid;
    }

    public void setAssignmentid(String assignmentid) {
        this.assignmentid = assignmentid;
    }

    public String getTeacherclass() {
        return teacherclass;
    }

    public void setTeacherclass(String teacherclass) {
        this.teacherclass = teacherclass;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}

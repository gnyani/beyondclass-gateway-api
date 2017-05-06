package api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by GnyaniMac on 06/05/17.
 */
public class Subject {

    @JsonProperty
    @NotEmpty
    @NotNull
    public String subject;

    public Subject(){

    }
    @JsonCreator
    public Subject(@JsonProperty("subject") String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}

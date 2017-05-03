package api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by GnyaniMac on 02/05/17.
 */
public class Subject {

    @JsonProperty
    private String subject;

    public Subject()
    {

    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}

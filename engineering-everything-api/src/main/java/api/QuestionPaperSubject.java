package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by GnyaniMac on 02/05/17.
 */
public class QuestionPaperSubject {

    @JsonProperty
    @NotNull
    @NotEmpty
    private String subject;

    @JsonProperty
    @NotNull
    @NotEmpty
    private String qpyear;

    public QuestionPaperSubject()
    {

    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getQpyear() {
        return qpyear;
    }

    public void setQpyear(String qpyear) {
        this.qpyear = qpyear;
    }
}

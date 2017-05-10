package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * Created by GnyaniMac on 02/05/17.
 */
@Document(collection = "questionpapers")

public class Questionpaper {

    @JsonProperty
    @NotEmpty
    @NotNull
    private String university;
    @JsonProperty
    @NotEmpty
    @NotNull
    private String college;
    @JsonProperty
    @NotEmpty
    @NotNull
    private String branch;
    @JsonProperty
    @NotEmpty
    @NotNull
    private String year;
    @JsonProperty
    @NotEmpty
    @NotNull
    private String sem;
    @JsonProperty
    @NotEmpty
    @NotNull
    private String subject;
    @JsonProperty
    @NotEmpty
    @NotNull
    private  String qpyear;

    public Questionpaper()
    {

    }

    public Questionpaper(String university, String college, String branch,  String year, String sem, String subject, String qpyear) {
        this.university = university;
        this.college = college;
        this.branch = branch;
        this.year = year;
        this.sem = sem;
        this.subject = subject;
        this.qpyear = qpyear;
    }


    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
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
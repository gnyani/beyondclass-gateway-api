package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.File;

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
    private String collegecode;
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

    public Questionpaper()
    {

    }

    public Questionpaper(String university, String collegecode, String branch, String year, String sem, String subject) {
        this.university = university;
        this.collegecode = collegecode;
        this.branch = branch;
        this.year = year;
        this.sem = sem;
        this.subject = subject;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getCollegecode() {
        return collegecode;
    }

    public void setCollegecode(String collegecode) {
        this.collegecode = collegecode;
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

}

package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;


/**
 * Created by GnyaniMac on 02/05/17.
 */
@Document(collection = "assignments")

public class Assignments{

    private String university;

    private String college;

    private String branch;

    private String section;

    private String year;

    private String sem;
    @JsonProperty
    @NotEmpty
    @NotNull
    private String subject;
    @JsonProperty
    @NotEmpty
    @NotNull
    private byte[] file;

    public Assignments()
    {

    }

    public Assignments(String university, String college, String branch, String section, String year, String sem, String subject, byte[] file) {
        this.university = university;
        this.college = college;
        this.branch = branch;
        this.section = section;
        this.year = year;
        this.sem = sem;
        this.subject = subject;
        this.file = file;
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
    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }


}

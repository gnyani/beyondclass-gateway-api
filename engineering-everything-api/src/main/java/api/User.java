package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import constants.BranchNames;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Date;


/**
 * Created by GnyaniMac on 27/04/17.
 */
@Document(collection = "users")
public class User {

    @JsonProperty
    @NotEmpty(message = "please enter your emailaddress")
    @NotNull(message = "please enter your emailAddress")
    @Email(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",message = "invalid email")
    @Id
    private String email;

    @JsonProperty
    @NotNull
    @NotEmpty(message = "please enter your password")
    @Size(max = 20 , min = 6 ,message = "password length should be in between 6 and 20")
    private String password;

    @JsonProperty
    @NotNull
    @NotEmpty(message = "please confirm your password")
    @Size(max = 20 , min = 6, message = "password length should be in between 6 and 20" )
    @Transient
    private String confirmpassword;

    @NotEmpty(message = "please enter your name")
    @NotNull(message = "please enter your name")
    @JsonProperty
     private String firstName;

    @JsonProperty
     private String lastName;

    @JsonProperty
    @NotEmpty(message = "university name cannot be null")
    @NotNull(message = "university name cannot be null")
    private String university;

    @JsonProperty
    @NotEmpty(message = "college name cannot be null")
    @NotNull(message = "college name cannot be null")
    private String college;

    @JsonProperty
    @NotEmpty(message = "please enter your branch name")
    @NotNull
    private String branch;

    @JsonProperty
    @NotEmpty(message = "please enter your roll number")
    @NotNull
    private String rollno;

    @JsonProperty
    @NotNull
    @NotEmpty(message = "please enter your section")
    private String section;


    @JsonProperty
    @NotNull
    @NotEmpty(message = "please enter year")
   // @Max(value = 4,message = "please enter year in range 1 to 4")@Min(value = 1,message = "please enter year in range 1 to 4")
    private String year;

    @JsonProperty
    @NotNull
    @NotEmpty(message = "please enter semester")
    private String sem;

    @JsonProperty

    private String hostel;

    @JsonProperty
    private Date dob;

    @JsonProperty
    @Pattern(regexp="(^$|[0-9]{10})",message = "please enter a valid mobile number")
    private String mobilenumber;

    private LocalDateTime registerdate = LocalDateTime.now();

    public User(){
    }

    public User(String email, String password, String confirmpassword, String firstName, String lastName,String university, String college, String branch, String rollno, String section, String year, String sem, String hostel, Date dob, String mobilenumber, LocalDateTime registerdate) {
        this.email = email;
        this.password = password;
        this.confirmpassword = confirmpassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.university = university;
        this.college = college;
        this.branch = branch;
        this.rollno = rollno;
        this.section = section;
        this.year = year;
        this.sem = sem;
        this.hostel = hostel;
        this.dob = dob;
        this.mobilenumber = mobilenumber;
        this.registerdate = registerdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
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

    public String getHostel() {
        return hostel;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public LocalDateTime getRegisterdate() {
        return registerdate;
    }

    public void setRegisterdate(LocalDateTime registerdate) {
        this.registerdate = registerdate;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!email.equals(user.email)) return false;
        if (!password.equals(user.password)) return false;
        if (!confirmpassword.equals(user.confirmpassword)) return false;
        if (!firstName.equals(user.firstName)) return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
        if (!university.equals(user.university)) return false;
        if (!college.equals(user.college)) return false;
        if (!branch.equals(user.branch)) return false;
        if (rollno != null ? !rollno.equals(user.rollno) : user.rollno != null) return false;
        if (!section.equals(user.section)) return false;
        if (!year.equals(user.year)) return false;
        if (!sem.equals(user.sem)) return false;
        if (hostel != null ? !hostel.equals(user.hostel) : user.hostel != null) return false;
        if (dob != null ? !dob.equals(user.dob) : user.dob != null) return false;
        if (mobilenumber != null ? !mobilenumber.equals(user.mobilenumber) : user.mobilenumber != null) return false;
        return registerdate != null ? registerdate.equals(user.registerdate) : user.registerdate == null;

    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + confirmpassword.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + university.hashCode();
        result = 31 * result + college.hashCode();
        result = 31 * result + branch.hashCode();
        result = 31 * result + (rollno != null ? rollno.hashCode() : 0);
        result = 31 * result + section.hashCode();
        result = 31 * result + year.hashCode();
        result = 31 * result + sem.hashCode();
        result = 31 * result + (hostel != null ? hostel.hashCode() : 0);
        result = 31 * result + (dob != null ? dob.hashCode() : 0);
        result = 31 * result + (mobilenumber != null ? mobilenumber.hashCode() : 0);
        result = 31 * result + (registerdate != null ? registerdate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", confirmpassword='" + confirmpassword + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", college='" + college + '\'' +
                ", branch='" + branch + '\'' +
                ", rollno='" + rollno + '\'' +
                ", section='" + section + '\'' +
                ", year='" + year + '\'' +
                ", sem='" + sem + '\'' +
                ", hostel='" + hostel + '\'' +
                ", dob=" + dob +
                ", mobilenumber='" + mobilenumber + '\'' +
                ", registerdate=" + registerdate +
                '}';
    }
}

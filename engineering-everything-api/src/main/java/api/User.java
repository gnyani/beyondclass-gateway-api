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
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by GnyaniMac on 27/04/17.
 */
@Document(collection = "users")
public class User {

    @Id
    private String email;

    @NotEmpty(message = "please enter your name")
    @NotNull(message = "please enter your name")
    @JsonProperty
     private String firstName;

    @JsonProperty
     private String lastName;

    @JsonProperty
    private String university;

    @JsonProperty
    private String college;

    @JsonProperty
    private String branch;

    @JsonProperty
    private String rollno;

    @JsonProperty
    private String section;


    @JsonProperty
   // @Max(value = 4,message = "please enter year in range 1 to 4")@Min(value = 1,message = "please enter year in range 1 to 4")
    private String year;

    @JsonProperty
    private String sem;

    @JsonProperty

    private String hostel;

    @JsonProperty
    private Date dob;

    @JsonProperty
    private String mobilenumber;

    @JsonProperty
    @NotNull
    @NotEmpty
    private String userrole;

    @JsonProperty
    private String[] classes;

    private String googlepicUrl;

    private String normalpicUrl;

    private LocalDateTime registerdate = LocalDateTime.now();

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;



    private Set<String> roles = new HashSet<String>();

    public User(){
    }

    public User(String email, String firstName, String lastName,String university, String college, String branch, String rollno, String section, String year, String sem, String hostel, Date dob, String mobilenumber, String userrole,String[] classes,LocalDateTime registerdate) {
        this.email = email;
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
        this.userrole = userrole;
        this.registerdate = registerdate;
        this.classes = classes;
    }

    public String[] getClasses() {
        return classes;
    }

    public void setClasses(String[] classes) {
        this.classes = classes;
    }

    public String getUserrole() {
        return userrole;
    }

    public void setUserrole(String userrole) {
        this.userrole = userrole;
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


    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public void addRole(String role) {
        roles.add(role);
    }

    public Set<String> getRoles() {
        return roles;
    }

    public String getUsername() {
        return email;
    }

    public void setUsername(String email) {
        this.email = email;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public String getGooglepicUrl() {
        return googlepicUrl;
    }

    public void setGooglepicUrl(String googlepicUrl) {
        this.googlepicUrl = googlepicUrl;
    }

    public String getNormalpicUrl() {
        return normalpicUrl;
    }

    public void setNormalpicUrl(String normalpicUrl) {
        this.normalpicUrl = normalpicUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (accountNonExpired != user.accountNonExpired) return false;
        if (accountNonLocked != user.accountNonLocked) return false;
        if (credentialsNonExpired != user.credentialsNonExpired) return false;
        if (enabled != user.enabled) return false;
        if (!email.equals(user.email)) return false;
        if (!firstName.equals(user.firstName)) return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
        if (!university.equals(user.university)) return false;
        if (!college.equals(user.college)) return false;
        if (!branch.equals(user.branch)) return false;
        if (rollno != null ? !rollno.equals(user.rollno) : user.rollno != null) return false;
        if (section != null ? !section.equals(user.section) : user.section != null) return false;
        if (year != null ? !year.equals(user.year) : user.year != null) return false;
        if (sem != null ? !sem.equals(user.sem) : user.sem != null) return false;
        if (hostel != null ? !hostel.equals(user.hostel) : user.hostel != null) return false;
        if (dob != null ? !dob.equals(user.dob) : user.dob != null) return false;
        if (mobilenumber != null ? !mobilenumber.equals(user.mobilenumber) : user.mobilenumber != null) return false;
        if (!userrole.equals(user.userrole)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(classes, user.classes)) return false;
        if (!googlepicUrl.equals(user.googlepicUrl)) return false;
        if (normalpicUrl != null ? !normalpicUrl.equals(user.normalpicUrl) : user.normalpicUrl != null) return false;
        if (!registerdate.equals(user.registerdate)) return false;
        return roles != null ? roles.equals(user.roles) : user.roles == null;

    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + university.hashCode();
        result = 31 * result + college.hashCode();
        result = 31 * result + branch.hashCode();
        result = 31 * result + (rollno != null ? rollno.hashCode() : 0);
        result = 31 * result + (section != null ? section.hashCode() : 0);
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (sem != null ? sem.hashCode() : 0);
        result = 31 * result + (hostel != null ? hostel.hashCode() : 0);
        result = 31 * result + (dob != null ? dob.hashCode() : 0);
        result = 31 * result + (mobilenumber != null ? mobilenumber.hashCode() : 0);
        result = 31 * result + userrole.hashCode();
        result = 31 * result + Arrays.hashCode(classes);
        result = 31 * result + googlepicUrl.hashCode();
        result = 31 * result + (normalpicUrl != null ? normalpicUrl.hashCode() : 0);
        result = 31 * result + registerdate.hashCode();
        result = 31 * result + (accountNonExpired ? 1 : 0);
        result = 31 * result + (accountNonLocked ? 1 : 0);
        result = 31 * result + (credentialsNonExpired ? 1 : 0);
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        return result;
    }
}

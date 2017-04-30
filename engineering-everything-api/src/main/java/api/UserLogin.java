package api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by GnyaniMac on 30/04/17.
 */
public class UserLogin {
    @JsonProperty
    private String email;
    @JsonProperty
    private String password;

    public UserLogin()
    {

    }

    public UserLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }


    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserLogin userLogin = (UserLogin) o;

        if (email != null ? !email.equals(userLogin.email) : userLogin.email != null) return false;
        return password != null ? password.equals(userLogin.password) : userLogin.password == null;

    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}

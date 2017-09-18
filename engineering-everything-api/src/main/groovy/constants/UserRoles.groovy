package constants

/**
 * Created by GnyaniMac on 16/09/17.
 */
public enum UserRoles {

    STUDENT("student"),
    TEACHER("teacher")

    private String value

    UserRoles(String value) {
        this.value = value
    }

    String getValue() {
        return value
    }
}

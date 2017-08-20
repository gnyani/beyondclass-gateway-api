package api;

/**
 * Created by GnyaniMac on 18/08/17.
 */
public class NotificationsReadStatus {

    private String email;

    private  boolean read ;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificationsReadStatus that = (NotificationsReadStatus) o;

        if (read != that.read) return false;
        return email.equals(that.email);

    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + (read ? 1 : 0);
        return result;
    }
}

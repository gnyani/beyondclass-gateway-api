package api;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Created by GnyaniMac on 17/08/17.
 */
@Document (collection = "notifications")
public class Notifications {

    @Id
    private String notificationId;

    private  String content;

    private String picurl;

    private String notificationType;

    @Indexed
    private Date createdAt = new Date();

    @Indexed
    private List<NotificationsReadStatus> users;

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public List<NotificationsReadStatus> getUsers() {
        return users;
    }

    public void setUsers(List<NotificationsReadStatus> users) {
        this.users = users;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notifications that = (Notifications) o;

        if (!notificationId.equals(that.notificationId)) return false;
        if (!content.equals(that.content)) return false;
        if (picurl != null ? !picurl.equals(that.picurl) : that.picurl != null) return false;
        if (!notificationType.equals(that.notificationType)) return false;
        if (!createdAt.equals(that.createdAt)) return false;
        return users != null ? users.equals(that.users) : that.users == null;

    }

    @Override
    public int hashCode() {
        int result = notificationId.hashCode();
        result = 31 * result + content.hashCode();
        result = 31 * result + (picurl != null ? picurl.hashCode() : 0);
        result = 31 * result + notificationType.hashCode();
        result = 31 * result + createdAt.hashCode();
        result = 31 * result + (users != null ? users.hashCode() : 0);
        return result;
    }
}

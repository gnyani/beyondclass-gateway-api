package api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by GnyaniMac on 14/05/17.
 */
public class Comment {

    @JsonProperty
    private String comment;

    private User user;

    public Comment(){}

    public Comment(String comment,User user) {
        this.comment = comment;
        this.user = user;

    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

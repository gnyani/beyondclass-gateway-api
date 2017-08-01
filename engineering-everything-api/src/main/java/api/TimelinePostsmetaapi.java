package api;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by GnyaniMac on 09/05/17.
 */
@Document(collection = "timeline-files-meta")
public class TimelinePostsmetaapi {
    //we can set expiration rules if we want.
   // @Indexed(unique = true)
    @Id
    private String filename;
    private String description;
    private String postUrl;
    private String likeUrl;
    private String commentUrl;
    private int likes;
    private ArrayList<Comment> comments;
    private String owner;
    private LocalDateTime postdateTime;
    private ArrayList<User> likedUsers = new ArrayList<>();
    private String propicUrl;
    private User uploadeduser;
    private boolean isprofilepicchange;
    public TimelinePostsmetaapi(){

    }


    public TimelinePostsmetaapi(String filename,String description, String postUrl,String likeUrl,String commentUrl, int likes, ArrayList<Comment> comments, String owner, LocalDateTime postdateTime,ArrayList<User> likedUsers,String propicUrl,User user) {
        this.filename = filename;
        this.description = description;
        this.postUrl = postUrl;
        this.likeUrl = likeUrl;
        this.commentUrl = commentUrl;
        this.likes = likes;
        this.comments = comments;
        this.owner = owner;
        this.postdateTime = postdateTime;
        this.likedUsers = likedUsers;
        this.propicUrl = propicUrl;
        this.uploadeduser = user;
    }

    public boolean isprofilepicchange() {
        return isprofilepicchange;
    }

    public void setIsprofilepicchange(boolean isprofilepicchange) {
        this.isprofilepicchange = isprofilepicchange;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public LocalDateTime getPostdateTime() {
        return postdateTime;
    }

    public void setPostdateTime(LocalDateTime postdateTime) {
        this.postdateTime = postdateTime;
    }

    public ArrayList<User> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(ArrayList<User> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLikeUrl() {
        return likeUrl;
    }

    public void setLikeUrl(String likeUrl) {
        this.likeUrl = likeUrl;
    }

    public String getCommentUrl() {
        return commentUrl;
    }

    public void setCommentUrl(String commentUrl) {
        this.commentUrl = commentUrl;
    }

    public String getPropicUrl() {
        return propicUrl;
    }

    public void setPropicUrl(String propicUrl) {
        this.propicUrl = propicUrl;
    }

    public User getUploadeduser() {
        return uploadeduser;
    }

    public void setUploadeduser(User uploadeduser) {
        this.uploadeduser = uploadeduser;
    }

    @Override
    public String toString() {
        return "TimelinePostsmetaapi{" +
                "filename='" + filename + '\'' +
                ", description='" + description + '\'' +
                ", postUrl='" + postUrl + '\'' +
                ", likeUrl='" + likeUrl + '\'' +
                ", commentUrl='" + commentUrl + '\'' +
                ", likes=" + likes +
                ", comments=" + comments +
                ", owner='" + owner + '\'' +
                ", postdateTime=" + postdateTime +
                ", likedUsers=" + likedUsers +
                '}';
    }
}

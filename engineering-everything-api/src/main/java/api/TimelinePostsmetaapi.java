package api;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Created by GnyaniMac on 09/05/17.
 */
@Document(collection = "timeline-files-meta")
public class TimelinePostsmetaapi {
    //we can set expiration rules if we want.
    @Indexed(unique = true)
    private String filename;
    private String postUrl;
    private int likes;
    private String[] comments;
    private String owner;
    private LocalDateTime postdateTime;

    public TimelinePostsmetaapi(){

    }

    public TimelinePostsmetaapi(String filename, String postUrl, int likes, String[] comments, String owner, LocalDateTime postdateTime) {
        this.filename = filename;
        this.postUrl = postUrl;
        this.likes = likes;
        this.comments = comments;
        this.owner = owner;
        this.postdateTime = postdateTime;
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

    public String[] getComments() {
        return comments;
    }

    public void setComments(String[] comments) {
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

    @Override
    public String toString() {
        return "TimelinePostsmetaapi{" +
                "filename='" + filename + '\'' +
                ", postUrl='" + postUrl + '\'' +
                ", likes=" + likes +
                ", comments=" + Arrays.toString(comments) +
                ", owner='" + owner + '\'' +
                ", postdateTime=" + postdateTime +
                '}';
    }
}

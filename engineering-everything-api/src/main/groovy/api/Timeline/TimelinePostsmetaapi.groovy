package api.Timeline

import api.user.User
import api.user.UserDetails
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor;
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

import java.time.LocalDate;

/**
 * Created by GnyaniMac on 09/05/17.
 */
@EqualsAndHashCode
@TupleConstructor
@ToString
@Document(collection = "timeline-files-meta")
public class TimelinePostsmetaapi {


     @Id
     String filename;
     String description;
     String postUrl;
     String likeUrl;
     String commentUrl;
     int likes;
     ArrayList<Comment> comments;
     String owner;

     @Indexed(expireAfterSeconds = 5616000)        // 4 years = 1460 days
     LocalDate postdateTime;

     ArrayList<UserDetails> likedUsers = new ArrayList<>();
     String propicUrl;
     UserDetails uploadeduser;
     boolean isprofilepicchange;
}

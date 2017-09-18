package api.Timeline

import api.user.User
import api.user.UserDetails;
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor;

/**
 * Created by GnyaniMac on 14/05/17.
 */
@EqualsAndHashCode
@TupleConstructor
@ToString
public class Comment {

    @JsonProperty
     String comment;

     UserDetails user;
}

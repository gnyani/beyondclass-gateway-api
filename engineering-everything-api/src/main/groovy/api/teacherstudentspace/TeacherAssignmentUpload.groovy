package api.teacherstudentspace

import api.user.User
import api.user.UserDetails;
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Created by GnyaniMac on 15/08/17.
 */

@EqualsAndHashCode
@ToString
@TupleConstructor
public class TeacherAssignmentUpload {

     @JsonProperty
     String batch;

     @JsonProperty
     String subject;

     @JsonProperty
     byte[] file;
}

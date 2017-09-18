package api.questionpapers;

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * Created by GnyaniMac on 02/05/17.
 */

@EqualsAndHashCode
@TupleConstructor
public class Questionpaper {

    @JsonProperty
    @NotEmpty
    @NotNull
    String university;
    @JsonProperty
    @NotEmpty
    @NotNull
    String college;
    @JsonProperty
    @NotEmpty
    @NotNull
    String branch;
    @JsonProperty
    @NotEmpty
    @NotNull
    String subject;
    @JsonProperty
    @NotEmpty
    @NotNull
    String qpyear;
}

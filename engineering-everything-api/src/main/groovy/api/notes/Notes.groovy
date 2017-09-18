package api.notes;

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;


/**
 * Created by GnyaniMac on 02/05/17.
 */

@EqualsAndHashCode
@TupleConstructor
@ToString
public class Notes {
    @JsonProperty
    @NotEmpty
    @NotNull
    String subject;
    @JsonProperty
    @NotEmpty
    @NotNull
    byte[] file;
}

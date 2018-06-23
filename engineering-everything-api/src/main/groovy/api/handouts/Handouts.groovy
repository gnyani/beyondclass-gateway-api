package api.handouts

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import org.hibernate.validator.constraints.NotEmpty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import javax.validation.constraints.NotNull



@EqualsAndHashCode
@TupleConstructor
@ToString
@Document(collection = "handouts")
class Handouts {

    @Id
    String filename

    @JsonProperty
    @NotEmpty
    @NotNull
    String subject

    @JsonProperty
    @NotNull
    @NotEmpty
    String comment

    @JsonProperty
    @NotEmpty
    @NotNull
    String batch

    @JsonProperty
    @NotNull
    @NotEmpty
    String branch


    @JsonProperty
    @NotEmpty
    @NotNull
    byte[] file
}

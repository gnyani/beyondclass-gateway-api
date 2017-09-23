package api.questionpapers;

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by GnyaniMac on 02/05/17.
 */

@EqualsAndHashCode
@TupleConstructor
public class QuestionPaperSubject {

    //University and college can be used to allow users to query syllabuses of other colleges maybe in future.
    @JsonProperty
    String university

    @JsonProperty
    String college

    @JsonProperty
    @NotNull
    @NotEmpty
    String subject

    @JsonProperty
    @NotNull
    @NotEmpty
    String branch

    @JsonProperty
    @NotNull
    @NotEmpty
    String qpyear
}

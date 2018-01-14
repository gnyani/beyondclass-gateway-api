package api.feedback

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString
import org.hibernate.validator.constraints.NotEmpty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import javax.validation.constraints.NotNull

/**
 * Created by GnyaniMac on 11/01/18.
 */

@ToString
@Document(collection = "feedback")
class ReportIssue {

    @JsonProperty
    @NotNull
    @NotEmpty
    String message

    @JsonProperty
    @NotEmpty
    @NotNull
    @Id
    String email

    @JsonProperty
    byte[] file
}

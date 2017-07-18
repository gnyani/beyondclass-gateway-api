package api;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by GnyaniMac on 15/07/17.
 */
@Document ( collection = "coachingcentres-files")
public class CoachingCentreImages {

    @Id
    public String caochingcentreId;

    public byte[] feedetails;

    public String getCaochingcentreId() {
        return caochingcentreId;
    }

    public void setCaochingcentreId(String caochingcentreId) {
        this.caochingcentreId = caochingcentreId;
    }

    public byte[] getFeedetails() {
        return feedetails;
    }

    public void setFeedetails(byte[] feedetails) {
        this.feedetails = feedetails;
    }
}

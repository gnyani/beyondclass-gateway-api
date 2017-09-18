package api.coachingcentres;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by GnyaniMac on 15/07/17.
 */
@Document ( collection = "coachingcentres-files")
public class CoachingCentreImages {

    @Id
    String caochingcentreId;

    byte[] feedetails;

}

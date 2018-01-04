package api.Timeline;

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document

import java.time.LocalDate;

/**
 * Created by GnyaniMac on 09/05/17.
 */
@Document(collection = "timeline-files")
public class TimelinePosts {

    @JsonProperty
    private byte[] file ;

    @JsonProperty
    private String description;

    @JsonProperty
    private boolean isprofilepicchange;


    private String filename;

    public TimelinePosts(){

    }

    public TimelinePosts(byte[] file,String description, String filename) {
        this.file = file;
        this.description = description;
        this.filename = filename;
    }

    public boolean isprofilepicchange() {
        return isprofilepicchange;
    }

    public void setIsprofilepicchange(boolean isprofilepicchange) {
        this.isprofilepicchange = isprofilepicchange;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

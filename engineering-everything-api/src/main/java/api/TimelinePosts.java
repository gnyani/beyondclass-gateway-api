package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by GnyaniMac on 09/05/17.
 */
@Document(collection = "timeline-files")
public class TimelinePosts {

    @JsonProperty
    private byte[] file;

    private String filename;

    public TimelinePosts(){

    }

    public TimelinePosts(byte[] file, String filename) {
        this.file = file;
        this.filename = filename;
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
}

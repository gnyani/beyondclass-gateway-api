package api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by GnyaniMac on 24/07/17.
 */
public class FileData {

    @JsonProperty
    private  byte[] file;

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}

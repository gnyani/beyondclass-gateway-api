package api;

import org.springframework.stereotype.Component;

/**
 * Created by GnyaniMac on 02/05/17.
 */
@Component
public class Validation {
    private String result;
    private Boolean valid;

    public  Validation()
    {

    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }
}

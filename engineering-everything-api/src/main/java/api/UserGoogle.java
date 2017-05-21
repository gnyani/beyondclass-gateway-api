package api;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedHashMap;

@Document(collection = "userGoogle")
public class UserGoogle {
	private String name;
	@Id
	private String mailId;
	private String pic;
	private Location location;

    public UserGoogle(LinkedHashMap m) {
        this.name=(String)m.get("name");
        this.mailId=(String)m.get("email");
        this.pic=(String)m.get("picture");
        this.location = (Location)m.get("location");
    }

    public UserGoogle() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public String getPic() {

        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public UserGoogle(String name, String mailId, String pic) {
        this.name = name;
        this.mailId = mailId;
        this.pic = pic;
    }

    @Override
    public String toString() {
        return "UserGoogle{" +
                "name='" + name + '\'' +
                ", mailId='" + mailId + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }
}

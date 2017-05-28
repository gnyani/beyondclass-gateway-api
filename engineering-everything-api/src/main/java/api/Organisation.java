package api;

import com.fasterxml.jackson.annotation.JsonProperty;
import constants.OrganisationType;

import java.util.List;

public class Organisation {

	@JsonProperty
	public OrganisationType typeOfOrganisation;
	@JsonProperty
	public Location location;

	public Review review;

	public List<String> photos;
	
	public OrganisationType getTypeOfOrganisation() {
		return typeOfOrganisation;
	}
	public void setTypeOfOrganisation(OrganisationType typeOfOrganisation) {
		this.typeOfOrganisation = typeOfOrganisation;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Review getReview() {
		return review;
	}
	public void setReview(Review review) {
		this.review = review;
	}
	public List<String> getPhotos() {
		return photos;
	}
	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}
}

package api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by manoj.jangam on 5/18/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
    String lon;
    String lat;
    String city;
    String country;
    String zip;
    String regionName;

    public Location() {
    }

    public Location(String lon, String lat, String city, String country, String zip, String regionName) {
        this.lon = lon;
        this.lat = lat;
        this.city = city;
        this.country = country;
        this.zip = zip;
        this.regionName = regionName;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getLon() {

        return lon;
    }
}

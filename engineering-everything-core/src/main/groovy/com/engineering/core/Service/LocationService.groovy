//package com.engineering.core.Service;
//
//import api.Location;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.stereotype.Service;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.URL;
//import java.net.URLConnection;
//
///**
// * Created by manoj on 20/5/17.
// */
//@Service
//public class LocationService {
//
//    public Location getCurrentLocation() throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//		URL url = new URL("http://ip-api.com/json");
//		URLConnection uc = url.openConnection();
//		BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
//		String line = reader.readLine();
//		JsonNode array = mapper.readValue(line, JsonNode.class);
//		Location location = mapper.readValue(array.traverse(), Location.class);
//		return location;
//    }
//
//    public Double findDistance(Location first,Location second){
//	 	Double lat1=Double.parseDouble(first.getLat());
//	 	Double lat2 = Double.parseDouble(second.getLat());
//		Double lon1 = Double.parseDouble(first.getLon());
//		Double lon2 = Double.parseDouble(second.getLon());
//		double theta = lon1 - lon2;
//		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
//		dist = Math.acos(dist);
//		dist = rad2deg(dist);
//		dist = dist * 60 * 1.1515 * 1.609344;
//		return (dist);
//	}
//		private static double deg2rad(double deg) {
//			return (deg * Math.PI / 180.0);
//		}
//		private static double rad2deg(double rad) {
//			return (rad * 180 / Math.PI);
//		}
//}

package com.engineering.Application.Controller;

import api.Organisation;
import com.engineering.core.Service.LocationService;
import com.engineering.core.repositories.UserGoogleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
public class UserRestGoogleController {

	@Autowired
	private UserGoogleRepository user;

	@Autowired
	private LocationService locationUtility;


/*




	@RequestMapping("/user")
	public UserGoogle sayHello(OAuth2Authentication auth) throws IOException {
	LinkedHashMap m = (LinkedHashMap) auth.getUserAuthentication().getDetails();

	ObjectMapper mapper = new ObjectMapper();
	URL url = new URL("http://ip-api.com/json");
	URLConnection uc = url.openConnection();
	BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
	String line = reader.readLine();
	JsonNode array = mapper.readValue(line, JsonNode.class);
	Location location = mapper.readValue(array.traverse(), Location.class);

	m.put("location",location);
	UserGoogle u = new UserGoogle(m);
	user.save(u);

	return u ;
	}



	*/

	@RequestMapping(value = "/storeHangout",produces = "application/json",method = RequestMethod.POST)
	@ResponseBody
	public Organisation post(@RequestBody Organisation org) throws IOException {
		System.out.print("organization" + org);
		return org;
	}
}
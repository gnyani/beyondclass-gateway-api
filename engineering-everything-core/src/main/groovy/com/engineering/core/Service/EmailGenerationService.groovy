package com.engineering.core.Service

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.stereotype.Component

/**
 * Created by GnyaniMac on 01/08/17.
 */
@Component
class EmailGenerationService {

    JsonSlurper jsonSlurper = new JsonSlurper()
    public String parseEmail(Object obj)
    {

        def m = JsonOutput.toJson( obj.getUserAuthentication().getDetails())
        def Json = jsonSlurper.parseText(m);
        String email = Json."email"
        return  email;
    }
}

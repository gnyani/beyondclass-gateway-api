package com.engineering.Application.Controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by GnyaniMac on 30/04/17.
 */
@RestController
class DashBoardRestController {

    @RequestMapping(value="/" ,method = RequestMethod.GET)
    public String greeting()
    {
        return "Hello World User please login before doing anything"
    }
}

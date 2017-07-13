package com.engineering.Application.Controller

import api.Coachingcentre
import com.engineering.core.repositories.CoachingCentresRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by GnyaniMac on 13/07/17.
 */
@RestController
class CachingCentreRestController {

    @Autowired
    CoachingCentresRepository centresRepository

    @RequestMapping(value= "/coachingcentres/insert", method = RequestMethod.POST )
    public String insertCentre ( @RequestBody Coachingcentre coachingcentre )
    {
        def x= centresRepository.insert(coachingcentre)

        return( x ? "successfully inserted" : "sorry something went wrong")
    }



}

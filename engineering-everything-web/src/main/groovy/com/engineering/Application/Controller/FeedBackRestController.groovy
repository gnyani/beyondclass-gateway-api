package com.engineering.Application.Controller

import api.feedback.ReportIssue
import com.engineering.core.repositories.FeedBackRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * Created by GnyaniMac on 11/01/18.
 */
@RestController
class FeedBackRestController {

    @Autowired
    FeedBackRepository feedBackRepository

    @PostMapping(value = "/user/report/issue",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> saveFeedBack(@RequestBody ReportIssue reportIssue){
        ReportIssue response = feedBackRepository.save(reportIssue)

        response ? new ResponseEntity<>("Success",HttpStatus.OK) : new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR)
    }
}

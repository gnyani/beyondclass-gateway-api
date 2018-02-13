package com.engineering.Application.Controller

import api.feedback.ReportIssue
import com.engineering.core.Service.MailService
import com.engineering.core.Service.ServiceUtilities
import com.engineering.core.repositories.FeedBackRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Created by GnyaniMac on 11/01/18.
 */
@RestController
class FeedBackRestController {

    @Autowired
    FeedBackRepository feedBackRepository

    @Value('${email.issue.group}')
    String sendTo

    @Autowired
    MailService mailService

    @Autowired ServiceUtilities serviceUtils

    private static Logger log = LoggerFactory.getLogger(FeedBackRestController.class)


    @PostMapping(value = "/user/report/issue",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> saveFeedBack(@RequestBody ReportIssue reportIssue, OAuth2Authentication oauth){
        ReportIssue response = feedBackRepository.save(reportIssue)

        sendEmail(reportIssue.email)
        log.info("<feedBack>["+serviceUtils.parseEmail(auth)+"](issue reported)")
        response ? new ResponseEntity<>("Success",HttpStatus.OK) : new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @GetMapping(value = "/deamons/feedback/retrieve",produces = "image/*")
    ResponseEntity<?> viewImage (@RequestParam String email, OAuth2Authentication oauth){

        ReportIssue response = feedBackRepository.findByEmail(email)
        log.info("<feedBack>["+serviceUtils.parseEmail(auth)+"](retrieved feedback)")
        new ResponseEntity<>(response.file,HttpStatus.OK)

    }

    void sendEmail(String email){

        String [] list = new String[1]
        list[0] = sendTo
        mailService.sendHtmlMail(list,"You got a new Issue from","<h4>${email}</h4>")

        String[] reporter = new String[1]
        reporter[0] = email

        mailService.sendHtmlMail(reporter,"We have received your feed back","<h1>Thank You</h1>")
    }
}

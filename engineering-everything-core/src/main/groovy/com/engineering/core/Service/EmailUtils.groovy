package com.engineering.core.Service

import com.engineering.core.constants.EmailTypes
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import groovy.text.SimpleTemplateEngine

/**
 * Created by GnyaniMac on 15/01/18.
 */
@Component
class EmailUtils {

    @Value('${engineering.everything.host}')
    private String hostName

    public String createEmailMessage(EmailTypes type, String ... strings){

        String message = "";
        String sender = strings[0]


        if(type == EmailTypes.ANNOUNCEMENT)
        {
            def emailTemplate = getClass().getResource("/New_Announcement_posted.html")
            Map<String,String> config = new HashMap<>()
            config.put("type",EmailTypes.ANNOUNCEMENT.toString())
            config.put("hostname",hostName)
            config.put("sender",sender)
            def engine = new SimpleTemplateEngine()
            def template = engine.createTemplate(emailTemplate).make(config)
            message = template.toString()

        }
        else if(type == EmailTypes.ASSIGNMENT){

            def emailTemplate = getClass().getResource("/New_Assignment_Posted.html")
            Map<String,String> config = new HashMap<>()
            config.put("type",EmailTypes.ANNOUNCEMENT.toString())
            config.put("hostname",hostName)
            config.put("sender",sender)
            def engine = new SimpleTemplateEngine()
            def template = engine.createTemplate(emailTemplate).make(config)
            message = template.toString()

        }
        else if(type == EmailTypes.REMINDER_NOTIFIER)
        {
            String noOfDays = strings[1]
            def emailTemplate = getClass().getResource("/Assignment_Notification.html")
            Map<String,String> config = new HashMap()
            config.put("noOfDays",noOfDays)
            config.put("hostname",hostName)
            config.put("sender",sender)
            def engine = new SimpleTemplateEngine()
            def template = engine.createTemplate(emailTemplate).make(config)
            message = template.toString()

        }
        else if (type == EmailTypes.EVALUATION_DONE)
        {
            def emailTemplate = getClass().getResource("/Assignment_evaluation.html")
            Map<String,String> config = new HashMap<>()
            config.put("hostname",hostName)
            config.put("sender",sender)
            def engine = new SimpleTemplateEngine()
            def template = engine.createTemplate(emailTemplate).make(config)
            message = template.toString()
        }
        return message
    }

    public String createSubject(EmailTypes type){

        String subject = ""
        if(type == EmailTypes.ASSIGNMENT)
            subject = "You got a new Assignment"
        else if(type == EmailTypes.ANNOUNCEMENT)
            subject = "You got a new Announcement"
        else if(type == EmailTypes.EVALUATION_DONE)
            subject = "Your Assignment has been evaluated"
        else if(type == EmailTypes.REMINDER_NOTIFIER)
            subject = "Reminder for your assignment"

        return subject
    }
}

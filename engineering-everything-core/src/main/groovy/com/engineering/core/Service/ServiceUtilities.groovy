package com.engineering.core.Service

import api.user.User
import api.user.UserDetails
import com.engineering.core.constants.EmailTypes
import com.engineering.core.repositories.UserRepository
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import static groovyx.gpars.dataflow.Dataflow.task
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by GnyaniMac on 01/08/17.
 */
@Component
class ServiceUtilities {

    @Autowired
    UserRepository repository;

    @Autowired
    EmailUtils emailUtils

    @Autowired
    MailService mailService


    UserDetails userDetails = new UserDetails()

    JsonSlurper jsonSlurper = new JsonSlurper()

    public String parseEmail(Object obj)
    {

        def m = JsonOutput.toJson( obj.getUserAuthentication().getDetails())
        def Json = jsonSlurper.parseText(m);
        String email = Json."email"
        email
    }


    public User findUserByEmail(String email){
        repository.findByEmail(email)
    }

    public String generateFileName(String ... strings) {
        StringBuilder filename = new StringBuilder()
        strings.each {
            if(strings.last() == it)
                filename.append(it)
             else {
                filename.append(it)
                filename.append('-')
            }
        }
        filename.toString();
    }

    public UserDetails toUserDetails(User user){

        String mobilenumber = user ?. getMobilenumber()

        String classid = user ?. getUniqueclassid()

        userDetails.setEmail(user.getEmail())

        userDetails.setFirstName(user ?. getFirstName())

        userDetails.setLastName(user ?. getLastName())

        userDetails.setMobilenumber(mobilenumber)

        userDetails.setUserrole(user ?. getUserrole())

        userDetails.setUniqueclassId(classid)

        userDetails.setGooglepicUrl(user ?. getGooglepicUrl())

        userDetails.setNormalpicUrl(user ?. getNormalpicUrl())

        userDetails.setCollege(user ?. getCollege())

        userDetails.setUniversity(user ?. getUniversity())

        userDetails.setBranch(user ?. getBranch())

        userDetails
    }

    public String generateUniqueClassIdForTeacher( String batch, String email) {
        def splits = batch.split('-')
        String startyear = splits[0]
        String section = splits[1]
        String endyear = Integer.parseInt(startyear) + 4
        def user = findUserByEmail(email)
        def uniqueClassId = generateFileName(user.university, user.college, user.branch, section, startyear, endyear)
        uniqueClassId
    }

    void findUsersAndSendEmail(String classId, EmailTypes emailTypes, String sender){
        task {
            List<User> users = repository.findByUniqueclassid(classId)
            def toEmails = []
            users.each {
                toEmails.add(it.email)
            }
            String[] emails = new String[toEmails.size()]
            emails = toEmails.toArray(emails)
            String htmlMessage = emailUtils.createEmailMessage(emailTypes, sender)
            String subject = emailUtils.createSubject(emailTypes)

            mailService.sendHtmlMail(emails, subject, htmlMessage)
        }.then{
            println("Sending mail for ${emailTypes} from ${sender} to class ${classId}")
        }
    }
}

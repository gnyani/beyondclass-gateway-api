package com.engineering.core.Service

import api.coachingcentres.Coachingcentre
import api.user.User
import org.springframework.stereotype.Component

import java.time.LocalDate

/**
 * Created by GnyaniMac on 02/05/17.
 */

@Component
class FilenameGenerator {

     public FilenameGenerator() {

     }

    public String generateQuestionIdToSave(User user, Long dateTime){
        String questionPaperId = user.getUniversity()+"_"+
                user.getBranch()+"_"+user.getYear()+"_"+user.getSem()+"_"+dateTime;
        return questionPaperId;
    }

    public String generateQuestionIdToFetch(User user){
        String questionPaperId = user.getUniversity()+"_"+
                user.getBranch()+"_"+user.getYear()+"_"+user.getSem();
        return questionPaperId;
    }
}

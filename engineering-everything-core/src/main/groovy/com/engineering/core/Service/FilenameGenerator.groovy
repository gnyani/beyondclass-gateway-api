package com.engineering.core.Service

import api.Coachingcentre
import api.User
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


    public String generateQpName(String university, String collegecode, String branch, String year, String sem, String subject,String qpyear)
    {
        university = university.toUpperCase();
        collegecode = collegecode.toUpperCase();
        branch = branch.toUpperCase();
        subject = subject.toUpperCase();
        def filename =null;
        filename = university+"-"+collegecode+"-"+branch+"-"+year+"-"+sem+"-"+subject+"-"+qpyear;
        return  filename;
    }
    public String generateSyllabusName(String university, String collegecode, String branch, String year, String sem, String subject)
    {
        university = university.toUpperCase();
        collegecode = collegecode.toUpperCase();
        branch = branch.toUpperCase();
        subject = subject.toUpperCase();
        def filename =null;
        filename = university+"-"+collegecode+"-"+branch+"-"+year+"-"+sem+"-"+subject;
        return  filename;
    }

    public String generateAssignmentName(String university, String collegecode, String branch, String section, String year, String sem, String subject,String email)
    {
        university = university.toUpperCase();
        collegecode = collegecode.toUpperCase();
        branch = branch.toUpperCase();
        section = section.toUpperCase();
        subject = subject.toUpperCase();
        def filename =null;
        filename = university+"-"+collegecode+"-"+branch+"-"+section+"-"+year+"-"+sem+"-"+subject+"-"+email;
        return  filename;
    }

    public String generateAssignmentNamewithoutEmail(String university, String collegecode, String branch, String section, String year, String sem, String subject)
    {
        university = university.toUpperCase();
        collegecode = collegecode.toUpperCase();
        branch = branch.toUpperCase();
        section = section.toUpperCase();
        subject = subject.toUpperCase();
        def filename =null;
        filename = university+"-"+collegecode+"-"+branch+"-"+section+"-"+year+"-"+sem+"-"+subject;
        return  filename;
    }

    public String generatePostname(String university, String collegecode, String branch, String section, String year, String sem,LocalDate date, String email,long currenttime)
    {
        university = university.toUpperCase();
        collegecode = collegecode.toUpperCase();
        branch = branch.toUpperCase();
        section = section.toUpperCase();
        def filename =null;
        filename = university+"-"+collegecode+"-"+branch+"-"+section+"-"+year+"-"+sem+"-"+date.toString()+"-"+email+"-"+currenttime;
        return  filename;
    }

    public String generatePostnamewithouttime(String university, String collegecode, String branch, String section, String year, String sem)
    {
        university = university.toUpperCase();
        collegecode = collegecode.toUpperCase();
        branch = branch.toUpperCase();
        section = section.toUpperCase();
        def filename =null;
        filename = university+"-"+collegecode+"-"+branch+"-"+section+"-"+year+"-"+sem;
        return  filename;
    }

    public String generateClassId(String university,String collegecode,String branch,String section,String year, String sem)
    {
        university = university?.toUpperCase();
        collegecode = collegecode?.toUpperCase();
        branch = branch?.toUpperCase();
        section = section?.toUpperCase();
        def filename =null;
        filename = university+"-"+collegecode+"-"+branch+"-"+section+"-"+year+"-"+sem;
        return  filename;
    }

    public String generateCoachingCentreId(Coachingcentre coachingcentre )
    {
        def filename = coachingcentre.getType().toString() +"-"+coachingcentre.getCity().toString()+"-"+ coachingcentre.getArea().toString()+"-" + coachingcentre.getOrgname()

        return  filename
    }

    public String generateCoachingCentreIdwithoutname(Coachingcentre coachingcentre )
    {
        def filename = coachingcentre.getType().toString() +"-"+coachingcentre.getCity().toString()+"-"+ coachingcentre.getArea().toString()

        return  filename
    }

    public String generateTeacherAnnouncementId(String univ,String college,String branch,String teacherclass,String email){
        return (univ+'-'+college+'-'+branch+'-'+teacherclass+'-'+email+'-'+System.currentTimeMillis())
    }

    public String generateTeacherAnnouncementIdformatch (String univ,String college,String branch,String teacherclass){
        return(univ+'-'+college+'-'+branch+'-'+teacherclass)
    }
    public String genericGenerator(String ... strings) {
      def x ;
        for (String s : strings) {
           if(x == null) {
               x = s
           }else{
               x += '-'
               x += s
           }
        }
        return x;
    }
}

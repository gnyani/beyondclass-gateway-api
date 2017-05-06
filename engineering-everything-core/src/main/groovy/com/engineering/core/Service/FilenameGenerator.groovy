package com.engineering.core.Service

import org.springframework.stereotype.Component


/**
 * Created by GnyaniMac on 02/05/17.
 */

@Component
class FilenameGenerator {

     public FilenameGenerator() {

     }

    public String generateQpName(String university, String collegecode, String branch, String section, String year, String sem, String subject,String qpyear)
    {
        university = university.toUpperCase();
        collegecode = collegecode.toUpperCase();
        branch = branch.toUpperCase();
        section = section.toUpperCase();
        subject = subject.toUpperCase();
        def filename =null;
        filename = university+"-"+collegecode+"-"+branch+"-"+section+"-"+year+"-"+sem+"-"+subject+"-"+qpyear;
        return  filename;
    }
    public String generateSyllabusName(String university, String collegecode, String branch, String section, String year, String sem, String subject)
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
}

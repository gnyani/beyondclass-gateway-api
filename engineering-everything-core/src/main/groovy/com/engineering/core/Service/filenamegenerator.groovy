package com.engineering.core.Service

import org.springframework.stereotype.Component


/**
 * Created by GnyaniMac on 02/05/17.
 */

@Component
class filenamegenerator {

    filenamegenerator() {
    }

    public String generateName(String university, String collegecode, String branch,String section, String year, String sem, String subject)
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
}

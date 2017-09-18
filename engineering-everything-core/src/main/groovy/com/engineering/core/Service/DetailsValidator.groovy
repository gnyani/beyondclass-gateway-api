//package com.engineering.core.Service
//
//import api.Validation
//import constants.year
//import constants.BranchNames
//import constants.Colleges
//import constants.Sections
//import constants.Semester
//import constants.Universities
//import org.springframework.context.annotation.Bean
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
//import org.springframework.stereotype.Component
//import com.engineering.core.repositories.UserRepository
//
///**
// * Created by GnyaniMac on 02/05/17.
// */
//
//@Component
//class DetailsValidator {
//
//    Validation validation = new Validation();
//
//    public DetailsValidator(){
//
//    }
//
//    public Validation refineBranch(String branch)
//    {
//        Boolean flag = false
//        for (BranchNames type : BranchNames.values()) {
//            if ((type.name().equalsIgnoreCase(branch))) {
//                flag=true
//            }
//        }
//        if (!flag){
//            validation.setValid(false);
//            validation.setResult(branch);
//        }else{
//            validation.setValid(true);
//            validation.setResult(branch.toUpperCase())
//        }
//        return validation;
//    }
//
//    public  Validation refineUniv(String university)
//    {
//        Boolean flag = false
//        for (Universities type : Universities.values()) {
//            if ((type.name().equalsIgnoreCase(university))) {
//                flag=true
//            }
//        }
//        if (!flag){
//            validation.setValid(false);
//            validation.setResult(university);
//        }else{
//            validation.setValid(true);
//            validation.setResult(university.toUpperCase())
//        }
//
//        return  validation;
//    }
//
//    public Validation refineCollege(String college)
//    {
//        Boolean flag = false
//        for (Colleges type : Colleges.values()) {
//            if ((type.name().equalsIgnoreCase(college))) {
//                flag=true
//            }
//        }
//        if (!flag){
//            validation.setValid(false);
//            validation.setResult(college);
//        }else{
//            validation.setValid(true);
//            validation.setResult(college.toUpperCase())
//        }
//
//        return validation
//    }
//    public Validation refineYear(String Year){
//        if (Year.isInteger()) {
//            int value = Year as Integer
//            if(value in 1..year.values().length){
//                validation.setValid(true)
//                validation.setResult(value.toString())
//            }else{
//                validation.setValid(false)
//            }
//        }else{
//            validation.setValid(false);
//        }
//        return validation
//    }
//    public Validation refineSemester(String sem){
//        if (sem.isInteger()) {
//            int value = sem as Integer
//            if(value in 1..Semester.values().length){
//                validation.setValid(true)
//                validation.setResult(value.toString())
//            }else{
//                validation.setValid(false)
//            }
//        }else{
//            validation.setValid(false);
//        }
//        return validation
//    }
//    public Validation refineSection(String section)
//    {
//        Boolean flag = false
//        for (Sections type : Sections.values()) {
//            if ((type.name().equalsIgnoreCase(section))) {
//                flag=true
//            }
//        }
//        if (!flag){
//            validation.setValid(false);
//        }else{
//            validation.setValid(true);
//            validation.setResult(section)
//        }
//        switch (section){
//            case "1" : validation.setResult("A");
//                       validation.setValid(true);
//                       break;
//            case "2" : validation.setResult("B");
//                       validation.setValid(true);
//                       break;
//            case "3" : validation.setResult("C");
//                       validation.setValid(true);
//                       break;
//            case "4" : validation.setResult("D");
//                       validation.setValid(true);
//                       break;
//            case "5" : validation.setResult("E");
//                       validation.setValid(true);
//                       break;
//            case "6" : validation.setResult("F");
//                       validation.setValid(true);
//                       break;
//            default  : break;
//        }
//        return validation
//
//    }
//
//    public Validation refineEmail(String email)
//    {
//        def trimmedemail = email.trim();
//        String [] parts = trimmedemail.split("@");
//        String part1 = parts[0].replace(".","");
//        String part2 = parts[1];
//        validation.setResult(part1+"@"+part2);
//        //we can implement some regex match here and return invalid in future if required.
//        validation.setValid(true);
//
//        return validation
//    }
//}

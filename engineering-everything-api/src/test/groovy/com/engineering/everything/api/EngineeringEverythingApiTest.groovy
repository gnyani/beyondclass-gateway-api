package com.engineering.everything.api

import api.Subject
import api.UserLogin
import spock.lang.Specification

/**
 * Created by GnyaniMac on 13/05/17.
 */
class EngineeringEverythingApiTest extends Specification {

    def "test subject equals with unequal instances"(){
        given :
        def a = new Subject("DS")
        def b = new Subject("DM")
        expect :
        a != b
    }
    def "test UserLogin equals with unequal instaces"(){
        given:
        def a = new UserLogin("gnyani007@gmail.com","destro123")
        def b = new UserLogin("manojkumarjanagam@gmail.com","destro123")
        expect:
        a.getEmail() != b.getEmail()
    }

}

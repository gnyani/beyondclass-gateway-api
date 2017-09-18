package com.engineering.everything.api

import api.Subject
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
}

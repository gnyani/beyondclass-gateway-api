package com.engineering.Application


import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


/**
 * Created by GnyaniMac on 27/04/17.
 */

@ComponentScan(basePackages = ['com.engineering.Application','com.engineering.core.Service'],basePackageClasses =[])
@SpringBootApplication
@EnableMongoRepositories
@EnableZuulProxy
@EnableOAuth2Sso
public class EngineeringEverythingApplication{

    public static void main(String[] args) {
        SpringApplication.run(EngineeringEverythingApplication.class, args);
    }

}
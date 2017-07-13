package com.engineering.Application


import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


/**
 * Created by GnyaniMac on 27/04/17.
 */

@ComponentScan(basePackages = ['com.engineering.Application','com.engineering.core.Service'],basePackageClasses =[])
@SpringBootApplication
@EnableMongoRepositories
@EnableOAuth2Sso
public class EngineeringEverythingApplication{

    public static void main(String[] args) {
        SpringApplication.run(EngineeringEverythingApplication.class, args);
    }

   /* @Override
    public void run(String... args) throws Exception {

        repository.deleteAll();

        // save a couple of customers
        repository.save(new User("Alice", "Smith"));
        repository.save(new User("Bob", "Smith"));

        // fetch all customers
        System.out.println("Customers found with findAll():");
        System.out.println("-------------------------------");
        for (User customer : repository.findAll()) {
            System.out.println(customer);
        }
        System.out.println();

        // fetch an individual customer
        System.out.println("Customer found with findByFirstName('Alice'):");
        System.out.println("--------------------------------");
        System.out.println(repository.findByFirstName("Alice"));

        System.out.println("Customers found with findByLastName('Smith'):");
        System.out.println("--------------------------------");
        for (User customer : repository.findByLastName("Smith")) {
            System.out.println(customer);
        }

    }*/

}
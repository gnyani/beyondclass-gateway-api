package com.engineering.everything.core.repositories

import com.mongodb.MongoClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoDbFactory
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

/**
 * Created by GnyaniMac on 14/05/17.
 */
    @Configuration
    @EnableMongoRepositories
    @EnableConfigurationProperties(MongoProperties.class)
    public class MongoConfiguration {
        @Autowired
        private MongoProperties props;



        @Bean
        public MongoDbFactory mongoDbFactory() throws Exception{

            String host = props.getHost();
            int port = props.getPort();
            String db   = props.getDatabase();

            return new SimpleMongoDbFactory(new MongoClient(host, port), db);
        }
        public @Bean(name = "mongoTemplate") MongoTemplate mongoTemplate() throws Exception {
            return new MongoTemplate(mongoDbFactory());
        }
    }


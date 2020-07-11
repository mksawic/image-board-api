package com.mksawic.imageboardapi.config;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class DatabaseConfig {

    public @Bean MongoClient mongoClient() {
        //FOR DEVELOPING
        //return MongoClients.create("***URL FOR DEVELOPING***");

        //FOR PRODUCTION
        return MongoClients.create(System.getenv("MONGODB_URI"));
    }

    public @Bean MongoTemplate mongoTemplate() {
        //ONLY FOR DEVELOPING
        //return new MongoTemplate(mongoClient(), "***DATABASE NAME***");
        
        //FOR PRODUCTION
        return new MongoTemplate(mongoClient(), System.getenv("DB_NAME"));
    }
}
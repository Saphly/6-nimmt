package com.nimmt.server;

import com.nimmt.server.model.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@Configuration
public class TestMongoConfig {

    private static final String collectionName = "sessions";

    @Bean
    public static MongoTemplate mongoTemplate() {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory("mongodb://127.0.0.1:27017/sixNimmtTest"));
    }

    public static void clearCollection() {
        System.out.println("Deleting existing sessions");
        mongoTemplate().remove(new Query(), collectionName);
    }

    public static void repopulateCollection(List<Session> sessions) {
        System.out.println("Creating sessions");
        System.out.println("Inserting sessions");
        mongoTemplate().insert(sessions, collectionName);
    }
}

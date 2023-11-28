package com.nimmt.server.repository;

import com.nimmt.server.model.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends MongoRepository <Session, String>{
}

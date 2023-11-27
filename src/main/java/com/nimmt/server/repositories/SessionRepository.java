package com.nimmt.server.repositories;

import com.nimmt.server.models.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends MongoRepository <Session, String>{
}

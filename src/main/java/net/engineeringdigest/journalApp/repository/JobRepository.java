package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.Job;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends MongoRepository<Job, ObjectId> {
    Optional<Job> findById(ObjectId id); // Use ObjectId as the parameter type
}

package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.Job;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends MongoRepository<Job, ObjectId> {
    Optional<Job> findById(ObjectId id); // Use ObjectId as the parameter type

    List<Job> findByPostedBy(@NotNull(message = "Please provide the user who posted the job.") ObjectId postedBy);
}

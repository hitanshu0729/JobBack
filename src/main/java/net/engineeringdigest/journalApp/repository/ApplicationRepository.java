package net.engineeringdigest.journalApp.repository;
import net.engineeringdigest.journalApp.entity.Application;
import net.engineeringdigest.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, ObjectId> {

    Optional<Application> findById(ObjectId id);

    List<Application> findByApplicant(User applicant);

    List<Application> findByEmployer(User employer);
}

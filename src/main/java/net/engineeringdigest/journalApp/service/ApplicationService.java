package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.Application;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.ApplicationRepository;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.apache.coyote.Response;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Configuration
public class ApplicationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    public List<Application> getAllApplications (){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // This will return the email if the subject is set to email
        User user = userRepository.findByEmail(email);
        // Find applications where the current user is either the applicant or employer
        List<Application> applications = applicationRepository.findByApplicant(user);
        List<Application> employerApplications = applicationRepository.findByEmployer(user);
        // Combine both lists
        applications.addAll(employerApplications);
        return applications;
    }

    public ResponseEntity<?> deleteApplication(ObjectId id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // This will return the email if the subject is set to email
        User user = userRepository.findByEmail(email);
        Application application = applicationRepository.findById(id).orElse(null);
        if(application == null){
            return ResponseEntity.badRequest().body("Application not found");
        }
        if(application.getApplicant().equals(user) || application.getEmployer().equals(user)){
            applicationRepository.deleteById(id);
            return ResponseEntity.ok("Application deleted successfully");
        }
        return ResponseEntity.badRequest().body("You are not authorized to delete this application");
    }
}

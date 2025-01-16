package net.engineeringdigest.journalApp.service;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.Application;
import net.engineeringdigest.journalApp.entity.Job;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.ApplicationRepository;
import net.engineeringdigest.journalApp.repository.JobRepository;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.apache.coyote.Response;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
public class ApplicationService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private JobRepository jobRepository;

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

    public ResponseEntity<?> createApplication(MultipartFile resume,  String name, String email, String coverLetter, String phone, String address,ObjectId jobId){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName();  // This will return the email if the subject is set to email
            User user = userRepository.findByEmail(userEmail);
            if(user == null || !user.getRole().contains("Job Seeker")){
                return ResponseEntity.badRequest().body("Unauthorized");
            }
            if (!Arrays.asList("image/png", "image/jpeg", "image/webp", "application/pdf")
                    .contains(resume.getContentType())) {
                return ResponseEntity.badRequest().body("Invalid file type. Please upload a PNG, JPEG, WEBP, or PDF file.");
            }
            if (name == null || name.trim().isEmpty() ||
                    email == null || email.trim().isEmpty() ||
                    coverLetter == null || coverLetter.trim().isEmpty() ||
                    phone == null || phone.trim().isEmpty() ||
                    address == null || address.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Please fill all fields");
            }
            if (!phone.matches("\\d+")) {
                return ResponseEntity.badRequest().body("Invalid phone number format");
            }

            Job job = jobRepository.findById(jobId).orElse(null);
            if(job == null){
                return ResponseEntity.badRequest().body("Job not found");
            }
            Map<String, Object> uploadResult = cloudinary.uploader().upload(resume.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            if (uploadResult == null || uploadResult.isEmpty()) {
                return ResponseEntity.badRequest().body("Error occurred while uploading resume");
            }
            String secureUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");
            if (publicId == null || publicId.isEmpty() || secureUrl == null || secureUrl.isEmpty()) {
                return ResponseEntity.badRequest().body("Error occurred while uploading resume");
            }

            Application application = new Application();
            application.setName(name);
            application.setEmail(email);
            application.setCoverLetter(coverLetter);
            application.setPhone(Long.parseLong(phone));
            application.setAddress(address);
            application.setResume(new Application.Resume(publicId, secureUrl));
            application.setApplicant(user);
            application.setEmployer(userRepository.findById(job.getPostedBy()).orElse(null));
            applicationRepository.save(application);
        } catch (Exception e) {
            log.error("An unexpected error occurred while processing the application: ", e);
            return ResponseEntity.badRequest().body("An unexpected error occurred while processing the application");
        }
        return ResponseEntity.ok("Application submitted successfully");
    }

}

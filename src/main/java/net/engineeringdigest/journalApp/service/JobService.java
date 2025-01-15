package net.engineeringdigest.journalApp.service;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.Job;
import net.engineeringdigest.journalApp.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
public class JobService {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public ResponseEntity<?> createJob(Job job) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();  // This will return the email if the subject is set to email
            User user = userRepository.findByEmail(email);
            if(user==null) {
                throw new IllegalArgumentException("User not found");
            }
            if(!user.getRole().equals("Employer")) {
                throw new IllegalArgumentException("Only Employers can create jobs");
            }
            if ((job.getSalaryFrom() == null || job.getSalaryTo() == null) && (job.getFixedSalary()==0)) {
                throw new IllegalArgumentException("Job must have either salary range or fixed salary");
            }
            if(job.getSalaryFrom()!=null && job.getSalaryTo()!=null && (job.getFixedSalary()!=0)) {
                throw new IllegalArgumentException("Job cannot have both salary range and fixed salary");
            }
            job.setPostedBy(user.getId());
            jobRepository.save(job);
            return ResponseEntity.ok("Job created successfully");
        } catch (Exception e) {
            log.error("Exception occurred while creating job: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

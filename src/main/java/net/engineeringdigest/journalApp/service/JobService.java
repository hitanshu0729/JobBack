package net.engineeringdigest.journalApp.service;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
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

    public List<Job> getMyJobs() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // This will return the email if the subject is set to email
        User user = userRepository.findByEmail(email);
        if(user==null) {
            throw new IllegalArgumentException("User not found");
        }
        if(!user.getRole().equals("Employer")) {
            throw new IllegalArgumentException("Only Employers can create jobs");
        }
        return jobRepository.findByPostedBy((user.getId()));
    }

    public ResponseEntity<?> updateJob(ObjectId jobId, Job jobInp) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();  // This will return the email if the subject is set to email
            User user = userRepository.findByEmail(email);
            if(user==null) {
                throw new IllegalArgumentException("User not found");
            }
            if(!user.getRole().equals("Employer")) {
                throw new IllegalArgumentException("Only Employers can update jobs");
            }
            Job job = jobRepository.findById(jobId).orElse(null);
            if(job==null) {
                throw new IllegalArgumentException("Job not found");
            }
            if(!job.getPostedBy().equals(user.getId())) {
                throw new IllegalArgumentException("You can only update your own jobs");
            }
            jobRepository.save(jobInp);
            return ResponseEntity.ok("Job updated successfully");
        } catch (Exception e) {
            log.error("Exception occurred while updating job: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public boolean deleteJob(ObjectId jobId) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();  // This will return the email if the subject is set to email
            User user = userRepository.findByEmail(email);
            if(user==null) {
                throw new IllegalArgumentException("User not found");
            }
            if(!user.getRole().equals("Employer")) {
                throw new IllegalArgumentException("Only Employers can delete jobs");
            }
            Job job = jobRepository.findById(jobId).orElse(null);
            if(job==null) {
                throw new IllegalArgumentException("Job not found");
            }
            if(!job.getPostedBy().equals(user.getId())) {
                throw new IllegalArgumentException("You can only delete your own jobs");
            }
            System.out.println("Deleting job");
            System.out.println( jobId);
            jobRepository.deleteById(jobId);
            return true;
        } catch (Exception e) {
            log.error("Exception occurred while deleting job: ", e);
            throw new IllegalArgumentException(e.getMessage());

        }
    }

}

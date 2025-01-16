package net.engineeringdigest.journalApp.controller;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.Application;
import net.engineeringdigest.journalApp.service.ApplicationService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/application")
@RestController
@Slf4j
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping("/getall")
    public List<Application> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @DeleteMapping("/delete")
    public void deleteApplication(String id) {
        applicationService.deleteApplication(new ObjectId(id));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createApplication(
            @RequestParam("resume") MultipartFile resume, // File input
            @RequestParam("name") String name,  // Other application details
            @RequestParam("email") String email,
            @RequestParam("coverLetter") String coverLetter,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam("jobId") String jobId
    ){
        if (resume == null || resume.isEmpty()) {
            return ResponseEntity.badRequest().body("Resume File Required!");
        }
        ObjectId id = new ObjectId(jobId.trim().split(",")[0]);
        return applicationService.createApplication(resume, name, email, coverLetter, phone, address, id);
    }

}

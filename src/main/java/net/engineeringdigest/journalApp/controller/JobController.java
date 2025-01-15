package net.engineeringdigest.journalApp.controller;


import net.engineeringdigest.journalApp.entity.Job;
import net.engineeringdigest.journalApp.service.JobService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping("/post")
    ResponseEntity<?> postJob(@RequestBody Job job) {
        return jobService.createJob(job);
    }

    @GetMapping("/getmyjobs")
    public ResponseEntity<List<Job>> getMyJobs() {
        List<Job> jobs = jobService.getMyJobs();
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateJob(@PathVariable String id, @RequestBody Job job) {
        ObjectId objectId = new ObjectId(id);
        return jobService.updateJob(objectId, job);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable String id) {
        ObjectId objectId = new ObjectId(id);
        if (jobService.deleteJob(objectId)) {
            return ResponseEntity.ok("Job deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("Job not found");
        }
    }
}
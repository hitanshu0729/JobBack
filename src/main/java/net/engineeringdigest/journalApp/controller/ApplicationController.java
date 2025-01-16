package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.Application;
import net.engineeringdigest.journalApp.service.ApplicationService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/application")
@RestController
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
}

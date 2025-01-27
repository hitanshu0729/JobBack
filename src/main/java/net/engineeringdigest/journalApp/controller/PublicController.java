package net.engineeringdigest.journalApp.controller;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.Job;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JobService;
import net.engineeringdigest.journalApp.service.UserDetailsServiceImpl;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.utilis.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private JobService jobService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Server chal raha hai bhai chill";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        return userService.saveNewUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        return userService.login(user, "Logged in successfully");
    }

    @GetMapping("/getalljobs")
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobService.getAllJobs();
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }
}
package net.engineeringdigest.journalApp.controller;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.RedisService;
import net.engineeringdigest.journalApp.service.UserDetailsServiceImpl;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.utilis.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
    private RedisService redisService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        String key = "currentDate";
        String currentDate = redisService.get(key, String.class);
        if (currentDate == null) {
            currentDate = java.time.LocalDateTime.now().toString();
            redisService.set(key, currentDate, 3600L); // set expiry time to 1 hour
        }
        return new ResponseEntity<>(currentDate, HttpStatus.OK);
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Server chal raha hai bhai chill";
    }

    @PostMapping("/signup")
    public void signup(@RequestBody User user) {
        userService.saveNewUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok()
                    .header("Set-Cookie", "token=" + jwt + "; HttpOnly; Path=/; Max-Age=3600")
                    .body(jwt);
        }catch (Exception e){
            log.error("Exception occurred while createAuthenticationToken ");
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }
}

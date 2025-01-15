package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.utilis.JwtUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity<?> saveNewUser(User user) {
        try {
            String rawPassword = user.getPassword();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if(user.getEmail()==null || user.getPhone()==null || user.getRole()==null) {
                throw new IllegalArgumentException("User must have email, phone, and role");
            }
            if(userRepository.findByEmail(user.getEmail())!=null) {
                throw new IllegalArgumentException("User with this email already exists");
            }
            if (user.getRole().contains("Job Seeker") || user.getRole().contains("Employer")) {
            userRepository.save(user);
            } else {
                throw new IllegalArgumentException("User must have either 'Job Seeker' or 'Employer' role");
            }
            return login(new User(user.getEmail(), rawPassword, user.getRole()), "User saved successfully , also logged in");
        } catch (Exception e) {
            log.error("Exception occurred while saving new user: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<?> login(User user,String message) {
        Map<String, Object> response = new HashMap<>();
        try{
            if(user.getEmail()==null || user.getPassword()==null || user.getRole()==null) {
                throw new IllegalArgumentException("User must have email and password");
            }
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            if(!message.isEmpty()){
                response.put("message", message);
            }
            return ResponseEntity.ok()
                    .header("Set-Cookie", "token=" + jwt + "; HttpOnly; Path=/; Max-Age=3600")
                    .body(response);
        }catch (Exception e){
            log.error("Exception occurred while createAuthenticationToken ");
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId id) {
        return userRepository.findById(id);
    }

    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }

    public User findByEmail(String userName) {
        return userRepository.findByEmail(userName);
    }
}
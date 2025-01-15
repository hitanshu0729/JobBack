package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public ResponseEntity<Map<String, Object>> saveNewUser(User user) {
        Map<String, Object> response = new HashMap<>();
        try {

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
            response.put("message", "User saved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Exception occurred while saving new user: ", e);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
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
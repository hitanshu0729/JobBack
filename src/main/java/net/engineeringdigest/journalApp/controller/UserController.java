package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

   @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        return userService.logout();
    }


//    @PutMapping
//    public ResponseEntity<?> updateUser(@RequestBody User user) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userName = authentication.getName();
//        User userInDb = userService.findByUserName(userName);
//        userInDb.setUserName(user.getUserName());
//        userInDb.setPassword(user.getPassword());
//        userService.saveNewUser(userInDb);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    @DeleteMapping
//    public ResponseEntity<?> deleteUserById() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        userRepository.deleteByUserName(authentication.getName());
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

}
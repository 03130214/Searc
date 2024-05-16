package com.example.searc.controller;
import com.example.searc.dto.UserLoginDTO;
import com.example.searc.dto.UserRegistrationDTO;
import com.example.searc.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    

    @GetMapping("/login")
    public ResponseEntity<?> showLoginForm() {
        return ResponseEntity.ok("Please login.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDTO userLoginDTO) {
        boolean isValidUser = userService.validateUser(userLoginDTO.getUsername(), userLoginDTO.getPassword());
        
        if (isValidUser) {
            return ResponseEntity.ok("Login successful. Redirecting to home.");
        } else {
            return ResponseEntity.badRequest().body("Invalid username or password.");
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        boolean userExists = userService.userExists(registrationDTO.getUsername());
        if (userExists) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }
        
        boolean emailExists = userService.emailExists(registrationDTO.getEmail());
        if (emailExists) {
            return ResponseEntity.badRequest().body("Error: Email is already registered!");
        }

        userService.createUser(registrationDTO);
        return ResponseEntity.ok("User registered successfully");
    }
}
package com.bookshop.bookstore.controller;

import com.bookshop.bookstore.model.User;
import com.bookshop.bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already registered");
        }
        user.setRole("USER");
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginUser) {
        Optional<User> userOpt = userRepository.findByEmail(loginUser.getEmail());

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(loginUser.getPassword())) {
            return ResponseEntity.ok("Login successful");
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }

    private boolean isAdmin(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.isPresent() && "ADMIN".equalsIgnoreCase(userOpt.get().getRole());
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(@RequestParam String email) {
        if (!isAdmin(email)) {
            return ResponseEntity.status(403).body("Access denied: Admins only");
        }
        return ResponseEntity.ok(userRepository.findAll());
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, @RequestParam String email) {
        if (!isAdmin(email)) {
            return ResponseEntity.status(403).body("Access denied: Admins only");
        }

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    
    @PutMapping("/promote")
    public ResponseEntity<String> promoteToAdmin(@RequestParam String email, @RequestParam String requesterEmail) {
        if (!isAdmin(requesterEmail)) {
            return ResponseEntity.status(403).body("Access denied: Admins only");
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOpt.get();
        user.setRole("ADMIN");
        userRepository.save(user);
        return ResponseEntity.ok("User promoted to admin");
    }

}

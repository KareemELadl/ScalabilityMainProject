package com.example.UserService.controllers;

import com.example.UserService.models.Product;
import com.example.UserService.models.User;
import com.example.UserService.services.JwtService;
import com.example.UserService.services.SearchClient;
import com.example.UserService.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    // This class will handle user-related requests
    // For example, user registration, login, etc.
    // It will use UserService to perform operations on User data
    // and return appropriate responses to the client.


    private final UserService userService;

    @Autowired
    private SearchClient searchClient;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    // get all users

@GetMapping("/deleteUserMail")
public ResponseEntity<String> deleteUser(@RequestParam String email) {
    try {
        System.out.println("Deleting user with email: " + email);
        userService.deleteUser(email);
        return ResponseEntity.ok("User deleted successfully");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error deleting user: " + e.getMessage());
    }
}



    @GetMapping
    public Iterable<User> getAllUsers() {
        return userService.getAllUsers();
    }


    // add new user take in the body the username, email, password and then in the method body create a new user
    // and call userService.addNewUser(user) to save the user to the database
    // @PostMapping("/add")
    // public void addUser(@Valid @RequestBody User user) {
    //     userService.register(user);
    // }

    @GetMapping("/search-products")
    public ResponseEntity<?> search(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String sort
    ) {
        try {
            // 1. Extract token (remove "Bearer ")
            String token = authHeader.replace("Bearer ", "");

            JwtService jwtService = JwtService.getInstance();
            // 2. Validate it
            if (jwtService.isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("expired token.");
            }


            // 3. Call search
            List<Product> result = searchClient.search(name, category, sort);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: " + e.getMessage());
        }
    }


@GetMapping("/version")
public String version() {
    return "v1.0 deployed at " + System.currentTimeMillis();
}



}

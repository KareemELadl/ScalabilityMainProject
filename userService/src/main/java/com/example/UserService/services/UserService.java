package com.example.UserService.services;

import com.example.UserService.models.User;
import com.example.UserService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    // This class will contain the business logic for user-related operations
    // It will interact with UserRepository to perform CRUD operations on User data
    // and handle any additional logic needed for user management
    // For example, user registration, login, etc.
    // It will also handle validation and error handling for user-related requests

    // Example method to register a new user
    // public User registerUser(User user) {
    //     // Validate user data
    //     // Check if user already exists
    //     // Save user to the database
    //     // Return the saved user
    // }


    @Value("${services.user-service.url}")
    private String userServiceUrl;

    @Autowired
    private UserRepository userRepository;

    @Value("${verification-url-base}")
    private String verificationUrlBase;


    // get all users
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Autowired
    private JwtService jwtService;


    @Autowired
    private EmailService emailService;
//    private final PasswordEncoder passwordEncoder; // e.g., BCryptPasswordEncoder

    public void register(User user, String baseUrl) {
        // 1. Check if email already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalStateException("Email already in use");
        }

        // 2. Hash password and set initial fields
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPassword(user.getPassword());
        user.setEmailVerified(false);
//        user.setCreatedAt(LocalDateTime.now());
//        user.setUpdatedAt(LocalDateTime.now());

        // 3. Save user
        userRepository.save(user);

        // 4. Generate verification token (JWT with email claim)
        String token = jwtService.generateToken(user.getUserId(), user.getEmail());

        // 5. Build verification link
        String verificationLink = baseUrl + "/auth/verify?token=" + token;

        // 6. Send email
        String emailContent = buildVerificationEmail(user.getName(), verificationLink);
        emailService.send(user.getEmail(), emailContent);
    }

    private String buildVerificationEmail(String username, String link) {
        return """
               Hello %s,
               This is a confirmation email for your registration sent by Amazon Replica application.
               Please click the link below to verify your email address and activate your account:
               %s

               This link will expire in 15 minutes.

               Thank you!
               """.formatted(username, link);
    }

    @Transactional
    public boolean deleteUser(String mail){
        User user = userRepository.findByEmail(mail);
        if (user == null) {
            throw new IllegalStateException("User with email " + mail + " does not exist");
        }
        userRepository.deleteByEmail(mail);
        return true; // User deleted successfully
    }
}

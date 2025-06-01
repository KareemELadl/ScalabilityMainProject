package com.example.UserService.services.login;


import com.example.UserService.models.User;
import com.example.UserService.repositories.UserRepository;
import com.example.UserService.services.JwtService;
import com.example.UserService.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("facebook")
public class FacebookLoginStrategy implements LoginStrategy {

    @Autowired
    private UserRepository userRepository;
    private final JwtService jwtService = JwtService.getInstance();

    @Override
    public String login(String email, String unused) {
        SessionService sessionService = SessionService.getInstance();

        // Simulate a "Facebook" user trying to log in
        User user = userRepository.findByEmail(email);

        if (user == null) {
            // Auto-register user if not found
            user = new User();
            user.setEmail(email);
            user.setName(email.split("@")[0] + "_fb"); // distinguish if needed
            user.setPassword("facebook"); // placeholder
            user.setEmailVerified(true);  // Facebook = auto-verified
            user.setRole(User.Role.CLIENT);
            user.setPhoneNumber("01000000");
            userRepository.save(user);
        }

        // Create and return session token
        String token = jwtService.generateToken(user.getUserId(), user.getEmail());
        sessionService.saveSession(token, user.getUserId());

        return token;
    }
}

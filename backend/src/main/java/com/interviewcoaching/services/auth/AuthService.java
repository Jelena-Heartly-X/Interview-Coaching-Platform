package com.interviewcoaching.services.auth;

import java.util.List;
import com.interviewcoaching.dto.AuthRequest;
import com.interviewcoaching.dto.AuthResponse;
import com.interviewcoaching.dto.PasswordResetRequest;
import com.interviewcoaching.models.auth.User;
import com.interviewcoaching.repositories.auth.UserRepository;
import com.interviewcoaching.utils.auth.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse authenticateUser(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsernameOrEmail(),
                        authRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Fix the UserDetails cast
        org.springframework.security.core.userdetails.User userDetails = 
            (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        
        String jwt = jwtUtils.generateToken(userDetails);

        // Get user details from database
        User user = userRepository.findByUsernameOrEmail(authRequest.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new AuthResponse(jwt, user.getId(), user.getUsername(), user.getEmail(), user.getFullName());
    }

    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Add this method for password reset
    public void resetPassword(PasswordResetRequest resetRequest) {
        // Validate passwords match
        if (!resetRequest.getNewPassword().equals(resetRequest.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        System.out.println("🔍 Searching for user with email: " + resetRequest.getEmail());

        // Find user by email - using your existing findByEmail method
        Optional<User> userOptional = userRepository.findByEmail(resetRequest.getEmail());
        
        if (!userOptional.isPresent()) {
            System.out.println("❌ User not found with email: " + resetRequest.getEmail());
            
            // Let's check what emails exist in the database
            List<User> allUsers = userRepository.findAll();
            System.out.println("📋 All users in database:");
            allUsers.forEach(user -> System.out.println(" - " + user.getEmail()));
            
            throw new RuntimeException("User not found with this email address");
        }

        User user = userOptional.get();
        System.out.println("✅ Found user: " + user.getEmail() + " (enabled: " + user.isEnabled() + ")");
        
        // Check if user is enabled
        if (!user.isEnabled()) {
            throw new RuntimeException("Account is disabled. Please contact support.");
        }

        // Check if new password is different from current password
        if (passwordEncoder.matches(resetRequest.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("New password cannot be the same as current password");
        }

        // Hash and update the password
        String hashedPassword = passwordEncoder.encode(resetRequest.getNewPassword());
        user.setPassword(hashedPassword);
        
        // Save the updated user
        userRepository.save(user);
        System.out.println("✅ Password reset successful for: " + user.getEmail());
    }
}
package com.interviewcoaching.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PasswordResetRequest {
    
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;
    
    private String token; // Add this field
    
    @NotBlank(message = "New password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String newPassword;
    
    @NotBlank(message = "Please confirm your password")
    private String confirmPassword;

    // Constructors
    public PasswordResetRequest() {}

    public PasswordResetRequest(String email, String token, String newPassword, String confirmPassword) {
        this.email = email;
        this.token = token;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getToken() { return token; } // Add getter
    public void setToken(String token) { this.token = token; } // Add setter

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}
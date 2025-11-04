package com.interviewcoaching.controllers.interview;

import com.interviewcoaching.models.auth.User;
import com.interviewcoaching.models.interview.UserSkillsAnalytics;
import com.interviewcoaching.services.auth.UserService;
import com.interviewcoaching.services.interview.UserSkillsAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "http://localhost:3000")
public class AnalyticsController {
    
    @Autowired
    private UserSkillsAnalyticsService analyticsService;
    
    @Autowired
    private UserService userService;
    
    /**
     * Get user's overall analytics
     */
    @GetMapping("/my-analytics")
    public ResponseEntity<?> getMyAnalytics(Principal principal) {
        try {
            String username = principal != null ? principal.getName() : "testuser";
            User user = userService.getUserByUsernameOrEmail(username);
            List<UserSkillsAnalytics> analytics = analyticsService.getUserAnalytics(user.getId());
            
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get analytics for specific topic
     */
    @GetMapping("/topic/{topic}")
    public ResponseEntity<?> getTopicAnalytics(@PathVariable String topic, Principal principal) {
        try {
            String username = principal != null ? principal.getName() : "testuser";
            User user = userService.getUserByUsernameOrEmail(username);
            UserSkillsAnalytics analytics = analyticsService.getUserTopicAnalytics(user.getId(), topic)
                .orElseThrow(() -> new RuntimeException("No analytics found for this topic"));
            
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    /**
     * Get user's top skills
     */
    @GetMapping("/top-skills")
    public ResponseEntity<?> getTopSkills(Principal principal) {
        try {
            String username = principal != null ? principal.getName() : "testuser";
            User user = userService.getUserByUsernameOrEmail(username);
            List<UserSkillsAnalytics> topSkills = analyticsService.getTopSkills(user.getId());
            
            return ResponseEntity.ok(topSkills);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get user's weak areas
     */
    @GetMapping("/weak-areas")
    public ResponseEntity<?> getWeakAreas(Principal principal) {
        try {
            String username = principal != null ? principal.getName() : "testuser";
            User user = userService.getUserByUsernameOrEmail(username);
            List<UserSkillsAnalytics> weakAreas = analyticsService.getWeakAreas(user.getId());
            
            return ResponseEntity.ok(weakAreas);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get overall progress percentage
     */
    @GetMapping("/progress")
    public ResponseEntity<?> getProgress(Principal principal) {
        try {
            String username = principal != null ? principal.getName() : "testuser";
            User user = userService.getUserByUsernameOrEmail(username);
            double progress = analyticsService.calculateOverallProgress(user.getId());
            
            Map<String, Object> response = new HashMap<>();
            response.put("progress", progress);
            response.put("message", "Overall progress calculated successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get improvement suggestions
     */
    @GetMapping("/suggestions")
    public ResponseEntity<?> getSuggestions(Principal principal) {
        try {
            String username = principal != null ? principal.getName() : "testuser";
            User user = userService.getUserByUsernameOrEmail(username);
            String suggestions = analyticsService.getImprovementSuggestions(user.getId());
            
            Map<String, String> response = new HashMap<>();
            response.put("suggestions", suggestions);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

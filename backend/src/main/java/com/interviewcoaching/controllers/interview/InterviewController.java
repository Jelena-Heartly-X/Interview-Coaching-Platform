package com.interviewcoaching.controllers.interview;

import com.interviewcoaching.dto.interview.AnswerSubmitRequest;
import com.interviewcoaching.dto.interview.InterviewStartRequest;
import com.interviewcoaching.dto.interview.InterviewDetailsDTO;
import com.interviewcoaching.dto.interview.QuestionDTO;
import com.interviewcoaching.models.auth.User;
import com.interviewcoaching.models.interview.*;
import com.interviewcoaching.services.auth.UserService;
import com.interviewcoaching.services.interview.InterviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.validation.Valid;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interviews")
@CrossOrigin(origins = "http://localhost:3000")
public class InterviewController {
    
    @Autowired
    private InterviewService interviewService;
    
    @Autowired
    private UserService userService;
    
    /**
     * Start a new interview
     */
    @PostMapping("/start")
    public ResponseEntity<?> startInterview(@Valid @RequestBody InterviewStartRequest request, Principal principal) {
        try {
            System.out.println("===== START INTERVIEW REQUEST =====");
            System.out.println("Request: " + request);
            System.out.println("Topic: " + request.getTopic());
            System.out.println("Difficulty: " + request.getDifficultyLevel());
            System.out.println("Question Count: " + request.getQuestionCount());
            System.out.println("Duration: " + request.getDuration() + " minutes");
            
            // Get current user
            String username = principal != null ? principal.getName() : "testuser";
            System.out.println("Username: " + username);
            User user = userService.getUserByUsernameOrEmail(username);
            System.out.println("User found: " + user.getId());
            
            // Start interview
            System.out.println("Calling interviewService.startInterview...");
            Interview interview = interviewService.startInterview(user, request);
            System.out.println("Interview created with ID: " + interview.getId());
            
            // Convert to DTOs to avoid serialization issues
            InterviewDetailsDTO interviewDTO = new InterviewDetailsDTO(interview);
            
            // Get questions and convert to DTOs
            List<QuestionDTO> questionDTOs = interview.getInterviewQuestions().stream()
                .map(InterviewQuestion::getQuestion)
                .map(QuestionDTO::new)
                .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("interview", interviewDTO);
            response.put("questions", questionDTOs);
            response.put("message", "Interview started successfully");
            
            System.out.println("===== START INTERVIEW SUCCESS =====");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("===== START INTERVIEW ERROR =====");
            System.err.println("Error Type: " + e.getClass().getName());
            System.err.println("Error Message: " + e.getMessage());
            System.err.println("Stack Trace:");
            e.printStackTrace();
            System.err.println("===================================");
            
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("type", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Submit an answer
     */
    @PostMapping("/{interviewId}/questions/{questionId}/submit-answer")
    public ResponseEntity<?> submitAnswer(
            @PathVariable Long interviewId,
            @PathVariable Long questionId,
            @Valid @RequestBody AnswerSubmitRequest request,
            Principal principal) {
        try {
            // Get the current user
            String username = principal != null ? principal.getName() : "testuser";
            User user = userService.getUserByUsernameOrEmail(username);
            
            InterviewResponse response = interviewService.submitAnswer(interviewId, questionId, request, user);
            
            Map<String, Object> result = new HashMap<>();
            result.put("response", response);
            result.put("message", "Answer submitted successfully");
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    /**
     * Complete interview
     */
    @PostMapping("/{interviewId}/complete")
    public ResponseEntity<?> completeInterview(
            @PathVariable Long interviewId,
            Principal principal) {
        try {
            // Get current user
            String username = principal != null ? principal.getName() : "testuser";
            User user = userService.getUserByUsernameOrEmail(username);
            
            // Complete the interview
            Interview interview = interviewService.completeInterview(interviewId, user);
            
            // Convert to DTO to avoid serialization issues
            InterviewDetailsDTO interviewDTO = new InterviewDetailsDTO(interview);
            
            Map<String, Object> response = new HashMap<>();
            response.put("interview", interviewDTO);
            response.put("message", "Interview completed successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            e.printStackTrace(); // Log the error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    /**
     * Get interview details
     */
    @GetMapping("/{interviewId}")
    public ResponseEntity<?> getInterview(
            @PathVariable Long interviewId,
            Principal principal) {
        try {
            // Get current user
            String username = principal != null ? principal.getName() : "testuser";
            User user = userService.getUserByUsernameOrEmail(username);
            
            // Get interview details for the authenticated user
            Interview interview = interviewService.getInterviewDetails(interviewId, user);
            
            // Convert to DTO to avoid serialization issues
            InterviewDetailsDTO interviewDTO = new InterviewDetailsDTO(interview);
            
            // Get questions and convert to DTOs
            List<QuestionDTO> questionDTOs = interview.getInterviewQuestions().stream()
                .map(InterviewQuestion::getQuestion)
                .map(QuestionDTO::new)
                .collect(Collectors.toList());
            
            // Return interview with questions
            Map<String, Object> response = new HashMap<>();
            response.put("interview", interviewDTO);
            response.put("questions", questionDTOs);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            e.printStackTrace(); // Log the error
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    /**
     * Get user's interview history
     */
    @GetMapping("/history")
    public ResponseEntity<?> getInterviewHistory(Principal principal) {
        try {
            String username = principal != null ? principal.getName() : "testuser";
            User user = userService.getUserByUsernameOrEmail(username);
            List<Interview> interviews = interviewService.getInterviewHistory(user);
            
            return ResponseEntity.ok(interviews);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get completed interviews
     */
    @GetMapping("/completed")
    public ResponseEntity<?> getCompletedInterviews(Principal principal) {
        try {
            String username = principal != null ? principal.getName() : "testuser";
            User user = userService.getUserByUsernameOrEmail(username);
            List<Interview> interviews = interviewService.getCompletedInterviews(user);
            
            return ResponseEntity.ok(interviews);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Cancel interview
     */
    @PutMapping("/{interviewId}/cancel")
    public ResponseEntity<?> cancelInterview(
            @PathVariable Long interviewId,
            Principal principal) {
        try {
            // Get current user
            String username = principal != null ? principal.getName() : "testuser";
            User user = userService.getUserByUsernameOrEmail(username);
            
            // Cancel the interview
            Interview interview = interviewService.cancelInterview(interviewId, user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("interview", interview);
            response.put("message", "Interview cancelled successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}

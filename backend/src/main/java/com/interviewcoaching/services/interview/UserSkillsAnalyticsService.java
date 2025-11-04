package com.interviewcoaching.services.interview;

import com.interviewcoaching.models.auth.User;
import com.interviewcoaching.models.interview.Interview;
import com.interviewcoaching.models.interview.InterviewResponse;
import com.interviewcoaching.models.interview.UserSkillsAnalytics;
import com.interviewcoaching.repositories.interview.InterviewResponseRepository;
import com.interviewcoaching.repositories.interview.UserSkillsAnalyticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserSkillsAnalyticsService {
    
    @Autowired
    private UserSkillsAnalyticsRepository analyticsRepository;
    
    @Autowired
    private InterviewResponseRepository responseRepository;
    
    /**
     * Update user skills analytics after interview
     */
    public void updateSkillsAnalytics(User user, Interview interview) {
        String topic = interview.getTopic();
        
        // Get or create analytics record for this topic
        Optional<UserSkillsAnalytics> existingAnalytics = 
            analyticsRepository.findByUserIdAndTopic(user.getId(), topic);
        
        UserSkillsAnalytics analytics;
        if (existingAnalytics.isPresent()) {
            analytics = existingAnalytics.get();
        } else {
            analytics = new UserSkillsAnalytics();
            analytics.setUser(user);
            analytics.setTopic(topic);
        }
        
        // Update attempt counts
        analytics.setTotalAttempts(analytics.getTotalAttempts() + 1);
        
        // Check if interview was successful (>60% score)
        if (interview.getMaxScore() > 0) {
            double scorePercentage = (double) interview.getTotalScore() / interview.getMaxScore() * 100;
            if (scorePercentage >= 60) {
                analytics.setSuccessfulAttempts(analytics.getSuccessfulAttempts() + 1);
            }
        }
        
        // Calculate accuracy percentage
        BigDecimal accuracy = BigDecimal.ZERO;
        if (analytics.getTotalAttempts() > 0) {
            accuracy = BigDecimal.valueOf(analytics.getSuccessfulAttempts())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(analytics.getTotalAttempts()), 2, RoundingMode.HALF_UP);
        }
        analytics.setAccuracyPercentage(accuracy);
        
        // Calculate average time
        List<InterviewResponse> responses = responseRepository.findByInterviewId(interview.getId());
        if (!responses.isEmpty()) {
            double avgTime = responses.stream()
                .filter(r -> r.getTimeTakenSeconds() != null)
                .mapToInt(InterviewResponse::getTimeTakenSeconds)
                .average()
                .orElse(0);
            analytics.setAverageTimeSeconds((int) avgTime);
        }
        
        // Update last attempted time
        analytics.setLastAttemptedAt(LocalDateTime.now());
        
        // Determine skill level based on accuracy and attempts
        String skillLevel = determineSkillLevel(accuracy.doubleValue(), analytics.getTotalAttempts());
        analytics.setSkillLevel(skillLevel);
        
        analyticsRepository.save(analytics);
    }
    
    /**
     * Determine skill level based on performance
     */
    private String determineSkillLevel(double accuracy, int attempts) {
        if (attempts >= 10 && accuracy >= 80) {
            return "EXPERT";
        } else if (attempts >= 5 && accuracy >= 70) {
            return "ADVANCED";
        } else if (attempts >= 3 && accuracy >= 50) {
            return "INTERMEDIATE";
        } else {
            return "BEGINNER";
        }
    }
    
    /**
     * Get user analytics for all topics
     */
    public List<UserSkillsAnalytics> getUserAnalytics(Long userId) {
        return analyticsRepository.findByUserId(userId);
    }
    
    /**
     * Get user analytics for specific topic
     */
    public Optional<UserSkillsAnalytics> getUserTopicAnalytics(Long userId, String topic) {
        return analyticsRepository.findByUserIdAndTopic(userId, topic);
    }
    
    /**
     * Get top skills for a user
     */
    public List<UserSkillsAnalytics> getTopSkills(Long userId) {
        return analyticsRepository.findTopSkillsByUserId(userId);
    }
    
    /**
     * Get weak areas that need improvement
     */
    public List<UserSkillsAnalytics> getWeakAreas(Long userId) {
        return analyticsRepository.findWeakAreasByUserId(userId);
    }
    
    /**
     * Get analytics by topic across all users
     */
    public List<UserSkillsAnalytics> getAnalyticsByTopic(String topic) {
        return analyticsRepository.findByTopic(topic);
    }
    
    /**
     * Calculate user's overall progress
     */
    public double calculateOverallProgress(Long userId) {
        List<UserSkillsAnalytics> allAnalytics = analyticsRepository.findByUserId(userId);
        
        if (allAnalytics.isEmpty()) {
            return 0.0;
        }
        
        double totalAccuracy = allAnalytics.stream()
            .mapToDouble(a -> a.getAccuracyPercentage().doubleValue())
            .average()
            .orElse(0.0);
        
        return totalAccuracy;
    }
    
    /**
     * Get skill improvement suggestions
     */
    public String getImprovementSuggestions(Long userId) {
        List<UserSkillsAnalytics> weakAreas = getWeakAreas(userId);
        
        if (weakAreas.isEmpty()) {
            return "You're doing great! Keep practicing to maintain your skills.";
        }
        
        StringBuilder suggestions = new StringBuilder("Focus on improving: ");
        for (int i = 0; i < Math.min(3, weakAreas.size()); i++) {
            UserSkillsAnalytics area = weakAreas.get(i);
            suggestions.append(area.getTopic());
            if (i < Math.min(3, weakAreas.size()) - 1) {
                suggestions.append(", ");
            }
        }
        
        return suggestions.toString();
    }
}

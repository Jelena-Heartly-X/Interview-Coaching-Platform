package com.interviewcoaching.services.interview;

import com.interviewcoaching.models.auth.User;
import com.interviewcoaching.models.interview.*;
import com.interviewcoaching.repositories.interview.InterviewRepository;
import com.interviewcoaching.repositories.interview.UserSkillsAnalyticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {
    
    @Autowired
    private InterviewRepository interviewRepository;
    
    @Autowired
    private UserSkillsAnalyticsRepository userSkillsAnalyticsRepository;
    
    /**
     * Update user analytics after completing an interview
     */
    @Transactional
    public void updateAnalytics(Interview interview) {
        User user = interview.getUser();
        List<InterviewQuestion> questions = interview.getInterviewQuestions();
        
        // Group questions by category
        Map<String, List<InterviewQuestion>> categoryMap = questions.stream()
            .collect(Collectors.groupingBy(iq -> iq.getQuestion().getCategory()));
        
        // Update analytics for each category
        for (Map.Entry<String, List<InterviewQuestion>> entry : categoryMap.entrySet()) {
            String category = entry.getKey();
            List<InterviewQuestion> categoryQuestions = entry.getValue();
            
            updateCategoryAnalytics(user, category, categoryQuestions);
        }
    }
    
    /**
     * Update analytics for a specific category
     */
    private void updateCategoryAnalytics(User user, String category, List<InterviewQuestion> questions) {
        // Find or create analytics record
        UserSkillsAnalytics analytics = userSkillsAnalyticsRepository
            .findByUserAndTopic(user, category)
            .orElse(new UserSkillsAnalytics());
        
        if (analytics.getId() == null) {
            analytics.setUser(user);
            analytics.setTopic(category);
        }
        
        // Calculate statistics
        int totalAttempts = questions.size();
        int successfulAttempts = 0;
        int totalTime = 0;
        
        for (InterviewQuestion iq : questions) {
            InterviewResponse response = iq.getResponse();
            if (response != null) {
                if (response.getIsCorrect() != null && response.getIsCorrect()) {
                    successfulAttempts++;
                }
                if (response.getTimeTakenSeconds() != null) {
                    totalTime += response.getTimeTakenSeconds();
                }
            }
        }
        
        // Update cumulative statistics
        int previousAttempts = analytics.getTotalAttempts() != null ? analytics.getTotalAttempts() : 0;
        int previousSuccessful = analytics.getSuccessfulAttempts() != null ? analytics.getSuccessfulAttempts() : 0;
        int previousAvgTime = analytics.getAverageTimeSeconds() != null ? analytics.getAverageTimeSeconds() : 0;
        
        analytics.setTotalAttempts(previousAttempts + totalAttempts);
        analytics.setSuccessfulAttempts(previousSuccessful + successfulAttempts);
        
        // Calculate accuracy
        BigDecimal accuracy = BigDecimal.ZERO;
        if (analytics.getTotalAttempts() > 0) {
            accuracy = BigDecimal.valueOf(analytics.getSuccessfulAttempts())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(analytics.getTotalAttempts()), 2, RoundingMode.HALF_UP);
        }
        analytics.setAccuracyPercentage(accuracy);
        
        // Calculate average time
        if (totalAttempts > 0) {
            int newAvgTime = (previousAvgTime * previousAttempts + totalTime) / analytics.getTotalAttempts();
            analytics.setAverageTimeSeconds(newAvgTime);
        }
        
        // Update skill level
        analytics.setSkillLevel(determineSkillLevel(accuracy, analytics.getTotalAttempts()));
        analytics.setLastAttemptedAt(LocalDateTime.now());
        
        userSkillsAnalyticsRepository.save(analytics);
    }
    
    /**
     * Determine skill level based on accuracy and experience
     */
    private String determineSkillLevel(BigDecimal accuracy, int totalAttempts) {
        double acc = accuracy.doubleValue();
        
        if (totalAttempts < 5) {
            return "BEGINNER";
        } else if (totalAttempts < 15) {
            return acc >= 70 ? "INTERMEDIATE" : "BEGINNER";
        } else if (totalAttempts < 30) {
            return acc >= 80 ? "ADVANCED" : acc >= 60 ? "INTERMEDIATE" : "BEGINNER";
        } else {
            return acc >= 85 ? "EXPERT" : acc >= 75 ? "ADVANCED" : acc >= 55 ? "INTERMEDIATE" : "BEGINNER";
        }
    }
    
    /**
     * Get user's overall analytics summary
     */
    public AnalyticsSummary getUserAnalyticsSummary(User user) {
        List<UserSkillsAnalytics> allAnalytics = userSkillsAnalyticsRepository.findByUser(user);
        List<Interview> allInterviews = interviewRepository.findByUserOrderByCreatedAtDesc(user);
        
        AnalyticsSummary summary = new AnalyticsSummary();
        summary.setUserId(user.getId());
        summary.setTotalInterviews(allInterviews.size());
        
        // Calculate completed interviews
        long completedCount = allInterviews.stream()
            .filter(i -> i.getStatus() == InterviewStatus.COMPLETED)
            .count();
        summary.setCompletedInterviews((int) completedCount);
        
        // Calculate average score
        OptionalDouble avgScore = allInterviews.stream()
            .filter(i -> i.getStatus() == InterviewStatus.COMPLETED && i.getTotalScore() != null)
            .mapToInt(Interview::getTotalScore)
            .average();
        summary.setAverageScore(avgScore.isPresent() ? (int) avgScore.getAsDouble() : 0);
        
        // Category breakdown
        summary.setCategoryAnalytics(allAnalytics);
        
        // Recent interviews
        summary.setRecentInterviews(allInterviews.stream()
            .limit(5)
            .collect(Collectors.toList()));
        
        // Calculate improvement trend
        summary.setImprovementTrend(calculateImprovementTrend(allInterviews));
        
        // Recommendations
        summary.setRecommendations(generateRecommendations(allAnalytics));
        
        return summary;
    }
    
    /**
     * Get analytics for a specific topic
     */
    public UserSkillsAnalytics getTopicAnalytics(User user, String topic) {
        return userSkillsAnalyticsRepository
            .findByUserAndTopic(user, topic)
            .orElse(new UserSkillsAnalytics());
    }
    
    /**
     * Calculate improvement trend
     */
    private String calculateImprovementTrend(List<Interview> interviews) {
        if (interviews.size() < 2) {
            return "INSUFFICIENT_DATA";
        }
        
        // Get last 5 interviews
        List<Interview> recentInterviews = interviews.stream()
            .filter(i -> i.getStatus() == InterviewStatus.COMPLETED && i.getTotalScore() != null)
            .limit(5)
            .collect(Collectors.toList());
        
        if (recentInterviews.size() < 2) {
            return "INSUFFICIENT_DATA";
        }
        
        // Compare average of first half vs second half
        int midpoint = recentInterviews.size() / 2;
        double firstHalfAvg = recentInterviews.stream()
            .limit(midpoint)
            .mapToInt(Interview::getTotalScore)
            .average()
            .orElse(0);
        
        double secondHalfAvg = recentInterviews.stream()
            .skip(midpoint)
            .mapToInt(Interview::getTotalScore)
            .average()
            .orElse(0);
        
        if (secondHalfAvg > firstHalfAvg + 10) {
            return "IMPROVING";
        } else if (secondHalfAvg < firstHalfAvg - 10) {
            return "DECLINING";
        } else {
            return "STABLE";
        }
    }
    
    /**
     * Generate personalized recommendations
     */
    private List<String> generateRecommendations(List<UserSkillsAnalytics> analytics) {
        List<String> recommendations = new ArrayList<>();
        
        if (analytics.isEmpty()) {
            recommendations.add("Take your first interview to get personalized recommendations");
            return recommendations;
        }
        
        // Find weak areas (accuracy < 60%)
        List<UserSkillsAnalytics> weakAreas = analytics.stream()
            .filter(a -> a.getAccuracyPercentage().doubleValue() < 60)
            .sorted(Comparator.comparing(UserSkillsAnalytics::getAccuracyPercentage))
            .limit(3)
            .collect(Collectors.toList());
        
        if (!weakAreas.isEmpty()) {
            for (UserSkillsAnalytics area : weakAreas) {
                recommendations.add(String.format("Focus on %s - Current accuracy: %.1f%%", 
                    area.getTopic(), area.getAccuracyPercentage()));
            }
        }
        
        // Find topics not attempted recently
        List<UserSkillsAnalytics> staleTopics = analytics.stream()
            .filter(a -> a.getLastAttemptedAt() != null && 
                        a.getLastAttemptedAt().isBefore(LocalDateTime.now().minusDays(7)))
            .limit(2)
            .collect(Collectors.toList());
        
        for (UserSkillsAnalytics topic : staleTopics) {
            recommendations.add(String.format("Practice %s - Last attempted %d days ago",
                topic.getTopic(), 
                java.time.Duration.between(topic.getLastAttemptedAt(), LocalDateTime.now()).toDays()));
        }
        
        // General recommendations based on overall performance
        double avgAccuracy = analytics.stream()
            .mapToDouble(a -> a.getAccuracyPercentage().doubleValue())
            .average()
            .orElse(0);
        
        if (avgAccuracy < 50) {
            recommendations.add("Focus on fundamentals - review basic concepts before attempting interviews");
        } else if (avgAccuracy < 70) {
            recommendations.add("Good progress! Practice consistently to reach intermediate level");
        } else if (avgAccuracy >= 85) {
            recommendations.add("Excellent work! Challenge yourself with advanced topics");
        }
        
        return recommendations;
    }
    
    /**
     * Get progress over time
     */
    public List<ProgressDataPoint> getProgressOverTime(User user, int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        List<Interview> interviews = interviewRepository
            .findByUserAndStatusAndCreatedAtAfter(user, InterviewStatus.COMPLETED, startDate);
        
        // Group by date and calculate average score
        Map<String, List<Interview>> byDate = interviews.stream()
            .collect(Collectors.groupingBy(i -> i.getCreatedAt().toLocalDate().toString()));
        
        List<ProgressDataPoint> progress = new ArrayList<>();
        for (Map.Entry<String, List<Interview>> entry : byDate.entrySet()) {
            double avgScore = entry.getValue().stream()
                .filter(i -> i.getTotalScore() != null)
                .mapToInt(Interview::getTotalScore)
                .average()
                .orElse(0);
            
            ProgressDataPoint point = new ProgressDataPoint();
            point.setDate(entry.getKey());
            point.setAverageScore((int) avgScore);
            point.setInterviewCount(entry.getValue().size());
            progress.add(point);
        }
        
        progress.sort(Comparator.comparing(ProgressDataPoint::getDate));
        return progress;
    }
    
    /**
     * Analytics Summary DTO
     */
    public static class AnalyticsSummary {
        private Long userId;
        private int totalInterviews;
        private int completedInterviews;
        private int averageScore;
        private List<UserSkillsAnalytics> categoryAnalytics;
        private List<Interview> recentInterviews;
        private String improvementTrend;
        private List<String> recommendations;
        
        // Getters and Setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        
        public int getTotalInterviews() { return totalInterviews; }
        public void setTotalInterviews(int totalInterviews) { this.totalInterviews = totalInterviews; }
        
        public int getCompletedInterviews() { return completedInterviews; }
        public void setCompletedInterviews(int completedInterviews) { this.completedInterviews = completedInterviews; }
        
        public int getAverageScore() { return averageScore; }
        public void setAverageScore(int averageScore) { this.averageScore = averageScore; }
        
        public List<UserSkillsAnalytics> getCategoryAnalytics() { return categoryAnalytics; }
        public void setCategoryAnalytics(List<UserSkillsAnalytics> categoryAnalytics) { this.categoryAnalytics = categoryAnalytics; }
        
        public List<Interview> getRecentInterviews() { return recentInterviews; }
        public void setRecentInterviews(List<Interview> recentInterviews) { this.recentInterviews = recentInterviews; }
        
        public String getImprovementTrend() { return improvementTrend; }
        public void setImprovementTrend(String improvementTrend) { this.improvementTrend = improvementTrend; }
        
        public List<String> getRecommendations() { return recommendations; }
        public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }
    }
    
    /**
     * Progress Data Point DTO
     */
    public static class ProgressDataPoint {
        private String date;
        private int averageScore;
        private int interviewCount;
        
        // Getters and Setters
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
        
        public int getAverageScore() { return averageScore; }
        public void setAverageScore(int averageScore) { this.averageScore = averageScore; }
        
        public int getInterviewCount() { return interviewCount; }
        public void setInterviewCount(int interviewCount) { this.interviewCount = interviewCount; }
    }
}

package com.interviewcoaching.models.interview;

import com.interviewcoaching.models.auth.User;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_skills_analytics")
public class UserSkillsAnalytics {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false, length = 100)
    private String topic;
    
    @Column(name = "sub_category", length = 100)
    private String subCategory;
    
    @Column(name = "total_attempts")
    private Integer totalAttempts = 0;
    
    @Column(name = "successful_attempts")
    private Integer successfulAttempts = 0;
    
    @Column(name = "accuracy_percentage", precision = 5, scale = 2)
    private BigDecimal accuracyPercentage = BigDecimal.ZERO;
    
    @Column(name = "average_time_seconds")
    private Integer averageTimeSeconds;
    
    @Column(name = "last_attempted_at")
    private LocalDateTime lastAttemptedAt;
    
    @Column(name = "skill_level", length = 20)
    private String skillLevel; // BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public UserSkillsAnalytics() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getTopic() {
        return topic;
    }
    
    public void setTopic(String topic) {
        this.topic = topic;
    }
    
    public String getSubCategory() {
        return subCategory;
    }
    
    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
    
    public Integer getTotalAttempts() {
        return totalAttempts;
    }
    
    public void setTotalAttempts(Integer totalAttempts) {
        this.totalAttempts = totalAttempts;
    }
    
    public Integer getSuccessfulAttempts() {
        return successfulAttempts;
    }
    
    public void setSuccessfulAttempts(Integer successfulAttempts) {
        this.successfulAttempts = successfulAttempts;
    }
    
    public BigDecimal getAccuracyPercentage() {
        return accuracyPercentage;
    }
    
    public void setAccuracyPercentage(BigDecimal accuracyPercentage) {
        this.accuracyPercentage = accuracyPercentage;
    }
    
    public Integer getAverageTimeSeconds() {
        return averageTimeSeconds;
    }
    
    public void setAverageTimeSeconds(Integer averageTimeSeconds) {
        this.averageTimeSeconds = averageTimeSeconds;
    }
    
    public LocalDateTime getLastAttemptedAt() {
        return lastAttemptedAt;
    }
    
    public void setLastAttemptedAt(LocalDateTime lastAttemptedAt) {
        this.lastAttemptedAt = lastAttemptedAt;
    }
    
    public String getSkillLevel() {
        return skillLevel;
    }
    
    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

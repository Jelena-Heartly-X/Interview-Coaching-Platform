package com.interviewcoaching.dto.interview;

import com.interviewcoaching.models.interview.Interview;
import java.time.LocalDateTime;

public class InterviewDetailsDTO {
    private Long id;
    private String title;
    private String topic;
    private String difficultyLevel;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer totalScore;
    private Integer maxScore;
    private String feedback;
    private Integer questionCount;
    private LocalDateTime createdAt;
    
    // Constructor to create from Interview entity
    public InterviewDetailsDTO(Interview interview) {
        this.id = interview.getId();
        this.title = interview.getTitle();
        this.topic = interview.getTopic();
        this.difficultyLevel = interview.getDifficultyLevel() != null ? 
            interview.getDifficultyLevel().toString() : null;
        this.status = interview.getStatus() != null ? 
            interview.getStatus().toString() : null;
        this.startTime = interview.getStartTime();
        this.endTime = interview.getEndTime();
        this.totalScore = interview.getTotalScore();
        this.maxScore = interview.getMaxScore();
        this.feedback = interview.getFeedback();
        this.questionCount = interview.getQuestionCount();
        this.createdAt = interview.getCreatedAt();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

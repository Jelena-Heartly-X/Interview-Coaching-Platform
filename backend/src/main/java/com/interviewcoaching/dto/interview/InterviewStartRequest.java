package com.interviewcoaching.dto.interview;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class InterviewStartRequest {
    
    @NotBlank(message = "Topic is required")
    private String topic;
    
    @NotBlank(message = "Difficulty level is required")
    private String difficultyLevel;
    
    @Min(value = 1, message = "At least 1 question is required")
    private Integer questionCount = 5;
    
    @Min(value = 5, message = "Duration must be at least 5 minutes")
    private Integer duration = 30; // Duration in minutes
    
    // Constructors
    public InterviewStartRequest() {}
    
    public InterviewStartRequest(String topic, String difficultyLevel, Integer questionCount, Integer duration) {
        this.topic = topic;
        this.difficultyLevel = difficultyLevel;
        this.questionCount = questionCount;
        this.duration = duration;
    }
    
    // Getters and Setters
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
    
    public Integer getQuestionCount() {
        return questionCount;
    }
    
    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }
    
    public Integer getDuration() {
        return duration;
    }
    
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
    @Override
    public String toString() {
        return "InterviewStartRequest{" +
                "topic='" + topic + '\'' +
                ", difficultyLevel='" + difficultyLevel + '\'' +
                ", questionCount=" + questionCount +
                ", duration=" + duration +
                " minutes}";
    }
}

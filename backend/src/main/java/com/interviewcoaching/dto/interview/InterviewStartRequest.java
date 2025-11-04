package com.interviewcoaching.dto.interview;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class InterviewStartRequest {
    
    private Long slotId;
    
    @NotBlank(message = "Topic is required")
    private String topic;
    
    @NotBlank(message = "Difficulty level is required")
    private String difficultyLevel;
    
    @Min(value = 1, message = "At least 1 question is required")
    private Integer questionCount = 5;
    
    // Constructors
    public InterviewStartRequest() {}
    
    public InterviewStartRequest(Long slotId, String topic, String difficultyLevel, Integer questionCount) {
        this.slotId = slotId;
        this.topic = topic;
        this.difficultyLevel = difficultyLevel;
        this.questionCount = questionCount;
    }
    
    // Getters and Setters
    public Long getSlotId() {
        return slotId;
    }
    
    public void setSlotId(Long slotId) {
        this.slotId = slotId;
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
    
    public Integer getQuestionCount() {
        return questionCount;
    }
    
    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }
}

package com.interviewcoaching.dto.interview;

import javax.validation.constraints.NotNull;

public class AnswerSubmitRequest {
    
    @NotNull(message = "Question ID is required")
    private Long questionId;
    
    private String answer;
    private String codeSubmission;
    private String programmingLanguage;
    private Integer timeTakenSeconds;
    private Integer hintsUsed;
    
    // Constructors
    public AnswerSubmitRequest() {}
    
    // Getters and Setters
    public Long getQuestionId() {
        return questionId;
    }
    
    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
    
    public String getAnswer() {
        return answer;
    }
    
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    
    public String getCodeSubmission() {
        return codeSubmission;
    }
    
    public void setCodeSubmission(String codeSubmission) {
        this.codeSubmission = codeSubmission;
    }
    
    public String getProgrammingLanguage() {
        return programmingLanguage;
    }
    
    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }
    
    public Integer getTimeTakenSeconds() {
        return timeTakenSeconds;
    }
    
    public void setTimeTakenSeconds(Integer timeTakenSeconds) {
        this.timeTakenSeconds = timeTakenSeconds;
    }
    
    public Integer getHintsUsed() {
        return hintsUsed;
    }
    
    public void setHintsUsed(Integer hintsUsed) {
        this.hintsUsed = hintsUsed;
    }
}

// InterviewResponse.java
package com.interviewcoaching.models.interview;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "interview_responses")
public class InterviewResponse {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_question_id")
    private InterviewQuestion interviewQuestion;
    
    @Column(columnDefinition = "TEXT")
    private String answer;
    
    @Column(columnDefinition = "TEXT")
    private String codeSubmission;
    
    @Column(name = "programming_language")
    private String programmingLanguage;
    
    @Column(columnDefinition = "TEXT")
    private String feedback;
    
    @Column(name = "score_obtained")
    private Integer scoreObtained;
    
    @Column(name = "is_correct")
    private Boolean isCorrect;
    
    @Column(name = "execution_output", columnDefinition = "TEXT")
    private String executionOutput;
    
    @Column(name = "hints_used")
    private Integer hintsUsed = 0;
    
    @Column(name = "test_cases_total")
    private Integer testCasesTotal;
    
    @Column(name = "test_cases_passed")
    private Integer testCasesPassed;
    
    @Column(name = "time_taken_seconds")
    private Integer timeTakenSeconds;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.submittedAt == null) {
            this.submittedAt = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InterviewQuestion getInterviewQuestion() {
        return interviewQuestion;
    }

    public void setInterviewQuestion(InterviewQuestion interviewQuestion) {
        this.interviewQuestion = interviewQuestion;
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

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Integer getScoreObtained() {
        return scoreObtained;
    }

    public void setScoreObtained(Integer scoreObtained) {
        this.scoreObtained = scoreObtained;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public String getExecutionOutput() {
        return executionOutput;
    }

    public void setExecutionOutput(String executionOutput) {
        this.executionOutput = executionOutput;
    }

    public Integer getHintsUsed() {
        return hintsUsed;
    }

    public void setHintsUsed(Integer hintsUsed) {
        this.hintsUsed = hintsUsed;
    }

    public Integer getTestCasesTotal() {
        return testCasesTotal;
    }

    public void setTestCasesTotal(Integer testCasesTotal) {
        this.testCasesTotal = testCasesTotal;
    }

    public Integer getTestCasesPassed() {
        return testCasesPassed;
    }

    public void setTestCasesPassed(Integer testCasesPassed) {
        this.testCasesPassed = testCasesPassed;
    }

    public Integer getTimeTakenSeconds() {
        return timeTakenSeconds;
    }

    public void setTimeTakenSeconds(Integer timeTakenSeconds) {
        this.timeTakenSeconds = timeTakenSeconds;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "InterviewResponse{" +
                "id=" + id +
                ", scoreObtained=" + scoreObtained +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
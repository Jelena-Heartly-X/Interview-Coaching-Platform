package com.interviewcoaching.models.interview;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "interview_evaluations")
public class InterviewEvaluation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview;
    
    @Column(name = "overall_score")
    private Integer overallScore;
    
    @Column(name = "code_quality_score")
    private Integer codeQualityScore;
    
    @Column(name = "logical_reasoning_score")
    private Integer logicalReasoningScore;
    
    @Column(name = "time_management_score")
    private Integer timeManagementScore;
    
    @Column(name = "problem_solving_score")
    private Integer problemSolvingScore;
    
    @Column(name = "ai_feedback", columnDefinition = "TEXT")
    private String aiFeedback;
    
    @Column(columnDefinition = "TEXT")
    private String strengths;
    
    @Column(columnDefinition = "TEXT")
    private String weaknesses;
    
    @Column(name = "improvement_suggestions", columnDefinition = "TEXT")
    private String improvementSuggestions;
    
    @Column(name = "confidence_level", length = 20)
    private String confidenceLevel; // LOW, MEDIUM, HIGH
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors
    public InterviewEvaluation() {
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Interview getInterview() {
        return interview;
    }
    
    public void setInterview(Interview interview) {
        this.interview = interview;
    }
    
    public Integer getOverallScore() {
        return overallScore;
    }
    
    public void setOverallScore(Integer overallScore) {
        this.overallScore = overallScore;
    }
    
    public Integer getCodeQualityScore() {
        return codeQualityScore;
    }
    
    public void setCodeQualityScore(Integer codeQualityScore) {
        this.codeQualityScore = codeQualityScore;
    }
    
    public Integer getLogicalReasoningScore() {
        return logicalReasoningScore;
    }
    
    public void setLogicalReasoningScore(Integer logicalReasoningScore) {
        this.logicalReasoningScore = logicalReasoningScore;
    }
    
    public Integer getTimeManagementScore() {
        return timeManagementScore;
    }
    
    public void setTimeManagementScore(Integer timeManagementScore) {
        this.timeManagementScore = timeManagementScore;
    }
    
    public Integer getProblemSolvingScore() {
        return problemSolvingScore;
    }
    
    public void setProblemSolvingScore(Integer problemSolvingScore) {
        this.problemSolvingScore = problemSolvingScore;
    }
    
    public String getAiFeedback() {
        return aiFeedback;
    }
    
    public void setAiFeedback(String aiFeedback) {
        this.aiFeedback = aiFeedback;
    }
    
    public String getStrengths() {
        return strengths;
    }
    
    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }
    
    public String getWeaknesses() {
        return weaknesses;
    }
    
    public void setWeaknesses(String weaknesses) {
        this.weaknesses = weaknesses;
    }
    
    public String getImprovementSuggestions() {
        return improvementSuggestions;
    }
    
    public void setImprovementSuggestions(String improvementSuggestions) {
        this.improvementSuggestions = improvementSuggestions;
    }
    
    public String getConfidenceLevel() {
        return confidenceLevel;
    }
    
    public void setConfidenceLevel(String confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

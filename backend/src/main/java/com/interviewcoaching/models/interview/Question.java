package com.interviewcoaching.models.interview;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question {
    
    public enum QuestionType {
        CODING, MCQ, THEORETICAL, SYSTEM_DESIGN
    }
    
    public enum DifficultyLevel {
        BEGINNER, INTERMEDIATE, ADVANCED
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 500)
    private String title;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false, length = 50)
    private QuestionType questionType;
    
    @Column(nullable = false, length = 100)
    private String category; // DSA, DBMS, OS, CN, OOP, WEB_DEV, SYSTEM_DESIGN
    
    @Column(name = "sub_category", length = 100)
    private String subCategory; // Arrays, Strings, Trees, Graphs, etc.
    
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", nullable = false, length = 20)
    private DifficultyLevel difficultyLevel;
    
    @Column(name = "time_limit_seconds")
    private Integer timeLimitSeconds = 900; // 15 minutes default
    
    @Column
    private Integer points = 10;
    
    @Column(columnDefinition = "TEXT")
    private String hints;
    
    @Column(columnDefinition = "TEXT")
    private String solution;
    
    @Column(name = "code_template", columnDefinition = "TEXT")
    private String codeTemplate;
    
    @Column(name = "constraints_info", columnDefinition = "TEXT")
    private String constraintsInfo;
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCase> testCases = new ArrayList<>();
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterviewQuestion> interviewQuestions = new ArrayList<>();
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    
    
    // Constructors
    public Question() {
        this.createdAt = LocalDateTime.now();
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public QuestionType getQuestionType() {
        return questionType;
    }
    
    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getSubCategory() {
        return subCategory;
    }
    
    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
    
    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }
    
    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
    
    public Integer getTimeLimitSeconds() {
        return timeLimitSeconds;
    }
    
    public void setTimeLimitSeconds(Integer timeLimitSeconds) {
        this.timeLimitSeconds = timeLimitSeconds;
    }
    
    public Integer getPoints() {
        return points;
    }
    
    public void setPoints(Integer points) {
        this.points = points;
    }
    
    public String getHints() {
        return hints;
    }
    
    public void setHints(String hints) {
        this.hints = hints;
    }
    
    public String getSolution() {
        return solution;
    }
    
    public void setSolution(String solution) {
        this.solution = solution;
    }
    
    public String getCodeTemplate() {
        return codeTemplate;
    }
    
    public void setCodeTemplate(String codeTemplate) {
        this.codeTemplate = codeTemplate;
    }
    
    public String getConstraintsInfo() {
        return constraintsInfo;
    }
    
    public void setConstraintsInfo(String constraintsInfo) {
        this.constraintsInfo = constraintsInfo;
    }
    
    public List<TestCase> getTestCases() {
        return testCases;
    }
    
    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<InterviewQuestion> getInterviewQuestions() {
        return interviewQuestions;
    }
    
    public void setInterviewQuestions(List<InterviewQuestion> interviewQuestions) {
        this.interviewQuestions = interviewQuestions;
    }
    
    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", type=" + questionType +
                ", difficulty=" + difficultyLevel +
                '}';
    }
}

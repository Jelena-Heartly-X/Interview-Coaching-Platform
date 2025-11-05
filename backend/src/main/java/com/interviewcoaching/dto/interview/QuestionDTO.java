package com.interviewcoaching.dto.interview;

import com.interviewcoaching.models.interview.Question;
import java.time.LocalDateTime;

public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String questionType;
    private String category;
    private String subCategory;
    private String difficultyLevel;
    private Integer timeLimitSeconds;
    private Integer points;
    private String hints;
    private String solution;
    private String codeTemplate;
    private String constraintsInfo;
    private LocalDateTime createdAt;
    
    // Constructor to create from Question entity
    public QuestionDTO(Question question) {
        this.id = question.getId();
        this.title = question.getTitle();
        this.description = question.getDescription();
        this.questionType = question.getQuestionType() != null ? 
            question.getQuestionType().toString() : null;
        this.category = question.getCategory();
        this.subCategory = question.getSubCategory();
        this.difficultyLevel = question.getDifficultyLevel() != null ? 
            question.getDifficultyLevel().toString() : null;
        this.timeLimitSeconds = question.getTimeLimitSeconds();
        this.points = question.getPoints();
        this.hints = question.getHints();
        this.solution = question.getSolution();
        this.codeTemplate = question.getCodeTemplate();
        this.constraintsInfo = question.getConstraintsInfo();
        this.createdAt = question.getCreatedAt();
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

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
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

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

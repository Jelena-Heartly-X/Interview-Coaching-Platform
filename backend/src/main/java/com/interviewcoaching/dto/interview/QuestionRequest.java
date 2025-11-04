package com.interviewcoaching.dto.interview;

import javax.validation.constraints.NotBlank;

public class QuestionRequest {
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotBlank(message = "Question type is required")
    private String questionType;
    
    @NotBlank(message = "Category is required")
    private String category;
    
    private String subCategory;
    
    @NotBlank(message = "Difficulty level is required")
    private String difficultyLevel;
    
    private Integer timeLimitSeconds;
    private Integer points;
    private String hints;
    private String solution;
    private String codeTemplate;
    private String constraintsInfo;
    
    // Constructors
    public QuestionRequest() {}
    
    // Getters and Setters
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
}

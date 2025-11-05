package com.interviewcoaching.models.interview;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

@Entity
@Table(name = "test_cases")
public class TestCase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
    
    @Column(name = "input_data", nullable = false, columnDefinition = "TEXT")
    private String inputData;
    
    @Column(name = "expected_output", nullable = false, columnDefinition = "TEXT")
    private String expectedOutput;
    
    @Column(name = "is_sample")
    private Boolean isSample = false;
    
    @Column(name = "is_hidden")
    private Boolean isHidden = false;
    
    @Column
    private Integer points = 1;
    
    @Column(columnDefinition = "TEXT")
    private String explanation;
    
    // Constructors
    public TestCase() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Question getQuestion() {
        return question;
    }
    
    public void setQuestion(Question question) {
        this.question = question;
    }
    
    public String getInputData() {
        return inputData;
    }
    
    public void setInputData(String inputData) {
        this.inputData = inputData;
    }
    
    public String getExpectedOutput() {
        return expectedOutput;
    }
    
    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }
    
    public Boolean getIsSample() {
        return isSample;
    }
    
    public void setIsSample(Boolean isSample) {
        this.isSample = isSample;
    }
    
    public Boolean getIsHidden() {
        return isHidden;
    }
    
    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }
    
    public Integer getPoints() {
        return points;
    }
    
    public void setPoints(Integer points) {
        this.points = points;
    }
    
    public String getExplanation() {
        return explanation;
    }
    
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}

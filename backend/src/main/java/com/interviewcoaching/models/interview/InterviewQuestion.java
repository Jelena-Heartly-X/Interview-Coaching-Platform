// InterviewQuestion.java
package com.interviewcoaching.models.interview;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "interview_questions")
public class InterviewQuestion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", nullable = false)
    private Interview interview;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
    
    @OneToOne(mappedBy = "interviewQuestion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private InterviewResponse response;
    
    @Column(name = "order_index")
    private Integer orderIndex;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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

    public Interview getInterview() {
        return interview;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public InterviewResponse getResponse() {
        return response;
    }

    public void setResponse(InterviewResponse response) {
        if (response == null) {
            if (this.response != null) {
                this.response.setInterviewQuestion(null);
            }
        } else {
            response.setInterviewQuestion(this);
        }
        this.response = response;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
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
    
    // Helper method to add response
    public void addResponse(InterviewResponse response) {
        response.setInterviewQuestion(this);
        this.response = response;
    }
    
    // Helper method to remove response
    public void removeResponse() {
        if (response != null) {
            response.setInterviewQuestion(null);
            this.response = null;
        }
    }
    
    @Override
    public String toString() {
        return "InterviewQuestion{" +
                "id=" + id +
                ", questionId=" + (question != null ? question.getId() : null) +
                ", orderIndex=" + orderIndex +
                '}';
    }
}
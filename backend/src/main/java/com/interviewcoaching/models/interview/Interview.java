// Interview.java
package com.interviewcoaching.models.interview;

import com.interviewcoaching.models.auth.User;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interviews")
public class Interview {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id")
    private InterviewSlot slot;
    
    @Column(length = 200)
    private String title;
    
    @Column(length = 100)
    private String topic;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", length = 20)
    private Question.DifficultyLevel difficultyLevel;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private InterviewStatus status = InterviewStatus.SCHEDULED;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "total_score")
    private Integer totalScore = 0;
    
    @Column(name = "max_score")
    private Integer maxScore = 0;
    
    @Column(columnDefinition = "TEXT")
    private String feedback;
    
    @Column(name = "question_count")
    private Integer questionCount;
    
    @OneToMany(mappedBy = "interview", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    private List<InterviewQuestion> interviewQuestions = new ArrayList<>();
    
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public InterviewSlot getSlot() {
        return slot;
    }

    public void setSlot(InterviewSlot slot) {
        this.slot = slot;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Question.DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(Question.DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public InterviewStatus getStatus() {
        return status;
    }

    public void setStatus(InterviewStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }
    
    public Integer getMaxScore() {
        return maxScore != null ? maxScore : 0;
    }
    
    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public List<InterviewQuestion> getInterviewQuestions() {
        return interviewQuestions;
    }

    public void setInterviewQuestions(List<InterviewQuestion> interviewQuestions) {
        this.interviewQuestions = interviewQuestions;
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
    
    // Helper methods
    public void startInterview() {
        this.status = InterviewStatus.IN_PROGRESS;
        this.startTime = LocalDateTime.now();
    }
    
    public void completeInterview() {
        this.status = InterviewStatus.COMPLETED;
        this.endTime = LocalDateTime.now();
        calculateTotalScore();
    }
    
    public void cancelInterview() {
        this.status = InterviewStatus.CANCELLED;
        this.endTime = LocalDateTime.now();
    }
    
    // Add a question to the interview
    public void addQuestion(Question question, int orderIndex) {
        InterviewQuestion interviewQuestion = new InterviewQuestion();
        interviewQuestion.setInterview(this);
        interviewQuestion.setQuestion(question);
        interviewQuestion.setOrderIndex(orderIndex);
        this.interviewQuestions.add(interviewQuestion);
    }
    
    // Remove a question from the interview
    public void removeQuestion(InterviewQuestion interviewQuestion) {
        interviewQuestions.remove(interviewQuestion);
        interviewQuestion.setInterview(null);
    }
    
    // Calculate total score based on all responses
    public void calculateTotalScore() {
        if (interviewQuestions == null || interviewQuestions.isEmpty()) {
            this.totalScore = 0;
            return;
        }
        
        int total = 0;
        int count = 0;
        
        for (InterviewQuestion iq : interviewQuestions) {
            if (iq.getResponse() != null && iq.getResponse().getScoreObtained() != null) {
                total += iq.getResponse().getScoreObtained();
                count++;
            }
        }
        
        this.totalScore = count > 0 ? total / count : 0;
    }
    
    // Get all responses for this interview
    public List<InterviewResponse> getResponses() {
        List<InterviewResponse> responses = new ArrayList<>();
        if (interviewQuestions != null) {
            for (InterviewQuestion iq : interviewQuestions) {
                if (iq.getResponse() != null) {
                    responses.add(iq.getResponse());
                }
            }
        }
        return responses;
    }
    
    @Override
    public String toString() {
        return "Interview{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", totalScore=" + totalScore +
                '}';
    }
}
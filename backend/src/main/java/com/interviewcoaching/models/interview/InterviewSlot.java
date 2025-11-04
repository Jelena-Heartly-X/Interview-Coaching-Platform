package com.interviewcoaching.models.interview;

import com.interviewcoaching.models.auth.User;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "interview_slots")
public class InterviewSlot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, length = 100)
    private String topic; // DSA, DBMS, WEB_DEV, OS, CN, OOP, SYSTEM_DESIGN
    
    @Column(name = "difficulty_level", nullable = false, length = 20)
    private String difficultyLevel; // BEGINNER, INTERMEDIATE, ADVANCED
    
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes = 60;
    
    @Column(name = "scheduled_date_time")
    private LocalDateTime scheduledDateTime;
    
    @Column(name = "max_participants")
    private Integer maxParticipants = 1;
    
    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private User mentor;
    
    @Column(length = 20)
    private String status = "AVAILABLE"; // AVAILABLE, BOOKED, COMPLETED, CANCELLED
    
    @Column(name = "booked", nullable = false)
    private boolean booked = false;
    
    @ManyToOne
    @JoinColumn(name = "booked_by")
    private User bookedBy;
    
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

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public LocalDateTime getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public User getMentor() {
        return mentor;
    }

    public void setMentor(User mentor) {
        this.mentor = mentor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public User getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(User bookedBy) {
        this.bookedBy = bookedBy;
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
        return "InterviewSlot{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", topic='" + topic + '\'' +
                ", difficultyLevel='" + difficultyLevel + '\'' +
                ", scheduledDateTime=" + scheduledDateTime +
                ", status='" + status + '\'' +
                ", booked=" + booked +
                '}';
    }
}
package com.interviewcoaching.models.interview;

public enum InterviewStatus {
    SCHEDULED,      // Interview is scheduled but not started
    IN_PROGRESS,    // Interview is currently in progress
    COMPLETED,      // Interview has been completed
    CANCELLED,      // Interview was cancelled
    EXPIRED,        // Interview slot expired without being taken
    PENDING_REVIEW  // Interview completed but needs review
}
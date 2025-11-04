package com.interviewcoaching.repositories.interview;

import com.interviewcoaching.models.interview.InterviewEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewEvaluationRepository extends JpaRepository<InterviewEvaluation, Long> {
    
    // Find evaluation by interview
    InterviewEvaluation findByInterviewId(Long interviewId);
}

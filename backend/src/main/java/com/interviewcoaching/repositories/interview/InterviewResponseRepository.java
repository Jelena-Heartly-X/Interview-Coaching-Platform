package com.interviewcoaching.repositories.interview;

import com.interviewcoaching.models.interview.InterviewResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewResponseRepository extends JpaRepository<InterviewResponse, Long> {
    
    // Find responses by interview
    @Query("SELECT r FROM InterviewResponse r WHERE r.interviewQuestion.interview.id = :interviewId")
    List<InterviewResponse> findByInterviewId(@Param("interviewId") Long interviewId);
    
    // Find responses by question
    @Query("SELECT r FROM InterviewResponse r WHERE r.interviewQuestion.question.id = :questionId")
    List<InterviewResponse> findByQuestionId(@Param("questionId") Long questionId);
    
    // Find response by interview question
    InterviewResponse findByInterviewQuestionId(Long interviewQuestionId);
}

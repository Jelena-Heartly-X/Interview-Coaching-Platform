// InterviewQuestionRepository.java
package com.interviewcoaching.repositories.interview;

import com.interviewcoaching.models.interview.InterviewQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {
    
    /**
     * Find all interview questions for a specific interview
     */
    List<InterviewQuestion> findByInterviewId(Long interviewId);
    
    /**
     * Find a specific interview question by interview ID and question ID
     */
    @Query("SELECT iq FROM InterviewQuestion iq " +
           "WHERE iq.interview.id = :interviewId AND iq.question.id = :questionId")
    Optional<InterviewQuestion> findByInterviewIdAndQuestionId(
        @Param("interviewId") Long interviewId, 
        @Param("questionId") Long questionId
    );
    
    /**
     * Find all interview questions with their responses for a specific interview
     */
    @Query("SELECT iq FROM InterviewQuestion iq " +
           "LEFT JOIN FETCH iq.response " +
           "WHERE iq.interview.id = :interviewId " +
           "ORDER BY iq.orderIndex")
    List<InterviewQuestion> findWithResponsesByInterviewId(@Param("interviewId") Long interviewId);
    
    /**
     * Check if a question is already added to an interview
     */
    boolean existsByInterviewIdAndQuestionId(Long interviewId, Long questionId);
    
    /**
     * Count questions in an interview
     */
    long countByInterviewId(Long interviewId);
}
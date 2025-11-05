// InterviewRepository.java
package com.interviewcoaching.repositories.interview;

import com.interviewcoaching.models.auth.User;
import com.interviewcoaching.models.interview.Interview;
import com.interviewcoaching.models.interview.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    
    // Existing methods...
    List<Interview> findByUserId(Long userId);
    List<Interview> findByStatus(String status);
    List<Interview> findBySlotId(Long slotId);
    
    // New methods needed by InterviewService
    List<Interview> findByUserIdOrderByStartTimeDesc(Long userId);
    Optional<Interview> findByIdAndUserId(Long id, Long userId);
    
    @Query("SELECT i FROM Interview i WHERE i.status = 'COMPLETED' AND i.user.id = :userId ORDER BY i.endTime DESC")
    List<Interview> findCompletedInterviewsByUserId(@Param("userId") Long userId);
    
    // Find by user ordered by created date
    List<Interview> findByUserOrderByCreatedAtDesc(User user);
    
    // Find by user and status after a certain date
    List<Interview> findByUserAndStatusAndCreatedAtAfter(User user, InterviewStatus status, LocalDateTime createdAt);
}
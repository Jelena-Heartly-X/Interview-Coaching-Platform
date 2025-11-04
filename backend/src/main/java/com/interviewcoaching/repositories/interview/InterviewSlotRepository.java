// InterviewSlotRepository.java
package com.interviewcoaching.repositories.interview;

import com.interviewcoaching.models.interview.InterviewSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewSlotRepository extends JpaRepository<InterviewSlot, Long> {
    
    // Existing methods...
    List<InterviewSlot> findByStatus(String status);
    List<InterviewSlot> findByTopicAndDifficultyLevel(String topic, String difficultyLevel);
    
    @Query("SELECT s FROM InterviewSlot s WHERE s.scheduledDateTime > :now AND s.status = 'AVAILABLE' ORDER BY s.scheduledDateTime ASC")
    List<InterviewSlot> findUpcomingAvailableSlots(@Param("now") LocalDateTime now);
    
    // Add this method to match service usage
    Optional<InterviewSlot> findByIdAndBookedFalse(Long id);
    
    // Other existing methods...
    @Modifying
    @Transactional
    @Query("UPDATE InterviewSlot s SET s.booked = true, s.bookedBy.id = :userId, s.updatedAt = CURRENT_TIMESTAMP WHERE s.id = :slotId AND s.booked = false")
    int bookSlot(@Param("slotId") Long slotId, @Param("userId") Long userId);
    
    // Add other repository methods as needed
    @Query("SELECT s FROM InterviewSlot s WHERE s.booked = false AND s.scheduledDateTime > :dateTime")
    List<InterviewSlot> findAvailableSlotsAfterDateTime(@Param("dateTime") LocalDateTime dateTime);
    List<InterviewSlot> findByBookedBy_Id(Long userId);
    
    /**
     * Find all interview slots for a specific topic, ordered by scheduled date/time in ascending order
     * @param topic The topic to search for
     * @return List of interview slots matching the topic, ordered by scheduled date/time
     */
    List<InterviewSlot> findByTopicOrderByScheduledDateTimeAsc(String topic);
    
    /**
     * Find all interview slots for a specific mentor
     * @param mentorId The ID of the mentor
     * @return List of interview slots for the specified mentor
     */
    List<InterviewSlot> findByMentorId(Long mentorId);
}

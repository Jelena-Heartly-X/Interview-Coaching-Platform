package com.interviewcoaching.repositories.interview;

import com.interviewcoaching.models.auth.User;
import com.interviewcoaching.models.interview.UserSkillsAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSkillsAnalyticsRepository extends JpaRepository<UserSkillsAnalytics, Long> {
    
    // Find analytics by user and topic
    Optional<UserSkillsAnalytics> findByUserIdAndTopic(Long userId, String topic);
    
    // Find all analytics for a user
    List<UserSkillsAnalytics> findByUserId(Long userId);
    
    // Find analytics by topic
    List<UserSkillsAnalytics> findByTopic(String topic);
    
    // Get top skills for a user
    @Query("SELECT s FROM UserSkillsAnalytics s WHERE s.user.id = :userId ORDER BY s.accuracyPercentage DESC")
    List<UserSkillsAnalytics> findTopSkillsByUserId(@Param("userId") Long userId);
    
    // Find weak areas for improvement
    @Query("SELECT s FROM UserSkillsAnalytics s WHERE s.user.id = :userId AND s.accuracyPercentage < 60 ORDER BY s.totalAttempts DESC")
    List<UserSkillsAnalytics> findWeakAreasByUserId(@Param("userId") Long userId);
    
    // Find by user and topic (using User entity)
    Optional<UserSkillsAnalytics> findByUserAndTopic(User user, String topic);
    
    // Find all by user (using User entity)
    List<UserSkillsAnalytics> findByUser(User user);
}

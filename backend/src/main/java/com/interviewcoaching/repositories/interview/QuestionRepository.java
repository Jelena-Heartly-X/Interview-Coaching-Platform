package com.interviewcoaching.repositories.interview;

import com.interviewcoaching.models.interview.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    // Find questions by category and difficulty with pagination
    List<Question> findByCategoryAndDifficultyLevel(String category, String difficultyLevel, org.springframework.data.domain.Pageable pageable);
    
    // Find questions by type
    List<Question> findByQuestionType(String questionType);
    
    // Find questions by subcategory
    List<Question> findBySubCategory(String subCategory);
    
    // Find questions by difficulty level with pagination
    List<Question> findByDifficultyLevel(String difficultyLevel, org.springframework.data.domain.Pageable pageable);
    
    // Custom query to find random questions by category
    @Query(value = "SELECT * FROM questions WHERE category = :category ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Question> findRandomQuestionsByCategory(@Param("category") String category, @Param("limit") int limit);
    
    // Find random questions by category and difficulty
    @Query(value = "SELECT * FROM questions WHERE category = :category AND difficulty_level = :difficultyLevel ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Question> findRandomQuestionsByCategoryAndDifficulty(@Param("category") String category, @Param("difficultyLevel") String difficultyLevel, @Param("limit") int limit);
    
    // Find random questions by difficulty
    @Query(value = "SELECT * FROM questions WHERE difficulty_level = :difficultyLevel ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Question> findRandomQuestionsByDifficulty(@Param("difficultyLevel") String difficultyLevel, @Param("limit") int limit);
    
    // Find questions by category
    List<Question> findByCategory(String category);
    
    // Count questions by category
    long countByCategory(String category);
    
    // Count questions by difficulty
    long countByDifficultyLevel(Question.DifficultyLevel difficultyLevel);
}

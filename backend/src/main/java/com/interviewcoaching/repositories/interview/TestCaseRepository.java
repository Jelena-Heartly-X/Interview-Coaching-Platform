package com.interviewcoaching.repositories.interview;

import com.interviewcoaching.models.interview.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    
    // Find test cases by question
    List<TestCase> findByQuestionId(Long questionId);
    
    // Find sample test cases
    List<TestCase> findByQuestionIdAndIsSampleTrue(Long questionId);
    
    // Find hidden test cases
    List<TestCase> findByQuestionIdAndIsHiddenTrue(Long questionId);
}

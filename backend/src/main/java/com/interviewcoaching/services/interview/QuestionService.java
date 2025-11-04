package com.interviewcoaching.services.interview;

import com.interviewcoaching.models.interview.Question;
import com.interviewcoaching.models.interview.TestCase;
import com.interviewcoaching.repositories.interview.QuestionRepository;
import com.interviewcoaching.repositories.interview.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private TestCaseRepository testCaseRepository;
    
    /**
     * Create a new question
     */
    public Question createQuestion(Question question) {
        question.setCreatedAt(LocalDateTime.now());
        return questionRepository.save(question);
    }
    
    /**
     * Get question by ID
     */
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }
    
    /**
     * Get all questions
     */
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }
    
    /**
     * Get questions by category and difficulty with pagination
     * @param category The category of questions to retrieve
     * @param difficulty The difficulty level (BEGINNER, INTERMEDIATE, ADVANCED)
     * @param page The page number (0-based)
     * @param size The number of questions per page
     * @return List of questions for the specified page
     */
    public List<Question> getQuestionsByCategoryAndDifficulty(String category, String difficulty, int page, int size) {
        return questionRepository.findByCategoryAndDifficultyLevel(
            category, 
            difficulty, 
            org.springframework.data.domain.PageRequest.of(page, size)
        );
    }
    
    /**
     * Get all questions by category and difficulty (without pagination)
     * Note: Use with caution as this could return a large number of records
     * This method uses a large page size to effectively get all records
     */
    public List<Question> getAllQuestionsByCategoryAndDifficulty(String category, String difficulty) {
        // Use a large page size to get all records in one page
        int largePageSize = 1000; // Adjust this based on your needs
        return questionRepository.findByCategoryAndDifficultyLevel(
            category, 
            difficulty, 
            org.springframework.data.domain.PageRequest.of(0, largePageSize)
        );
    }
    
    /**
     * Get questions by type
     */
    public List<Question> getQuestionsByType(String type) {
        return questionRepository.findByQuestionType(type);
    }
    
    /**
     * Get questions by subcategory
     */
    public List<Question> getQuestionsBySubCategory(String subCategory) {
        return questionRepository.findBySubCategory(subCategory);
    }
    
    /**
     * Get random questions for an interview
     */
    public List<Question> getRandomQuestions(String category, int count) {
        return questionRepository.findRandomQuestionsByCategory(category, count);
    }
    
    /**
     * Update a question
     */
    public Question updateQuestion(Long id, Question updatedQuestion) {
        Optional<Question> existingQuestion = questionRepository.findById(id);
        if (existingQuestion.isPresent()) {
            Question question = existingQuestion.get();
            question.setTitle(updatedQuestion.getTitle());
            question.setDescription(updatedQuestion.getDescription());
            question.setQuestionType(updatedQuestion.getQuestionType());
            question.setCategory(updatedQuestion.getCategory());
            question.setSubCategory(updatedQuestion.getSubCategory());
            question.setDifficultyLevel(updatedQuestion.getDifficultyLevel());
            question.setTimeLimitSeconds(updatedQuestion.getTimeLimitSeconds());
            question.setPoints(updatedQuestion.getPoints());
            question.setHints(updatedQuestion.getHints());
            question.setSolution(updatedQuestion.getSolution());
            question.setCodeTemplate(updatedQuestion.getCodeTemplate());
            question.setConstraintsInfo(updatedQuestion.getConstraintsInfo());
            return questionRepository.save(question);
        }
        throw new RuntimeException("Question not found with ID: " + id);
    }
    
    /**
     * Delete a question
     */
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
    
    /**
     * Add test case to a question
     */
    public TestCase addTestCase(Long questionId, TestCase testCase) {
        Optional<Question> questionOpt = questionRepository.findById(questionId);
        if (questionOpt.isPresent()) {
            testCase.setQuestion(questionOpt.get());
            return testCaseRepository.save(testCase);
        }
        throw new RuntimeException("Question not found with ID: " + questionId);
    }
    
    /**
     * Get test cases for a question
     */
    public List<TestCase> getTestCasesByQuestion(Long questionId) {
        return testCaseRepository.findByQuestionId(questionId);
    }
    
    /**
     * Get sample test cases
     */
    public List<TestCase> getSampleTestCases(Long questionId) {
        return testCaseRepository.findByQuestionIdAndIsSampleTrue(questionId);
    }
    
    /**
     * Get hidden test cases
     */
    public List<TestCase> getHiddenTestCases(Long questionId) {
        return testCaseRepository.findByQuestionIdAndIsHiddenTrue(questionId);
    }
    
    /**
     * Delete test case
     */
    public void deleteTestCase(Long testCaseId) {
        testCaseRepository.deleteById(testCaseId);
    }
}

package com.interviewcoaching.controllers.interview;

import com.interviewcoaching.dto.interview.QuestionRequest;
import com.interviewcoaching.models.interview.Question;
import com.interviewcoaching.models.interview.TestCase;
import com.interviewcoaching.services.interview.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "http://localhost:3000")
public class QuestionController {
    
    @Autowired
    private QuestionService questionService;
    
    /**
     * Get all questions
     */
    @GetMapping
    public ResponseEntity<?> getAllQuestions() {
        List<Question> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }
    
    /**
     * Get question by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestion(@PathVariable Long id) {
        try {
            Question question = questionService.getQuestionById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
            return ResponseEntity.ok(question);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    /**
     * Get questions by category and difficulty
     */
    @GetMapping("/category/{category}/difficulty/{difficulty}")
    public ResponseEntity<?> getQuestionsByCategoryAndDifficulty(
            @PathVariable String category,
            @PathVariable String difficulty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Question> questions = questionService.getQuestionsByCategoryAndDifficulty(category, difficulty, page, size);
        return ResponseEntity.ok(questions);
    }
    
    /**
     * Get all questions by category and difficulty without pagination
     * Note: Use with caution as this could return a large number of records
     */
    @GetMapping("/category/{category}/difficulty/{difficulty}/all")
    public ResponseEntity<?> getAllQuestionsByCategoryAndDifficulty(
            @PathVariable String category,
            @PathVariable String difficulty) {
        List<Question> questions = questionService.getAllQuestionsByCategoryAndDifficulty(category, difficulty);
        return ResponseEntity.ok(questions);
    }
    
    /**
     * Get questions by filter
     */
    @GetMapping("/filter")
    public ResponseEntity<?> getQuestionsByFilter(
            @RequestParam String category,
            @RequestParam String difficulty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Question> questions = questionService.getQuestionsByCategoryAndDifficulty(category, difficulty, page, size);
        return ResponseEntity.ok(questions);
    }
    
    /**
     * Get questions by type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<?> getQuestionsByType(@PathVariable String type) {
        List<Question> questions = questionService.getQuestionsByType(type);
        return ResponseEntity.ok(questions);
    }
    
    /**
     * Create a new question
     */
    @PostMapping
    public ResponseEntity<?> createQuestion(@Valid @RequestBody QuestionRequest request) {
        try {
            Question question = new Question();
            question.setTitle(request.getTitle());
            question.setDescription(request.getDescription());
            question.setQuestionType(Question.QuestionType.valueOf(request.getQuestionType().toUpperCase()));
            question.setCategory(request.getCategory());
            question.setSubCategory(request.getSubCategory());
            question.setDifficultyLevel(Question.DifficultyLevel.valueOf(request.getDifficultyLevel().toUpperCase()));
            question.setTimeLimitSeconds(request.getTimeLimitSeconds());
            question.setPoints(request.getPoints());
            question.setHints(request.getHints());
            question.setSolution(request.getSolution());
            question.setCodeTemplate(request.getCodeTemplate());
            question.setConstraintsInfo(request.getConstraintsInfo());
            
            Question savedQuestion = questionService.createQuestion(question);
            
            Map<String, Object> response = new HashMap<>();
            response.put("question", savedQuestion);
            response.put("message", "Question created successfully");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    /**
     * Update a question
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuestion(
            @PathVariable Long id,
            @Valid @RequestBody QuestionRequest request) {
        try {
            Question updatedQuestion = new Question();
            updatedQuestion.setTitle(request.getTitle());
            updatedQuestion.setDescription(request.getDescription());
            updatedQuestion.setQuestionType(Question.QuestionType.valueOf(request.getQuestionType().toUpperCase()));
            updatedQuestion.setCategory(request.getCategory());
            updatedQuestion.setSubCategory(request.getSubCategory());
            updatedQuestion.setDifficultyLevel(Question.DifficultyLevel.valueOf(request.getDifficultyLevel().toUpperCase()));
            updatedQuestion.setTimeLimitSeconds(request.getTimeLimitSeconds());
            updatedQuestion.setPoints(request.getPoints());
            updatedQuestion.setHints(request.getHints());
            updatedQuestion.setSolution(request.getSolution());
            updatedQuestion.setCodeTemplate(request.getCodeTemplate());
            updatedQuestion.setConstraintsInfo(request.getConstraintsInfo());
            
            Question question = questionService.updateQuestion(id, updatedQuestion);
            
            Map<String, Object> response = new HashMap<>();
            response.put("question", question);
            response.put("message", "Question updated successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    /**
     * Delete a question
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        try {
            questionService.deleteQuestion(id);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Question deleted successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    /**
     * Get test cases for a question
     */
    @GetMapping("/{questionId}/test-cases")
    public ResponseEntity<?> getTestCases(@PathVariable Long questionId) {
        List<TestCase> testCases = questionService.getTestCasesByQuestion(questionId);
        return ResponseEntity.ok(testCases);
    }
    
    /**
     * Add test case to a question
     */
    @PostMapping("/{questionId}/test-cases")
    public ResponseEntity<?> addTestCase(
            @PathVariable Long questionId,
            @RequestBody TestCase testCase) {
        try {
            TestCase savedTestCase = questionService.addTestCase(questionId, testCase);
            
            Map<String, Object> response = new HashMap<>();
            response.put("testCase", savedTestCase);
            response.put("message", "Test case added successfully");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}

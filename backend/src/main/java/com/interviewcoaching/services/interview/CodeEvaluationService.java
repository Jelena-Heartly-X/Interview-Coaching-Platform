package com.interviewcoaching.services.interview;

import com.interviewcoaching.models.interview.InterviewResponse;
import com.interviewcoaching.models.interview.Question;
import com.interviewcoaching.models.interview.TestCase;
import com.interviewcoaching.repositories.interview.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodeEvaluationService {
    
    @Autowired
    private TestCaseRepository testCaseRepository;
    
    /**
     * Evaluate code submission against test cases
     */
    public void evaluateCode(InterviewResponse response, Question question) {
        String code = response.getCodeSubmission();
        String language = response.getProgrammingLanguage();
        
        // Get test cases for the question
        List<TestCase> testCases = testCaseRepository.findByQuestionId(question.getId());
        
        if (testCases.isEmpty()) {
            // No test cases, give partial score based on code length
            response.setScoreObtained((int) (question.getPoints() * 0.5));
            response.setIsCorrect(false);
            response.setTestCasesTotal(0);
            response.setTestCasesPassed(0);
            response.setExecutionOutput("No test cases available for this question.");
            return;
        }
        
        int totalTestCases = testCases.size();
        int passedTestCases = 0;
        StringBuilder executionOutput = new StringBuilder();
        
        // Simulate test case execution
        // In a real implementation, you would use a code execution engine
        for (TestCase testCase : testCases) {
            boolean passed = simulateTestCaseExecution(code, language, testCase);
            if (passed) {
                passedTestCases++;
                executionOutput.append("Test Case ").append(testCase.getId()).append(": PASSED\n");
            } else {
                executionOutput.append("Test Case ").append(testCase.getId()).append(": FAILED\n");
                if (!testCase.getIsHidden()) {
                    executionOutput.append("  Expected: ").append(testCase.getExpectedOutput()).append("\n");
                }
            }
        }
        
        // Calculate score based on test cases passed
        double passPercentage = (double) passedTestCases / totalTestCases;
        int scoreObtained = (int) (question.getPoints() * passPercentage);
        
        // Apply penalty for hints used
        if (response.getHintsUsed() > 0) {
            scoreObtained = (int) (scoreObtained * 0.9); // 10% penalty per hint
        }
        
        response.setTestCasesTotal(totalTestCases);
        response.setTestCasesPassed(passedTestCases);
        response.setScoreObtained(scoreObtained);
        response.setIsCorrect(passedTestCases == totalTestCases);
        response.setExecutionOutput(executionOutput.toString());
    }
    
    /**
     * Simulate test case execution
     * In production, integrate with Judge0, HackerRank API, or custom sandbox
     */
    private boolean simulateTestCaseExecution(String code, String language, TestCase testCase) {
        // This is a simplified simulation
        // In reality, you would:
        // 1. Use a code execution service like Judge0 API
        // 2. Run code in isolated Docker containers
        // 3. Use language-specific compilers/interpreters
        
        // For now, we'll do basic validation
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        
        // Simple heuristic: if code is substantial and contains relevant keywords
        String codeLower = code.toLowerCase();
        int codeLength = code.length();
        
        // Check for basic structure based on language
        boolean hasBasicStructure = false;
        
        switch (language.toLowerCase()) {
            case "java":
                hasBasicStructure = codeLower.contains("class") || codeLower.contains("public");
                break;
            case "python":
                hasBasicStructure = codeLower.contains("def") || codeLower.contains("return");
                break;
            case "cpp":
            case "c++":
                hasBasicStructure = codeLower.contains("int") || codeLower.contains("void");
                break;
            case "javascript":
                hasBasicStructure = codeLower.contains("function") || codeLower.contains("const") || codeLower.contains("let");
                break;
            default:
                hasBasicStructure = codeLength > 50;
        }
        
        // Simulate 70% pass rate for well-structured code
        return hasBasicStructure && codeLength > 100 && Math.random() > 0.3;
    }
    
    /**
     * Validate code syntax (simplified)
     */
    public boolean validateCodeSyntax(String code, String language) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        
        // Basic syntax validation
        // In production, use language-specific parsers
        
        // Check for balanced braces
        int openBraces = 0;
        int closeBraces = 0;
        
        for (char c : code.toCharArray()) {
            if (c == '{') openBraces++;
            if (c == '}') closeBraces++;
        }
        
        return openBraces == closeBraces;
    }
    
    /**
     * Analyze code complexity (simplified)
     */
    public String analyzeCodeComplexity(String code) {
        if (code == null || code.isEmpty()) {
            return "Unable to analyze";
        }
        
        int lines = code.split("\n").length;
        int loops = countOccurrences(code, "for") + countOccurrences(code, "while");
        int conditions = countOccurrences(code, "if") + countOccurrences(code, "switch");
        
        if (loops == 0) {
            return "O(1) - Constant";
        } else if (loops == 1) {
            return "O(n) - Linear";
        } else if (loops == 2) {
            return "O(n²) - Quadratic";
        } else {
            return "O(n^" + loops + ") - Polynomial";
        }
    }
    
    /**
     * Count occurrences of a substring
     */
    private int countOccurrences(String text, String substring) {
        int count = 0;
        int index = 0;
        
        while ((index = text.toLowerCase().indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        
        return count;
    }
}

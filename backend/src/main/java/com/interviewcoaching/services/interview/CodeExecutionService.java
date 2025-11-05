package com.interviewcoaching.services.interview;

import com.interviewcoaching.models.interview.TestCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CodeExecutionService {
    
    @Value("${judge0.api.url:https://judge0-ce.p.rapidapi.com}")
    private String judge0Url;
    
    @Value("${judge0.api.key:}")
    private String apiKey;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    /**
     * Language ID mappings for Judge0
     */
    private static final Map<String, Integer> LANGUAGE_IDS = Map.of(
        "java", 62,
        "python", 71,
        "cpp", 54,
        "c", 50,
        "javascript", 63,
        "python3", 71
    );
    
    /**
     * Execute code against test cases
     */
    public ExecutionResult executeCode(String code, String language, List<TestCase> testCases) {
        ExecutionResult result = new ExecutionResult();
        result.setTotalTestCases(testCases.size());
        result.setPassedTestCases(0);
        result.setExecutionDetails(new ArrayList<>());
        
        // If Judge0 API key is not configured, use mock execution
        if (apiKey == null || apiKey.isEmpty()) {
            return mockExecution(code, testCases);
        }
        
        // Execute code for each test case
        for (TestCase testCase : testCases) {
            TestCaseResult tcResult = executeTestCase(code, language, testCase);
            result.getExecutionDetails().add(tcResult);
            
            if (tcResult.isPassed()) {
                result.setPassedTestCases(result.getPassedTestCases() + 1);
            }
        }
        
        // Calculate score (0-100)
        result.setScore(calculateScore(result));
        result.setAllPassed(result.getPassedTestCases() == result.getTotalTestCases());
        
        return result;
    }
    
    /**
     * Execute code for a single test case
     */
    private TestCaseResult executeTestCase(String code, String language, TestCase testCase) {
        TestCaseResult result = new TestCaseResult();
        result.setTestCaseId(testCase.getId());
        result.setInput(testCase.getInputData());
        result.setExpectedOutput(testCase.getExpectedOutput());
        
        try {
            // Prepare submission
            Map<String, Object> submission = new HashMap<>();
            submission.put("source_code", Base64.getEncoder().encodeToString(code.getBytes()));
            submission.put("language_id", LANGUAGE_IDS.getOrDefault(language.toLowerCase(), 62));
            submission.put("stdin", Base64.getEncoder().encodeToString(testCase.getInputData().getBytes()));
            
            // Submit to Judge0
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-RapidAPI-Key", apiKey);
            headers.set("X-RapidAPI-Host", "judge0-ce.p.rapidapi.com");
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(submission, headers);
            @SuppressWarnings("rawtypes")
            ResponseEntity<Map> response = restTemplate.postForEntity(
                judge0Url + "/submissions?base64_encoded=true&wait=true",
                request,
                Map.class
            );
            
            // Parse results
            if (response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> responseBody = response.getBody();
                
                String stdout = decodeBase64((String) responseBody.get("stdout"));
                String stderr = decodeBase64((String) responseBody.get("stderr"));
                String compileOutput = decodeBase64((String) responseBody.get("compile_output"));
                
                result.setActualOutput(stdout != null ? stdout : stderr);
                result.setError(stderr);
                result.setCompileError(compileOutput);
                result.setExecutionTime(getDoubleValue(responseBody.get("time")));
                result.setMemoryUsed(getDoubleValue(responseBody.get("memory")));
                
                // Check if passed
                boolean passed = stdout != null && 
                                stdout.trim().equals(testCase.getExpectedOutput().trim());
                result.setPassed(passed);
            }
            
        } catch (Exception e) {
            result.setPassed(false);
            result.setError("Execution error: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Mock execution for testing without Judge0 API
     */
    private ExecutionResult mockExecution(String code, List<TestCase> testCases) {
        ExecutionResult result = new ExecutionResult();
        result.setTotalTestCases(testCases.size());
        result.setExecutionDetails(new ArrayList<>());
        
        int passed = 0;
        for (TestCase tc : testCases) {
            TestCaseResult tcResult = new TestCaseResult();
            tcResult.setTestCaseId(tc.getId());
            tcResult.setInput(tc.getInputData());
            tcResult.setExpectedOutput(tc.getExpectedOutput());
            
            // Mock: randomly pass 70% of test cases
            boolean passes = Math.random() < 0.7;
            tcResult.setPassed(passes);
            tcResult.setActualOutput(passes ? tc.getExpectedOutput() : "Wrong output");
            tcResult.setExecutionTime(Math.random() * 100);
            tcResult.setMemoryUsed(Math.random() * 1024);
            
            if (passes) passed++;
            
            result.getExecutionDetails().add(tcResult);
        }
        
        result.setPassedTestCases(passed);
        result.setScore(calculateScore(result));
        result.setAllPassed(passed == testCases.size());
        
        return result;
    }
    
    /**
     * Calculate score based on passed test cases
     */
    private int calculateScore(ExecutionResult result) {
        if (result.getTotalTestCases() == 0) return 0;
        return (result.getPassedTestCases() * 100) / result.getTotalTestCases();
    }
    
    /**
     * Decode base64 string
     */
    private String decodeBase64(String encoded) {
        if (encoded == null) return null;
        try {
            return new String(Base64.getDecoder().decode(encoded));
        } catch (Exception e) {
            return encoded;
        }
    }
    
    /**
     * Safely get double value from object
     */
    private Double getDoubleValue(Object value) {
        if (value == null) return 0.0;
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    /**
     * Execution result class
     */
    public static class ExecutionResult {
        private int totalTestCases;
        private int passedTestCases;
        private int score;
        private boolean allPassed;
        private List<TestCaseResult> executionDetails;
        
        // Getters and Setters
        public int getTotalTestCases() { return totalTestCases; }
        public void setTotalTestCases(int totalTestCases) { this.totalTestCases = totalTestCases; }
        
        public int getPassedTestCases() { return passedTestCases; }
        public void setPassedTestCases(int passedTestCases) { this.passedTestCases = passedTestCases; }
        
        public int getScore() { return score; }
        public void setScore(int score) { this.score = score; }
        
        public boolean isAllPassed() { return allPassed; }
        public void setAllPassed(boolean allPassed) { this.allPassed = allPassed; }
        
        public List<TestCaseResult> getExecutionDetails() { return executionDetails; }
        public void setExecutionDetails(List<TestCaseResult> executionDetails) { this.executionDetails = executionDetails; }
    }
    
    /**
     * Test case result class
     */
    public static class TestCaseResult {
        private Long testCaseId;
        private String input;
        private String expectedOutput;
        private String actualOutput;
        private boolean passed;
        private String error;
        private String compileError;
        private Double executionTime;
        private Double memoryUsed;
        
        // Getters and Setters
        public Long getTestCaseId() { return testCaseId; }
        public void setTestCaseId(Long testCaseId) { this.testCaseId = testCaseId; }
        
        public String getInput() { return input; }
        public void setInput(String input) { this.input = input; }
        
        public String getExpectedOutput() { return expectedOutput; }
        public void setExpectedOutput(String expectedOutput) { this.expectedOutput = expectedOutput; }
        
        public String getActualOutput() { return actualOutput; }
        public void setActualOutput(String actualOutput) { this.actualOutput = actualOutput; }
        
        public boolean isPassed() { return passed; }
        public void setPassed(boolean passed) { this.passed = passed; }
        
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        
        public String getCompileError() { return compileError; }
        public void setCompileError(String compileError) { this.compileError = compileError; }
        
        public Double getExecutionTime() { return executionTime; }
        public void setExecutionTime(Double executionTime) { this.executionTime = executionTime; }
        
        public Double getMemoryUsed() { return memoryUsed; }
        public void setMemoryUsed(Double memoryUsed) { this.memoryUsed = memoryUsed; }
    }
}

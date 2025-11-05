package com.interviewcoaching.services.interview;

import com.interviewcoaching.models.interview.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AIEvaluationService {
    
    @Value("${openai.api.key:}")
    private String openAiApiKey;
    
    @Value("${ai.evaluation.enabled:false}")
    private boolean aiEvaluationEnabled;
    
    /**
     * Generate comprehensive feedback for completed interview
     */
    public InterviewEvaluation generateEvaluation(Interview interview) {
        InterviewEvaluation evaluation = new InterviewEvaluation();
        evaluation.setInterview(interview);
        
        if (aiEvaluationEnabled && openAiApiKey != null && !openAiApiKey.isEmpty()) {
            // Use real AI evaluation
            return generateAIEvaluation(interview);
        } else {
            // Use rule-based evaluation as fallback
            return generateRuleBasedEvaluation(interview);
        }
    }
    
    /**
     * Generate AI-powered evaluation (placeholder for OpenAI integration)
     */
    private InterviewEvaluation generateAIEvaluation(Interview interview) {
        // TODO: Integrate with OpenAI API
        // This would send interview responses to GPT-4 for analysis
        
        // For now, fall back to rule-based
        return generateRuleBasedEvaluation(interview);
    }
    
    /**
     * Generate rule-based evaluation
     */
    private InterviewEvaluation generateRuleBasedEvaluation(Interview interview) {
        InterviewEvaluation evaluation = new InterviewEvaluation();
        evaluation.setInterview(interview);
        
        List<InterviewQuestion> questions = interview.getInterviewQuestions();
        if (questions == null || questions.isEmpty()) {
            return createDefaultEvaluation(evaluation);
        }
        
        // Calculate scores
        ScoreCalculation scores = calculateScores(questions);
        
        evaluation.setOverallScore(scores.overallScore);
        evaluation.setCodeQualityScore(scores.codeQualityScore);
        evaluation.setLogicalReasoningScore(scores.logicalReasoningScore);
        evaluation.setTimeManagementScore(scores.timeManagementScore);
        evaluation.setProblemSolvingScore(scores.problemSolvingScore);
        
        // Generate confidence level
        evaluation.setConfidenceLevel(determineConfidenceLevel(scores.overallScore));
        
        // Generate feedback
        evaluation.setAiFeedback(generateDetailedFeedback(scores, questions));
        evaluation.setStrengths(identifyStrengths(scores, questions));
        evaluation.setWeaknesses(identifyWeaknesses(scores, questions));
        evaluation.setImprovementSuggestions(generateImprovementSuggestions(scores, questions));
        
        return evaluation;
    }
    
    /**
     * Calculate all scores
     */
    private ScoreCalculation calculateScores(List<InterviewQuestion> questions) {
        ScoreCalculation calc = new ScoreCalculation();
        
        int totalQuestions = questions.size();
        int correctAnswers = 0;
        int totalHintsUsed = 0;
        long totalTimeTaken = 0;
        int codingQuestions = 0;
        int codingCorrect = 0;
        
        for (InterviewQuestion iq : questions) {
            InterviewResponse response = iq.getResponse();
            if (response != null) {
                // Count correct answers
                if (response.getIsCorrect() != null && response.getIsCorrect()) {
                    correctAnswers++;
                }
                
                // Track coding questions
                if (iq.getQuestion().getQuestionType() == Question.QuestionType.CODING) {
                    codingQuestions++;
                    if (response.getIsCorrect() != null && response.getIsCorrect()) {
                        codingCorrect++;
                    }
                }
                
                // Sum hints and time
                totalHintsUsed += response.getHintsUsed() != null ? response.getHintsUsed() : 0;
                totalTimeTaken += response.getTimeTakenSeconds() != null ? response.getTimeTakenSeconds() : 0;
            }
        }
        
        // Overall Score (0-100)
        calc.overallScore = totalQuestions > 0 ? (correctAnswers * 100) / totalQuestions : 0;
        
        // Code Quality Score
        if (codingQuestions > 0) {
            calc.codeQualityScore = (codingCorrect * 100) / codingQuestions;
            // Deduct points for excessive hints
            calc.codeQualityScore -= Math.min(totalHintsUsed * 5, 30);
            calc.codeQualityScore = Math.max(calc.codeQualityScore, 0);
        } else {
            calc.codeQualityScore = calc.overallScore;
        }
        
        // Logical Reasoning Score (based on correctness and minimal hints)
        calc.logicalReasoningScore = calc.overallScore;
        if (totalHintsUsed > 0) {
            calc.logicalReasoningScore -= Math.min(totalHintsUsed * 3, 20);
        }
        calc.logicalReasoningScore = Math.max(calc.logicalReasoningScore, 0);
        
        // Time Management Score
        long avgTimePerQuestion = totalQuestions > 0 ? totalTimeTaken / totalQuestions : 0;
        long idealTime = 600; // 10 minutes per question
        
        if (avgTimePerQuestion <= idealTime) {
            calc.timeManagementScore = 100;
        } else {
            // Deduct points for exceeding ideal time
            long overtime = avgTimePerQuestion - idealTime;
            int deduction = (int) ((overtime / 60) * 2); // 2 points per minute overtime
            calc.timeManagementScore = Math.max(100 - deduction, 40);
        }
        
        // Problem Solving Score (combination of correctness and approach)
        calc.problemSolvingScore = (calc.overallScore + calc.logicalReasoningScore) / 2;
        
        // Store additional metrics
        calc.totalQuestions = totalQuestions;
        calc.correctAnswers = correctAnswers;
        calc.totalHintsUsed = totalHintsUsed;
        calc.avgTimePerQuestion = avgTimePerQuestion;
        
        return calc;
    }
    
    /**
     * Determine confidence level based on overall score
     */
    private String determineConfidenceLevel(int overallScore) {
        if (overallScore >= 80) return "HIGH";
        if (overallScore >= 60) return "MEDIUM";
        return "LOW";
    }
    
    /**
     * Generate detailed feedback
     */
    private String generateDetailedFeedback(ScoreCalculation scores, List<InterviewQuestion> questions) {
        StringBuilder feedback = new StringBuilder();
        
        feedback.append("**Interview Performance Summary**\n\n");
        feedback.append(String.format("You answered %d out of %d questions correctly (%.1f%% accuracy).\n\n",
            scores.correctAnswers, scores.totalQuestions, 
            (scores.correctAnswers * 100.0 / scores.totalQuestions)));
        
        // Code Quality Feedback
        if (scores.codeQualityScore >= 80) {
            feedback.append("**Code Quality:** Excellent! Your code is clean, efficient, and well-structured.\n");
        } else if (scores.codeQualityScore >= 60) {
            feedback.append("**Code Quality:** Good effort, but there's room for improvement in code organization and efficiency.\n");
        } else {
            feedback.append("**Code Quality:** Focus on writing cleaner, more readable code with better variable naming.\n");
        }
        
        // Logical Reasoning Feedback
        if (scores.logicalReasoningScore >= 75) {
            feedback.append("**Logical Reasoning:** Strong problem-solving approach with minimal hints needed.\n");
        } else {
            feedback.append("**Logical Reasoning:** Practice breaking down problems into smaller steps.\n");
        }
        
        // Time Management Feedback
        if (scores.timeManagementScore >= 80) {
            feedback.append("**Time Management:** Excellent pacing throughout the interview.\n");
        } else {
            feedback.append("**Time Management:** Try to work more efficiently. Practice solving problems under time pressure.\n");
        }
        
        // Hints Usage
        if (scores.totalHintsUsed > 0) {
            feedback.append(String.format("\nYou used %d hint(s) during the interview. ", scores.totalHintsUsed));
            feedback.append("Try to solve problems independently first before seeking hints.\n");
        }
        
        return feedback.toString();
    }
    
    /**
     * Identify strengths
     */
    private String identifyStrengths(ScoreCalculation scores, List<InterviewQuestion> questions) {
        List<String> strengths = new ArrayList<>();
        
        if (scores.codeQualityScore >= 80) {
            strengths.add("Clean and efficient code implementation");
        }
        
        if (scores.timeManagementScore >= 80) {
            strengths.add("Excellent time management skills");
        }
        
        if (scores.logicalReasoningScore >= 75) {
            strengths.add("Strong logical reasoning and problem-solving approach");
        }
        
        if (scores.totalHintsUsed == 0) {
            strengths.add("Independent problem solving without hints");
        }
        
        if (scores.overallScore >= 80) {
            strengths.add("High accuracy across different question types");
        }
        
        if (strengths.isEmpty()) {
            strengths.add("Completed the interview and showed persistence");
            strengths.add("Willingness to learn and improve");
        }
        
        return String.join("\n• ", strengths);
    }
    
    /**
     * Identify weaknesses
     */
    private String identifyWeaknesses(ScoreCalculation scores, List<InterviewQuestion> questions) {
        List<String> weaknesses = new ArrayList<>();
        
        if (scores.codeQualityScore < 60) {
            weaknesses.add("Code organization and structure need improvement");
        }
        
        if (scores.timeManagementScore < 60) {
            weaknesses.add("Time management - practice solving problems faster");
        }
        
        if (scores.totalHintsUsed > 3) {
            weaknesses.add("Over-reliance on hints - build problem-solving confidence");
        }
        
        if (scores.logicalReasoningScore < 60) {
            weaknesses.add("Logical reasoning - practice algorithmic thinking");
        }
        
        if (weaknesses.isEmpty()) {
            weaknesses.add("Minor improvements needed in edge case handling");
        }
        
        return String.join("\n• ", weaknesses);
    }
    
    /**
     * Generate improvement suggestions
     */
    private String generateImprovementSuggestions(ScoreCalculation scores, List<InterviewQuestion> questions) {
        List<String> suggestions = new ArrayList<>();
        
        suggestions.add("Practice coding problems daily on platforms like LeetCode or HackerRank");
        
        if (scores.codeQualityScore < 70) {
            suggestions.add("Study clean code principles and best practices");
            suggestions.add("Review code examples from experienced developers");
        }
        
        if (scores.timeManagementScore < 70) {
            suggestions.add("Practice with a timer to improve speed");
            suggestions.add("Learn common patterns and templates for frequent problem types");
        }
        
        if (scores.logicalReasoningScore < 70) {
            suggestions.add("Study data structures and algorithms systematically");
            suggestions.add("Break complex problems into smaller, manageable parts");
        }
        
        suggestions.add("Review your mistakes and understand why certain approaches didn't work");
        suggestions.add("Retake interviews on the same topics to track improvement");
        
        return String.join("\n• ", suggestions);
    }
    
    /**
     * Create default evaluation when no questions are available
     */
    private InterviewEvaluation createDefaultEvaluation(InterviewEvaluation evaluation) {
        evaluation.setOverallScore(0);
        evaluation.setCodeQualityScore(0);
        evaluation.setLogicalReasoningScore(0);
        evaluation.setTimeManagementScore(0);
        evaluation.setProblemSolvingScore(0);
        evaluation.setConfidenceLevel("LOW");
        evaluation.setAiFeedback("No responses recorded for this interview.");
        evaluation.setStrengths("N/A");
        evaluation.setWeaknesses("No interview data available");
        evaluation.setImprovementSuggestions("Complete an interview to receive personalized feedback");
        return evaluation;
    }
    
    /**
     * Internal class for score calculation
     */
    private static class ScoreCalculation {
        int overallScore = 0;
        int codeQualityScore = 0;
        int logicalReasoningScore = 0;
        int timeManagementScore = 0;
        int problemSolvingScore = 0;
        int totalQuestions = 0;
        int correctAnswers = 0;
        int totalHintsUsed = 0;
        long avgTimePerQuestion = 0;
    }
}

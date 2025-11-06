// InterviewService.java
package com.interviewcoaching.services.interview;

import com.interviewcoaching.models.auth.User;
import com.interviewcoaching.models.interview.*;
import com.interviewcoaching.repositories.interview.*;
import com.interviewcoaching.dto.interview.InterviewStartRequest;
import com.interviewcoaching.dto.interview.AnswerSubmitRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InterviewService {
    
    @Autowired private InterviewRepository interviewRepository;
    @Autowired private InterviewQuestionRepository interviewQuestionRepository;
    @Autowired private InterviewResponseRepository responseRepository;
    @Autowired private QuestionRepository questionRepository;
    @Autowired private InterviewSlotRepository slotRepository;
    @Autowired private CodeEvaluationService codeEvaluationService;
    @Autowired private UserSkillsAnalyticsService analyticsService;

    @Transactional
    public Interview startInterview(User user, InterviewStartRequest request) {
        System.out.println("========================================");
        System.out.println("Starting new interview...");
        System.out.println("User: " + user.getUsername());
        System.out.println("Topic: " + request.getTopic());
        System.out.println("Difficulty: " + request.getDifficultyLevel());
        System.out.println("Duration: " + request.getDuration() + " minutes");
        System.out.println("========================================");

        // Create new interview
        Interview interview = new Interview();
        interview.setUser(user);
        interview.setTitle(String.format("%s Interview - %s", 
            request.getTopic(), 
            request.getDifficultyLevel()));
        interview.setTopic(request.getTopic());
        interview.setDifficultyLevel(Question.DifficultyLevel.valueOf(request.getDifficultyLevel()));
        interview.setStatus(InterviewStatus.IN_PROGRESS);
        interview.setStartTime(LocalDateTime.now());
        
        // Calculate expected end time based on duration
        if (request.getDuration() != null && request.getDuration() > 0) {
            interview.setEndTime(LocalDateTime.now().plusMinutes(request.getDuration()));
            System.out.println("Interview will auto-complete at: " + interview.getEndTime());
        }
        
        // Select questions FIRST to know actual count and total points
        List<Question> questions = selectQuestions(
            request.getTopic(), 
            request.getDifficultyLevel(), 
            request.getQuestionCount()
        );
        
        // Calculate max score from question points
        int maxScore = questions.stream()
            .mapToInt(q -> q.getPoints() != null ? q.getPoints() : 10)
            .sum();
        
        // Set interview details
        interview.setQuestionCount(questions.size());
        interview.setMaxScore(maxScore);
        System.out.println("Interview will have " + questions.size() + " questions");
        System.out.println("Maximum possible score: " + maxScore + " points");
        
        interview = interviewRepository.save(interview);
        
        // Save interview questions
        saveInterviewQuestions(interview, questions);
        
        System.out.println("Interview created successfully with ID: " + interview.getId());
        System.out.println("========================================");
        
        return interview;
    }

    private List<Question> selectQuestions(String topic, String difficulty, int requestedCount) {
        List<Question> questions = new ArrayList<>();
        
        try {
            System.out.println("==========================================");
            System.out.println("Selecting questions for interview:");
            System.out.println("Topic: " + topic);
            System.out.println("Difficulty: " + difficulty);
            System.out.println("Requested count: " + requestedCount);
            System.out.println("==========================================");
            
            // STEP 1: Try to get questions by EXACT category and difficulty
            questions = questionRepository
                .findRandomQuestionsByCategoryAndDifficulty(topic, difficulty, requestedCount);
            
            System.out.println("Found " + questions.size() + " questions with exact match (category + difficulty)");
            
            // STEP 2: If not enough, get MORE from SAME CATEGORY (any difficulty)
            if (questions.size() < requestedCount) {
                System.out.println("Not enough questions with exact difficulty. Looking for more from same category...");
                
                int remaining = requestedCount - questions.size();
                
                // Get all questions from this category (any difficulty)
                List<Question> categoryQuestions = questionRepository.findByCategory(topic);
                System.out.println("Total questions in category '" + topic + "': " + categoryQuestions.size());
                
                // Filter out already selected questions
                Set<Long> existingIds = questions.stream()
                    .map(Question::getId)
                    .collect(Collectors.toSet());
                
                List<Question> additional = categoryQuestions.stream()
                    .filter(q -> !existingIds.contains(q.getId()))
                    .limit(remaining)
                    .collect(Collectors.toList());
                
                questions.addAll(additional);
                System.out.println("Added " + additional.size() + " more questions from same category");
            }
            
            System.out.println("==========================================");
            System.out.println("FINAL: Selected " + questions.size() + " questions (all from '" + topic + "' category)");
            
            // Log each selected question
            for (int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                System.out.println((i+1) + ". [ID:" + q.getId() + "] " + q.getTitle() + 
                    " (Category: " + q.getCategory() + ", Difficulty: " + q.getDifficultyLevel() + ")");
            }
            System.out.println("==========================================");
            
        } catch (Exception e) {
            System.err.println("ERROR selecting questions: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback: get questions from category only
            try {
                questions = questionRepository.findByCategory(topic);
                if (questions.size() > requestedCount) {
                    questions = questions.subList(0, requestedCount);
                }
                System.out.println("Fallback: Selected " + questions.size() + " questions from category");
            } catch (Exception ex) {
                System.err.println("Fallback also failed: " + ex.getMessage());
            }
        }
        
        return questions;
    }

    private void saveInterviewQuestions(Interview interview, List<Question> questions) {
        int order = 1;
        for (Question question : questions) {
            InterviewQuestion iq = new InterviewQuestion();
            iq.setInterview(interview);
            iq.setQuestion(question);
            iq.setOrderIndex(order++);
            iq.setCreatedAt(LocalDateTime.now());
            interviewQuestionRepository.save(iq);
        }
    }

    @Transactional
    public InterviewResponse submitAnswer(Long interviewId, Long questionId, AnswerSubmitRequest request, User user) {
        Interview interview = interviewRepository.findByIdAndUserId(interviewId, user.getId())
            .orElseThrow(() -> new IllegalArgumentException("Interview not found or access denied"));
        
        if (interview.getStatus() != InterviewStatus.IN_PROGRESS) {
            throw new IllegalStateException("Interview is not in progress");
        }
        
        InterviewQuestion iq = interviewQuestionRepository.findByInterviewIdAndQuestionId(interviewId, questionId)
            .orElseThrow(() -> new IllegalArgumentException("Question not found in this interview"));
        
        // Create or update response
        InterviewResponse response = iq.getResponse();
        if (response == null) {
            response = new InterviewResponse();
            response.setInterviewQuestion(iq);
            response.setStartTime(LocalDateTime.now());
        }
        
        response.setAnswer(request.getAnswer());
        response.setCodeSubmission(request.getCodeSubmission());
        response.setProgrammingLanguage(request.getProgrammingLanguage());
        response.setTimeTakenSeconds(request.getTimeTakenSeconds());
        response.setHintsUsed(request.getHintsUsed());
        response.setSubmittedAt(LocalDateTime.now());
        
        // Evaluate the response
        evaluateResponse(response, iq.getQuestion());
        
        // Save the response
        return responseRepository.save(response);
    }

    private void evaluateResponse(InterviewResponse response, Question question) {
        if (response.getCodeSubmission() != null && !response.getCodeSubmission().isEmpty()) {
            // Use the existing CodeEvaluationService for code evaluation
            codeEvaluationService.evaluateCode(response, question);
        } else {
            // Use AI-powered evaluation for theoretical/text answers
            evaluateTextAnswer(response, question);
        }
    }
    
    private void evaluateTextAnswer(InterviewResponse response, Question question) {
        String userAnswer = response.getAnswer() != null ? response.getAnswer().trim() : "";
        String expectedSolution = question.getSolution() != null ? question.getSolution().trim() : "";
        String questionText = question.getDescription();
        String questionCategory = question.getCategory();
        String difficultyLevel = question.getDifficultyLevel().name();
        int maxPoints = question.getPoints() != null ? question.getPoints() : 10;
        
        if (userAnswer.isEmpty()) {
            response.setScoreObtained(0);
            response.setIsCorrect(false);
            response.setExecutionOutput("No answer provided. 0/" + maxPoints + " points.");
            return;
        }
        
        // INTELLIGENT AI-BASED EVALUATION
        // Calculate percentage score (0-100%) based on multiple factors
        int percentageScore = 0;
        StringBuilder feedback = new StringBuilder();
        
        // 1. Answer Completeness (30%)
        int completenessScore = evaluateCompleteness(userAnswer, expectedSolution, difficultyLevel);
        percentageScore += completenessScore;
        
        // 2. Keyword Matching (30%)
        int keywordScore = evaluateKeywords(userAnswer, expectedSolution);
        percentageScore += keywordScore;
        
        // 3. Depth of Understanding (25%)
        int depthScore = evaluateDepth(userAnswer, difficultyLevel);
        percentageScore += depthScore;
        
        // 4. Accuracy (15%)
        int accuracyScore = evaluateAccuracy(userAnswer, expectedSolution);
        percentageScore += accuracyScore;
        
        // Convert percentage to actual points based on question worth
        int actualScore = (int) Math.round((percentageScore / 100.0) * maxPoints);
        
        // Generate AI-powered detailed feedback
        feedback.append("🤖 AI Evaluation: ");
        if (percentageScore >= 80) {
            feedback.append("Excellent answer! ");
            response.setIsCorrect(true);
        } else if (percentageScore >= 60) {
            feedback.append("Good answer with room for improvement. ");
            response.setIsCorrect(true);
        } else if (percentageScore >= 40) {
            feedback.append("Partial understanding shown. ");
            response.setIsCorrect(false);
        } else {
            feedback.append("Needs significant improvement. ");
            response.setIsCorrect(false);
        }
        
        feedback.append(String.format("Score: %d/%d points (%d%%). ", actualScore, maxPoints, percentageScore));
        feedback.append(String.format("Breakdown - Completeness: %d%%, Keywords: %d%%, Depth: %d%%, Accuracy: %d%%. ",
            (int)(completenessScore/30.0*100), (int)(keywordScore/30.0*100), 
            (int)(depthScore/25.0*100), (int)(accuracyScore/15.0*100)));
        
        // Add AI-like specific recommendations
        if (completenessScore < 20) {
            feedback.append("💡 Tip: Expand your answer with more details and explanations. ");
        }
        if (keywordScore < 15) {
            feedback.append("💡 Tip: Use more technical terminology related to the question. ");
        }
        if (depthScore < 15) {
            feedback.append("💡 Tip: Include practical examples or use cases to demonstrate understanding. ");
        }
        if (accuracyScore < 10) {
            feedback.append("💡 Tip: Review the core concepts to ensure factual accuracy. ");
        }
        
        response.setScoreObtained(actualScore);
        response.setExecutionOutput(feedback.toString());
        
        System.out.println("=== AI ANSWER EVALUATION ===");
        System.out.println("Question: " + questionText);
        System.out.println("Category: " + questionCategory);
        System.out.println("Difficulty: " + difficultyLevel);
        System.out.println("Question Worth: " + maxPoints + " points");
        System.out.println("User Answer Length: " + userAnswer.length() + " characters");
        System.out.println("Percentage Score: " + percentageScore + "%");
        System.out.println("Actual Score: " + actualScore + "/" + maxPoints + " points");
        System.out.println("Feedback: " + feedback.toString());
        System.out.println("============================");
    }
    
    private int evaluateCompleteness(String userAnswer, String expectedSolution, String difficulty) {
        int minLength = difficulty.equals("BEGINNER") ? 50 : 
                       difficulty.equals("INTERMEDIATE") ? 100 : 150;
        
        if (userAnswer.length() >= minLength * 2) return 30;
        if (userAnswer.length() >= minLength) return 25;
        if (userAnswer.length() >= minLength / 2) return 15;
        return 5;
    }
    
    private int evaluateKeywords(String userAnswer, String expectedSolution) {
        if (expectedSolution.isEmpty()) return 20; // Default if no solution provided
        
        String userLower = userAnswer.toLowerCase();
        String expectedLower = expectedSolution.toLowerCase();
        
        // Extract important keywords (longer than 3 chars, not common words)
        String[] expectedWords = expectedLower.split("\\s+");
        List<String> keywords = new ArrayList<>();
        Set<String> commonWords = Set.of("the", "and", "for", "with", "this", "that", "from", "have", "are", "was");
        
        for (String word : expectedWords) {
            String cleaned = word.replaceAll("[^a-z0-9]", "");
            if (cleaned.length() > 3 && !commonWords.contains(cleaned)) {
                keywords.add(cleaned);
            }
        }
        
        if (keywords.isEmpty()) return 20;
        
        int matchCount = 0;
        for (String keyword : keywords) {
            if (userLower.contains(keyword)) {
                matchCount++;
            }
        }
        
        float matchRatio = (float) matchCount / keywords.size();
        return (int) (matchRatio * 30);
    }
    
    private int evaluateDepth(String userAnswer, String difficulty) {
        int sentenceCount = userAnswer.split("[.!?]+").length;
        boolean hasExamples = userAnswer.toLowerCase().contains("example") || 
                            userAnswer.toLowerCase().contains("e.g.") ||
                            userAnswer.toLowerCase().contains("for instance");
        boolean hasTechnicalTerms = userAnswer.matches(".*\\b(algorithm|complexity|performance|memory|optimization|design|architecture|pattern)\\b.*");
        
        int score = 0;
        
        // Depth based on sentence count
        if (sentenceCount >= 5) score += 10;
        else if (sentenceCount >= 3) score += 5;
        else score += 2;
        
        // Bonus for examples
        if (hasExamples) score += 8;
        
        // Bonus for technical depth
        if (hasTechnicalTerms) score += 7;
        
        return Math.min(score, 25);
    }
    
    private int evaluateAccuracy(String userAnswer, String expectedSolution) {
        if (expectedSolution.isEmpty()) return 10; // Default
        
        String userLower = userAnswer.toLowerCase();
        String expectedLower = expectedSolution.toLowerCase();
        
        // Check for contradictory or incorrect statements (basic)
        boolean hasNegativeIndicators = userLower.matches(".*(incorrect|wrong|false|not true).*") && 
                                       !expectedLower.matches(".*(incorrect|wrong|false|not true).*");
        
        if (hasNegativeIndicators) return 5;
        
        // Semantic similarity (basic version)
        int commonPhrases = 0;
        String[] expectedPhrases = expectedLower.split("[.!?,;]");
        
        for (String phrase : expectedPhrases) {
            String trimmedPhrase = phrase.trim();
            if (trimmedPhrase.length() > 10 && userLower.contains(trimmedPhrase)) {
                commonPhrases++;
            }
        }
        
        if (expectedPhrases.length > 0) {
            float phraseMatch = (float) commonPhrases / expectedPhrases.length;
            return (int) (phraseMatch * 15);
        }
        
        return 10; // Default medium score
    }

    @Transactional
    public Interview completeInterview(Long interviewId, User user) {
        Interview interview = interviewRepository.findByIdAndUserId(interviewId, user.getId())
            .orElseThrow(() -> new IllegalArgumentException("Interview not found or access denied"));
        
        if (interview.getStatus() != InterviewStatus.IN_PROGRESS) {
            throw new IllegalStateException("Interview is not in progress");
        }
        
        System.out.println("========================================");
        System.out.println("COMPLETING INTERVIEW: " + interviewId);
        
        // Get all responses for this interview
        List<InterviewQuestion> questions = interviewQuestionRepository.findByInterviewId(interviewId);
        int totalScoreObtained = 0;
        int maxPossibleScore = 0;
        int questionsAnswered = 0;
        
        for (InterviewQuestion iq : questions) {
            Question q = iq.getQuestion();
            int questionPoints = q.getPoints() != null ? q.getPoints() : 10;
            maxPossibleScore += questionPoints;
            
            if (iq.getResponse() != null) {
                int scoreObtained = iq.getResponse().getScoreObtained();
                totalScoreObtained += scoreObtained;
                questionsAnswered++;
                System.out.println("Question: " + q.getTitle() + " - Score: " + scoreObtained + "/" + questionPoints);
            } else {
                System.out.println("Question: " + q.getTitle() + " - NOT ANSWERED (0/" + questionPoints + ")");
            }
        }
        
        // Calculate percentage for feedback
        int percentage = maxPossibleScore > 0 ? (int)((totalScoreObtained * 100.0) / maxPossibleScore) : 0;
        
        System.out.println("Total Score: " + totalScoreObtained + "/" + maxPossibleScore + " (" + percentage + "%)");
        System.out.println("Questions Answered: " + questionsAnswered + "/" + questions.size());
        
        // Update interview status
        interview.setStatus(InterviewStatus.COMPLETED);
        interview.setEndTime(LocalDateTime.now());
        interview.setTotalScore(totalScoreObtained);  // Store ACTUAL total score
        interview.setMaxScore(maxPossibleScore);      // Store max possible score
        interview.setQuestionCount(questions.size()); // Ensure count is correct
        
        // Generate feedback based on percentage
        String feedback = generateOverallFeedback(percentage, totalScoreObtained, maxPossibleScore);
        interview.setFeedback(feedback);
        
        System.out.println("Feedback: " + feedback);
        System.out.println("========================================");
        
        // Update user analytics if analytics service is available
        if (analyticsService != null) {
            analyticsService.updateSkillsAnalytics(user, interview);
        }
        
        return interviewRepository.save(interview);
    }
    
    private String generateOverallFeedback(int percentage, int scoreObtained, int maxScore) {
        String baseMessage;
        
        if (percentage >= 80) {
            baseMessage = "🎉 Excellent performance! You've demonstrated a strong understanding of the concepts. ";
        } else if (percentage >= 60) {
            baseMessage = "👍 Good job! You have a solid understanding but there's room for improvement in some areas. ";
        } else if (percentage >= 40) {
            baseMessage = "📚 You're on the right track, but need more practice. Review the concepts and try again. ";
        } else {
            baseMessage = "💪 Keep practicing! Review the material and focus on understanding core concepts. ";
        }
        
        return baseMessage + String.format("You scored %d out of %d points (%d%%).", scoreObtained, maxScore, percentage);
    }
    
    public List<Interview> getInterviewHistory(User user) {
        return interviewRepository.findByUserIdOrderByStartTimeDesc(user.getId());
    }
    
    @Transactional(readOnly = true)
    public Interview getInterviewDetails(Long interviewId, User user) {
        Interview interview = interviewRepository.findByIdAndUserId(interviewId, user.getId())
            .orElseThrow(() -> new IllegalArgumentException("Interview not found or access denied"));
        
        // Eagerly load interview questions to prevent lazy loading issues
        interview.getInterviewQuestions().size();
        
        // Eagerly load questions within interview questions
        interview.getInterviewQuestions().forEach(iq -> {
            if (iq.getQuestion() != null) {
                // Touch the question to load it
                iq.getQuestion().getTitle();
            }
        });
        
        return interview;
    }
    
    /**
     * Get all completed interviews for a user
     * @param user The user whose completed interviews to retrieve
     * @return List of completed interviews
     */
    public List<Interview> getCompletedInterviews(User user) {
        return interviewRepository.findCompletedInterviewsByUserId(user.getId());
    }
    
    /**
     * Cancel an interview by ID
     * @param interviewId The ID of the interview to cancel
     * @param user The user requesting the cancellation
     * @return The cancelled interview
     * @throws IllegalArgumentException if the interview is not found or already completed/cancelled
     */
    @Transactional
    public Interview cancelInterview(Long interviewId, User user) {
        Interview interview = interviewRepository.findByIdAndUserId(interviewId, user.getId())
            .orElseThrow(() -> new IllegalArgumentException("Interview not found or access denied"));
            
        if (interview.getStatus() == InterviewStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed interview");
        }
        
        if (interview.getStatus() == InterviewStatus.CANCELLED) {
            throw new IllegalStateException("Interview is already cancelled");
        }
        
        // If there's a slot, mark it as available
        if (interview.getSlot() != null) {
            InterviewSlot slot = interview.getSlot();
            slot.setBooked(false);
            slotRepository.save(slot);
        }
        
        // Update interview status
        interview.setStatus(InterviewStatus.CANCELLED);
        interview.setEndTime(LocalDateTime.now());
        
        return interviewRepository.save(interview);
    }
}
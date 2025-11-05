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
import org.springframework.data.domain.PageRequest;

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
        // Validate slot availability if slotId is provided
        if (request.getSlotId() != null) {
            slotRepository.findByIdAndBookedFalse(request.getSlotId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid or already booked slot"));
        }

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
        interview.setQuestionCount(request.getQuestionCount());
        
        // Select questions
        List<Question> questions = selectQuestions(
            request.getTopic(), 
            request.getDifficultyLevel(), 
            request.getQuestionCount()
        );
        
        interview = interviewRepository.save(interview);
        
        // Save interview questions
        saveInterviewQuestions(interview, questions);
        
        // Mark slot as booked if applicable
        if (request.getSlotId() != null) {
            slotRepository.bookSlot(request.getSlotId(), user.getId());
        }
        
        return interview;
    }

    private List<Question> selectQuestions(String topic, String difficulty, int count) {
        List<Question> questions = new ArrayList<>();
        
        try {
            // Try to get questions by exact category and difficulty
            questions = questionRepository
                .findRandomQuestionsByCategoryAndDifficulty(topic, difficulty, count);
            
            if (questions.size() < count) {
                System.out.println("Not enough questions for category: " + topic + ", difficulty: " + difficulty);
                System.out.println("Found " + questions.size() + " questions, need " + count);
                
                // If not enough, get more by difficulty only
                int remaining = count - questions.size();
                List<Question> additional = questionRepository
                    .findRandomQuestionsByDifficulty(difficulty, remaining);
                
                if (additional != null && !additional.isEmpty()) {
                    // Avoid duplicates
                    Set<Long> existingIds = questions.stream()
                        .map(Question::getId)
                        .collect(Collectors.toSet());
                    
                    additional.stream()
                        .filter(q -> !existingIds.contains(q.getId()))
                        .limit(remaining)
                        .forEach(questions::add);
                }
            }
            
            if (questions.size() < count) {
                System.out.println("Still not enough questions. Getting any available questions.");
                // Last resort: get any questions
                int remaining = count - questions.size();
                PageRequest pageRequest = PageRequest.of(0, remaining + 10);
                List<Question> allQuestions = questionRepository.findAll(pageRequest).getContent();
                
                Set<Long> existingIds = questions.stream()
                    .map(Question::getId)
                    .collect(Collectors.toSet());
                
                allQuestions.stream()
                    .filter(q -> !existingIds.contains(q.getId()))
                    .limit(remaining)
                    .forEach(questions::add);
            }
            
        } catch (Exception e) {
            System.err.println("Error selecting questions: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback: get any questions
            PageRequest pageRequest = PageRequest.of(0, count);
            questions = questionRepository.findAll(pageRequest).getContent();
        }
        
        System.out.println("Selected " + questions.size() + " questions for interview");
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
            // Text answer evaluation
            String answer = response.getAnswer() != null ? response.getAnswer().toLowerCase() : "";
            String expected = question.getSolution() != null ? 
                question.getSolution().toLowerCase() : "";
            
            // Simple keyword matching for demo
            int keywordMatches = 0;
            String[] keywords = expected.split("\\s+");
            
            if (keywords.length > 0) {
                for (String keyword : keywords) {
                    if (!keyword.trim().isEmpty() && answer.contains(keyword.toLowerCase())) {
                        keywordMatches++;
                    }
                }
                float matchPercentage = (float) keywordMatches / keywords.length;
                int score = (int) (matchPercentage * 100);
                
                response.setScoreObtained(score);
                response.setIsCorrect(matchPercentage > 0.7);
                response.setExecutionOutput(matchPercentage > 0.7 ? "Answer is correct" : "Answer needs improvement");
            } else {
                response.setScoreObtained(0);
                response.setIsCorrect(false);
                response.setExecutionOutput("No expected answer provided for evaluation");
            }
        }
    }

    @Transactional
    public Interview completeInterview(Long interviewId, User user) {
        Interview interview = interviewRepository.findByIdAndUserId(interviewId, user.getId())
            .orElseThrow(() -> new IllegalArgumentException("Interview not found or access denied"));
        
        if (interview.getStatus() != InterviewStatus.IN_PROGRESS) {
            throw new IllegalStateException("Interview is not in progress");
        }
        
        // Get all responses for this interview
        List<InterviewQuestion> questions = interviewQuestionRepository.findByInterviewId(interviewId);
        int totalScore = 0;
        int totalQuestions = questions.size();
        
        for (InterviewQuestion iq : questions) {
            if (iq.getResponse() != null) {
                totalScore += iq.getResponse().getScoreObtained();
            }
        }
        
        int averageScore = totalQuestions > 0 ? totalScore / totalQuestions : 0;
        
        // Update interview status
        interview.setStatus(InterviewStatus.COMPLETED);
        interview.setEndTime(LocalDateTime.now());
        interview.setTotalScore(averageScore);
        
        // Generate feedback
        String feedback = generateOverallFeedback(averageScore);
        interview.setFeedback(feedback);
        
        // Update user analytics if analytics service is available
        if (analyticsService != null) {
            analyticsService.updateSkillsAnalytics(user, interview);
        }
        
        return interviewRepository.save(interview);
    }
    
    private String generateOverallFeedback(int score) {
        if (score >= 80) {
            return "Excellent performance! You've demonstrated a strong understanding of the concepts.";
        } else if (score >= 60) {
            return "Good job! You have a solid understanding but there's room for improvement in some areas.";
        } else if (score >= 40) {
            return "You're on the right track, but need more practice. Review the concepts and try again.";
        } else {
            return "Needs improvement. Please review the material and practice more before your next attempt.";
        }
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
// frontend/src/features/interviews/components/InterviewRoom.jsx
import React, { useState, useRef, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Editor from '@monaco-editor/react';
import { interviewApi } from '../services/interviewApi';
import './InterviewRoom.css';

const InterviewRoom = () => {
  const { interviewId } = useParams();
  const navigate = useNavigate();
  
  const [interview, setInterview] = useState(null);
  const [questions, setQuestions] = useState([]);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [answers, setAnswers] = useState({});
  const [code, setCode] = useState('');
  const [timeElapsed, setTimeElapsed] = useState(0);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const timerRef = useRef(null);

  const currentQuestion = questions[currentQuestionIndex];

  // Fetch interview data when component mounts
  useEffect(() => {
    const fetchInterviewData = async () => {
      try {
        setLoading(true);
        console.log('Fetching interview data for ID:', interviewId);
        
        const response = await interviewApi.getInterview(interviewId);
        console.log('Interview data received:', response);
        
        if (response.interview) {
          setInterview(response.interview);
          setQuestions(response.questions || []);
          
          // Initialize code with template if available
          if (response.questions && response.questions[0]) {
            setCode(response.questions[0].codeTemplate || '// Write your code here\n');
          }
        } else {
          throw new Error('Invalid interview data');
        }
      } catch (err) {
        console.error('Error fetching interview:', err);
        setError('Failed to load interview. Please try again.');
      } finally {
        setLoading(false);
      }
    };

    if (interviewId) {
      fetchInterviewData();
    }
  }, [interviewId]);

  // Start timer when interview data is loaded
  useEffect(() => {
    if (interview && !timerRef.current) {
      timerRef.current = setInterval(() => {
        setTimeElapsed(prev => prev + 1);
      }, 1000);
    }

    return () => {
      if (timerRef.current) {
        clearInterval(timerRef.current);
      }
    };
  }, [interview]);

  // Update code template when question changes
  useEffect(() => {
    if (currentQuestion && currentQuestion.codeTemplate) {
      setCode(currentQuestion.codeTemplate);
    } else if (currentQuestion) {
      setCode('// Write your code here\n');
    }
  }, [currentQuestionIndex, currentQuestion]);

  const handleSubmitAnswer = async () => {
    if (isSubmitting || !currentQuestion) return;
    
    setIsSubmitting(true);
    console.log('Submitting answer for question:', currentQuestion.id);
    
    try {
      const answerData = {
        answerText: answers[currentQuestion.id] || '',
        codeSubmission: code,
        programmingLanguage: 'java',
        timeTakenSeconds: timeElapsed,
        hintsUsed: 0
      };

      console.log('Sending answer data:', answerData);
      
      // Submit answer to backend
      const response = await interviewApi.submitAnswer(
        interview.id,
        currentQuestion.id,
        answerData
      );
      
      console.log('Answer submitted successfully:', response);
      
      // Move to next question or complete interview
      if (currentQuestionIndex < questions.length - 1) {
        setCurrentQuestionIndex(currentQuestionIndex + 1);
      } else {
        // Complete the interview
        await handleCompleteInterview();
      }
    } catch (error) {
      console.error('Error submitting answer:', error);
      alert('Failed to submit answer. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCompleteInterview = async () => {
    try {
      console.log('Completing interview...');
      const response = await interviewApi.completeInterview(interview.id);
      console.log('Interview completed:', response);
      
      // Navigate to results page or dashboard
      alert('Interview completed successfully!');
      navigate('/interviews');
    } catch (error) {
      console.error('Error completing interview:', error);
      alert('Interview completed but there was an error processing results.');
      navigate('/interviews');
    }
  };

  const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs < 10 ? '0' : ''}${secs}`;
  };

  if (loading) {
    return (
      <div className="interview-room">
        <div className="loading">Loading interview...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="interview-room">
        <div className="error">
          <h2>Error</h2>
          <p>{error}</p>
          <button onClick={() => navigate('/interviews')}>Back to Interviews</button>
        </div>
      </div>
    );
  }

  if (!interview || !currentQuestion) {
    return (
      <div className="interview-room">
        <div className="error">No interview data available</div>
      </div>
    );
  }

  const isCodingQuestion = currentQuestion.questionType === 'CODING';

  return (
    <div className="interview-room">
      <div className="header">
        <h2>{interview.title || 'Interview in Progress'}</h2>
        <div className="interview-info">
          <span className="timer">Time: {formatTime(timeElapsed)}</span>
          <span className="difficulty">{interview.difficultyLevel}</span>
        </div>
      </div>

      <div className="question-section">
        <div className="question-header">
          <h3>Question {currentQuestionIndex + 1} of {questions.length}</h3>
          <span className="question-type">{currentQuestion.questionType}</span>
          <span className="points">{currentQuestion.points} points</span>
        </div>
        
        <h4>{currentQuestion.title}</h4>
        <div className="question-description">
          <p>{currentQuestion.description}</p>
        </div>
        
        {currentQuestion.constraintsInfo && (
          <div className="constraints">
            <strong>Constraints:</strong>
            <p>{currentQuestion.constraintsInfo}</p>
          </div>
        )}
        
        {isCodingQuestion ? (
          <div className="code-editor-section">
            <h5>Code Editor</h5>
            <Editor
              height="400px"
              defaultLanguage="java"
              value={code}
              onChange={(value) => setCode(value || '')}
              theme="vs-dark"
              options={{ 
                minimap: { enabled: false },
                fontSize: 14,
                lineNumbers: 'on',
                scrollBeyondLastLine: false,
                automaticLayout: true
              }}
            />
          </div>
        ) : (
          <div className="answer-section">
            <h5>Your Answer</h5>
            <textarea
              value={answers[currentQuestion.id] || ''}
              onChange={(e) => 
                setAnswers({ ...answers, [currentQuestion.id]: e.target.value })
              }
              placeholder="Type your answer here..."
              className="answer-textarea"
              rows="10"
            />
          </div>
        )}
      </div>

      <div className="navigation-buttons">
        <button 
          onClick={() => setCurrentQuestionIndex(Math.max(0, currentQuestionIndex - 1))}
          disabled={currentQuestionIndex === 0 || isSubmitting}
          className="nav-button"
        >
          ← Previous
        </button>
        
        <button 
          onClick={handleSubmitAnswer}
          disabled={isSubmitting}
          className="submit-button"
        >
          {isSubmitting ? 'Submitting...' : 
           currentQuestionIndex < questions.length - 1 ? 'Next Question →' : 'Complete Interview ✓'}
        </button>
      </div>
    </div>
  );
};

export default InterviewRoom;
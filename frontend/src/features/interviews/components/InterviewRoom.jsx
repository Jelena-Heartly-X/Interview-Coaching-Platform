// frontend/src/features/interviews/components/InterviewRoom.jsx
import React, { useState, useRef, useEffect } from 'react';
import Editor from '@monaco-editor/react';
import { interviewApi } from '../services/interviewApi';
import './InterviewRoom.css';

const InterviewRoom = ({ interview, onComplete, onExit }) => {
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [answers, setAnswers] = useState({});
  const [code, setCode] = useState('// Write your code here\n');
  const [timeElapsed, setTimeElapsed] = useState(0);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const timerRef = useRef(null);

  const currentQuestion = interview.questions[currentQuestionIndex];

  // Start timer when component mounts
  useEffect(() => {
    timerRef.current = setInterval(() => {
      setTimeElapsed(prev => prev + 1);
    }, 1000);

    return () => clearInterval(timerRef.current);
  }, []);

  const handleSubmitAnswer = async () => {
    if (isSubmitting) return;
    
    setIsSubmitting(true);
    console.log('Submitting answer...');
    
    try {
      const answerData = {
        questionId: currentQuestion.id,
        answer: answers[currentQuestion.id] || '',
        codeSubmission: code,
        programmingLanguage: 'javascript',
        timeTakenSeconds: timeElapsed,
      };

      console.log('Sending answer data:', answerData);
      
      // For testing, log to console instead of making API call
      console.log('Answer submitted (mock):', answerData);
      
      // Mock API response
      await new Promise(resolve => setTimeout(resolve, 500));
      
      // Move to next question or complete interview
      if (currentQuestionIndex < interview.questions.length - 1) {
        setCurrentQuestionIndex(currentQuestionIndex + 1);
        setCode('// Write your code here\n');
      } else {
        console.log('Interview completed!');
        // Mock completion
        const mockResults = {
          overallScore: 85,
          codeQualityScore: 90,
          logicalReasoningScore: 80,
          timeManagementScore: 85,
          problemSolvingScore: 88,
          aiFeedback: 'Great job on the interview! Your code was clean and efficient.'
        };
        onComplete(mockResults);
      }
    } catch (error) {
      console.error('Error submitting answer:', error);
      alert('Failed to submit answer. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs < 10 ? '0' : ''}${secs}`;
  };

  return (
    <div className="interview-room">
      <div className="header">
        <h2>Interview in Progress</h2>
        <div className="timer">Time: {formatTime(timeElapsed)}</div>
      </div>

      <div className="question-section">
        <h3>Question {currentQuestionIndex + 1} of {interview.questions.length}</h3>
        <p>{currentQuestion.text}</p>
        
        {currentQuestion.type === 'coding' ? (
          <div className="code-editor">
            <Editor
              height="300px"
              defaultLanguage="javascript"
              value={code}
              onChange={(value) => setCode(value || '')}
              options={{ minimap: { enabled: false } }}
            />
          </div>
        ) : (
          <textarea
            value={answers[currentQuestion.id] || ''}
            onChange={(e) => 
              setAnswers({ ...answers, [currentQuestion.id]: e.target.value })
            }
            placeholder="Type your answer here..."
            className="answer-textarea"
          />
        )}
      </div>

      <div className="navigation-buttons">
        <button 
          onClick={() => setCurrentQuestionIndex(Math.max(0, currentQuestionIndex - 1))}
          disabled={currentQuestionIndex === 0 || isSubmitting}
          className="nav-button"
        >
          Previous
        </button>
        
        <button 
          onClick={handleSubmitAnswer}
          disabled={isSubmitting}
          className="submit-button"
        >
          {isSubmitting ? 'Submitting...' : 
           currentQuestionIndex < interview.questions.length - 1 ? 'Next' : 'Submit Interview'}
        </button>
      </div>
    </div>
  );
};

export default InterviewRoom;
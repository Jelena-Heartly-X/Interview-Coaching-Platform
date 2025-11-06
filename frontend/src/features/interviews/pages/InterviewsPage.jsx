// frontend/src/features/interviews/pages/InterviewsPage.jsx
import React, { useState } from 'react';
import InterviewLobby from '../components/InterviewLobby';
import InterviewRoom from '../components/InterviewRoom';
import InterviewResults from '../components/InterviewResults';
import './InterviewsPage.css';

const InterviewsPage = () => {
  const [currentView, setCurrentView] = useState('lobby');
  const [activeInterview, setActiveInterview] = useState(null);
  const [interviewResults, setInterviewResults] = useState(null);

  const handleStartInterview = (slot) => {
    console.log('Starting interview with slot:', slot);
    // In a real app, you would create an interview session with the backend here
    // frontend/src/features/interviews/pages/InterviewsPage.jsx
const mockInterview = {
  id: 1,  // Changed from 'mock-interview-1' to number
  slotId: slot.id,
  questions: [
    { id: 1, text: 'Explain the Virtual DOM...', type: 'conceptual' },
    { id: 2, text: 'Implement a function...', type: 'coding' }
  ]
};
    
    setActiveInterview(mockInterview);
    setCurrentView('room');
  };

  const handleCompleteInterview = (results) => {
    console.log('Interview completed with results:', results);
    setInterviewResults({
      overallScore: 85,
      codeQualityScore: 90,
      logicalReasoningScore: 80,
      timeManagementScore: 85,
      problemSolvingScore: 88,
      aiFeedback: 'You did a great job overall! Your code was clean and well-structured. Try to explain your thought process more clearly next time.'
    });
    setCurrentView('results');
  };

  const handleBackToLobby = () => {
    setCurrentView('lobby');
    setActiveInterview(null);
    setInterviewResults(null);
  };

  return (
    <div className="interviews-page">
      <header className="page-header">
        <h1>
          <span className="header-icon">🎯</span>
          Interview Preparation
          <span className="header-icon">💼</span>
        </h1>
        <p>
          🚀 Practice and improve your interview skills with our AI-powered interactive platform
        </p>
      </header>

      <main className="interview-container">
        {currentView === 'lobby' && (
          <InterviewLobby onStartInterview={handleStartInterview} />
        )}
        
        {currentView === 'room' && activeInterview && (
          <InterviewRoom 
            interview={activeInterview}
            onComplete={handleCompleteInterview}
            onExit={handleBackToLobby}
          />
        )}
        
        {currentView === 'results' && interviewResults && (
          <InterviewResults 
            results={interviewResults}
            onBackToLobby={handleBackToLobby}
          />
        )}
      </main>
    </div>
  );
};

export default InterviewsPage;
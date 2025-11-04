import React from 'react';
//import './InterviewResults.css';

const InterviewResults = ({ results, onBackToLobby }) => {
  return (
    <div className="interview-results">
      <h2>Interview Results</h2>
      <p>Overall Score: {results.overallScore}</p>
      <p>Code Quality: {results.codeQualityScore}</p>
      <p>Logical Reasoning: {results.logicalReasoningScore}</p>
      <p>Time Management: {results.timeManagementScore}</p>
      <p>Problem Solving: {results.problemSolvingScore}</p>
      <h3>AI Feedback</h3>
      <p>{results.aiFeedback}</p>
      <button onClick={onBackToLobby}>Back to Lobby</button>
    </div>
  );
};

export default InterviewResults;

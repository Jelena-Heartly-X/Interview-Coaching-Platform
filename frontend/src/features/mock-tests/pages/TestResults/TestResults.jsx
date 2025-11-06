import React from 'react';
import { useNavigate } from 'react-router-dom';
import './TestResults.css';

const TestResults = () => {
  const navigate = useNavigate();

  const results = {
    score: 85,
    totalQuestions: 10,
    correctAnswers: 8,
    timeTaken: '25:30',
    testName: 'JavaScript Fundamentals Test'
  };

  return (
    <div className="test-results">
      <div className="results-container">
        <div className="results-header">
          <h1>Test Results</h1>
          <p>{results.testName}</p>
        </div>

        <div className="score-section">
          <div className="score-circle">
            <div className="score-value">{results.score}%</div>
            <div className="score-label">Overall Score</div>
          </div>
          
          <div className="score-details">
            <div className="detail-item">
              <span className="detail-label">Correct Answers</span>
              <span className="detail-value">{results.correctAnswers}/{results.totalQuestions}</span>
            </div>
            <div className="detail-item">
              <span className="detail-label">Time Taken</span>
              <span className="detail-value">{results.timeTaken}</span>
            </div>
            <div className="detail-item">
              <span className="detail-label">Status</span>
              <span className="detail-value passed">Passed</span>
            </div>
          </div>
        </div>

        <div className="results-actions">
          <button className="action-btn secondary" onClick={() => navigate('/mock-tests')}>
            Back to Tests
          </button>
          <button className="action-btn primary">
            View Detailed Analysis
          </button>
          <button className="action-btn primary">
            Retake Test
          </button>
        </div>
      </div>
    </div>
  );
};

export default TestResults;
import React from 'react';
import { useNavigate } from 'react-router-dom';
import './TestAttempt.css';

const TestAttempt = () => {
  const navigate = useNavigate();

  return (
    <div className="test-attempt">
      <div className="test-container">
        <div className="test-header">
          <h1>Test in Progress</h1>
          <p>JavaScript Fundamentals Test</p>
        </div>
        
        <div className="test-content">
          <div className="question-section">
            <h2>Question 1</h2>
            <p>What is the output of console.log(typeof null) in JavaScript?</p>
            
            <div className="options">
              <label className="option">
                <input type="radio" name="answer" />
                <span>object</span>
              </label>
              <label className="option">
                <input type="radio" name="answer" />
                <span>null</span>
              </label>
              <label className="option">
                <input type="radio" name="answer" />
                <span>undefined</span>
              </label>
              <label className="option">
                <input type="radio" name="answer" />
                <span>string</span>
              </label>
            </div>
          </div>

          <div className="test-controls">
            <button className="nav-btn prev-btn">Previous</button>
            <div className="progress-info">
              <span>Question 1 of 10</span>
              <span>Time: 25:30</span>
            </div>
            <button className="nav-btn next-btn">Next</button>
          </div>
        </div>

        <div className="test-actions">
          <button className="secondary-btn" onClick={() => navigate('/mock-tests')}>
            Exit Test
          </button>
          <button 
            className="primary-btn"
            onClick={() => navigate('/mock-tests/results/123')}
          >
            Submit Test
          </button>
        </div>
      </div>
    </div>
  );
};

export default TestAttempt;
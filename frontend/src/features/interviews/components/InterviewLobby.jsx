// frontend/src/features/interviews/components/InterviewLobby.jsx
import React, { useState } from 'react';
import { interviewApi } from '../services/interviewApi';
import { useNavigate } from 'react-router-dom';
import './InterviewLobby.css';

const InterviewLobby = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [interviewConfig, setInterviewConfig] = useState({
    topic: 'DSA',
    difficultyLevel: 'INTERMEDIATE',
    duration: 30,
  });
  const navigate = useNavigate();

  const handleConfigChange = (e) => {
    const { name, value } = e.target;
    setInterviewConfig(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleStartInterview = async () => {
    try {
      setLoading(true);
      setError('');
      
      // Calculate question count based on duration (~6 minutes per question)
      const questionCount = Math.max(3, Math.floor(interviewConfig.duration / 6));
      
      const requestData = {
        topic: interviewConfig.topic,
        difficultyLevel: interviewConfig.difficultyLevel,
        questionCount: questionCount,
        duration: interviewConfig.duration
      };
      
      console.log('🚀 Starting interview with:', requestData);
      
      const response = await interviewApi.startInterview(requestData);
      
      console.log('✅ Interview started successfully:', response);
      
      // Navigate to interview room
      if (response.interview && response.interview.id) {
        navigate(`/interview/${response.interview.id}`);
      } else if (response.interviewId || response.id) {
        navigate(`/interview/${response.interviewId || response.id}`);
      }
    } catch (err) {
      console.error('❌ Error starting interview:', err);
      setError(err.response?.data?.error || 'Failed to start interview. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const topics = [
    { value: 'DSA', label: '📊 Data Structures & Algorithms', icon: '🔢' },
    { value: 'DBMS', label: '🗄️ Database Management', icon: '💾' },
    { value: 'OS', label: '🖥️ Operating Systems', icon: '⚙️' },
    { value: 'OOP', label: '🎯 Object-Oriented Programming', icon: '🔷' },
    { value: 'WEB_DEV', label: '🌐 Web Development', icon: '🚀' },
    { value: 'SYSTEM_DESIGN', label: '🏗️ System Design', icon: '🏛️' }
  ];

  const difficulties = [
    { value: 'BEGINNER', label: 'Beginner', icon: '🌱', color: '#10B981' },
    { value: 'INTERMEDIATE', label: 'Intermediate', icon: '⚡', color: '#F59E0B' },
    { value: 'ADVANCED', label: 'Advanced', icon: '🔥', color: '#EF4444' }
  ];

  const durations = [
    { value: 15, label: '15 min', desc: 'Quick Practice' },
    { value: 30, label: '30 min', desc: 'Standard' },
    { value: 45, label: '45 min', desc: 'Extended' },
    { value: 60, label: '60 min', desc: 'Full Session' }
  ];

  return (
    <div className="interview-lobby-modern">
      <div className="lobby-container">
        <div className="lobby-header">
          <h1 className="lobby-title">
            <span className="title-icon"></span>
            Configure Your Interview
          </h1>
          <p className="lobby-subtitle">
            Choose your topic, difficulty, and duration to start practicing
          </p>
        </div>

        {error && (
          <div className="alert-error">
            <span>⚠️</span>
            <span>{error}</span>
          </div>
        )}

        <div className="config-grid">
          {/* Topic Selection */}
          <div className="config-card">
            <label className="config-label">
              <span className="label-icon">📚</span>
              Interview Topic
            </label>
            <div className="topic-grid">
              {topics.map((topic) => (
                <button
                  key={topic.value}
                  className={`topic-btn ${interviewConfig.topic === topic.value ? 'active' : ''}`}
                  onClick={() => setInterviewConfig(prev => ({ ...prev, topic: topic.value }))}
                >
                  <span className="topic-icon">{topic.icon}</span>
                  <span className="topic-label">{topic.label}</span>
                </button>
              ))}
            </div>
          </div>

          {/* Difficulty Selection */}
          <div className="config-card">
            <label className="config-label">
              <span className="label-icon">⚡</span>
              Difficulty Level
            </label>
            <div className="difficulty-grid">
              {difficulties.map((diff) => (
                <button
                  key={diff.value}
                  className={`difficulty-btn ${interviewConfig.difficultyLevel === diff.value ? 'active' : ''}`}
                  onClick={() => setInterviewConfig(prev => ({ ...prev, difficultyLevel: diff.value }))}
                  style={{ '--diff-color': diff.color }}
                >
                  <span className="diff-icon">{diff.icon}</span>
                  <span className="diff-label">{diff.label}</span>
                </button>
              ))}
            </div>
          </div>

          {/* Duration Selection */}
          <div className="config-card">
            <label className="config-label">
              <span className="label-icon">⏱️</span>
              Interview Duration
            </label>
            <div className="duration-grid">
              {durations.map((dur) => (
                <button
                  key={dur.value}
                  className={`duration-btn ${interviewConfig.duration === dur.value ? 'active' : ''}`}
                  onClick={() => setInterviewConfig(prev => ({ ...prev, duration: dur.value }))}
                >
                  <span className="duration-time">{dur.label}</span>
                  <span className="duration-desc">{dur.desc}</span>
                  <span className="duration-questions">
                    ~{Math.max(3, Math.floor(dur.value / 6))} questions
                  </span>
                </button>
              ))}
            </div>
          </div>
        </div>

        {/* Interview Summary */}
        <div className="interview-summary">
          <h3 className="summary-title">Interview Summary</h3>
          <div className="summary-details">
            <div className="summary-item">
              <span className="summary-label">Topic:</span>
              <span className="summary-value">
                {topics.find(t => t.value === interviewConfig.topic)?.label}
              </span>
            </div>
            <div className="summary-item">
              <span className="summary-label">Difficulty:</span>
              <span className="summary-value">
                {difficulties.find(d => d.value === interviewConfig.difficultyLevel)?.label}
              </span>
            </div>
            <div className="summary-item">
              <span className="summary-label">Duration:</span>
              <span className="summary-value">{interviewConfig.duration} minutes</span>
            </div>
            <div className="summary-item">
              <span className="summary-label">Questions:</span>
              <span className="summary-value">
                ~{Math.max(3, Math.floor(interviewConfig.duration / 6))} questions
              </span>
            </div>
          </div>
        </div>

        {/* Start Button */}
        <div className="actions-container">
          <button
            onClick={handleStartInterview}
            disabled={loading}
            className="start-interview-btn"
          >
            {loading ? (
              <>
                <span className="spinner"></span>
                <span>Starting Interview...</span>
              </>
            ) : (
              <>
                <span className="btn-icon"></span>
                <span>Start Interview Now</span>
              </>
            )}
          </button>
        </div>
      </div>
    </div>
  );
};

export default InterviewLobby;
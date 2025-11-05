// frontend/src/features/interviews/components/InterviewLobby.jsx
import React, { useState, useEffect } from 'react';
import { interviewApi } from '../services/interviewApi';
import { useNavigate } from 'react-router-dom';
import './InterviewLobby.css';

const InterviewLobby = () => {
  const [slots, setSlots] = useState([]);
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [interviewConfig, setInterviewConfig] = useState({
    interviewType: 'DSA',
    difficultyLevel: 'INTERMEDIATE',
    company: '',
    duration: 30, // in minutes
  });
  const navigate = useNavigate();

  useEffect(() => {
    const fetchAvailableSlots = async () => {
      try {
        setLoading(true);
        const response = await interviewApi.getAvailableSlots();
        setSlots(response);
      } catch (err) {
        console.error('Error fetching slots:', err);
        setError('Failed to load interview slots. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchAvailableSlots();
  }, []);

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
      
      // Map frontend config to backend request format
      const requestData = {
        topic: interviewConfig.interviewType || 'TECHNICAL',
        difficultyLevel: interviewConfig.difficultyLevel || 'MEDIUM',
        questionCount: Math.floor(interviewConfig.duration / 6) || 5, // ~6 minutes per question
        ...(selectedSlot && { slotId: selectedSlot.id })
      };
      
      console.log('Starting interview with data:', requestData);
      
      const response = await interviewApi.startInterview(requestData);
      
      console.log('Interview started successfully:', response);
      
      // Navigate to interview room with the created interview ID
      if (response.interview && response.interview.id) {
        navigate(`/interview/${response.interview.id}`);
      } else if (response.interviewId || response.id) {
        navigate(`/interview/${response.interviewId || response.id}`);
      } else {
        // For testing - create a mock interview session
        const mockInterviewId = Date.now();
        navigate(`/interview/${mockInterviewId}`, { 
          state: { 
            config: interviewConfig,
            mockMode: true 
          }
        });
      }
    } catch (err) {
      console.error('Error starting interview:', err);
      setError('Failed to start interview. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Loading interview configuration...</div>;
  }

  return (
    <div className="interview-lobby">
      <h2>Configure Your Interview</h2>
      
      <div className="config-section">
        <div className="form-group">
          <label>Interview Topic:</label>
          <select 
            name="interviewType" 
            value={interviewConfig.interviewType}
            onChange={handleConfigChange}
            className="form-control"
          >
            <option value="DSA">Data Structures & Algorithms</option>
            <option value="DBMS">Database Management</option>
            <option value="OS">Operating Systems</option>
            <option value="OOP">Object-Oriented Programming</option>
            <option value="WEB_DEV">Web Development</option>
            <option value="SYSTEM_DESIGN">System Design</option>
          </select>
        </div>

        <div className="form-group">
          <label>Difficulty Level:</label>
          <select 
            name="difficultyLevel" 
            value={interviewConfig.difficultyLevel}
            onChange={handleConfigChange}
            className="form-control"
          >
            <option value="BEGINNER">Beginner</option>
            <option value="INTERMEDIATE">Intermediate</option>
            <option value="ADVANCED">Advanced</option>
          </select>
        </div>

        <div className="form-group">
          <label>Company (Optional):</label>
          <input
            type="text"
            name="company"
            value={interviewConfig.company}
            onChange={handleConfigChange}
            placeholder="E.g., Google, Amazon, etc."
            className="form-control"
          />
        </div>

        <div className="form-group">
          <label>Duration (minutes):</label>
          <select
            name="duration"
            value={interviewConfig.duration}
            onChange={handleConfigChange}
            className="form-control"
          >
            <option value={30}>30 minutes</option>
            <option value={45}>45 minutes</option>
            <option value={60}>60 minutes</option>
          </select>
        </div>
      </div>

      <h3>Available Time Slots (Optional)</h3>
      {error && <div className="error-message">{error}</div>}
      
      {slots.length === 0 ? (
        <p className="info-message">No pre-scheduled slots available. You can still start a practice interview with your configuration above!</p>
      ) : (
        <div className="slots-container">
          {slots.map((slot) => (
            <div 
              key={slot.id}
              className={`slot-card ${selectedSlot?.id === slot.id ? 'selected' : ''}`}
              onClick={() => setSelectedSlot(slot)}
            >
              <div className="slot-time">
                {new Date(slot.scheduledDateTime).toLocaleString()}
              </div>
              <div className="slot-mentor">
                Mentor: {slot.mentor?.name || 'Available'}
              </div>
              <div className="slot-duration">
                Duration: {slot.durationMinutes} minutes
              </div>
            </div>
          ))}
        </div>
      )}

      <div className="actions">
        <button
          onClick={handleStartInterview}
          disabled={loading}
          className="btn btn-primary start-button"
        >
          {loading ? 'Starting...' : 'Start Interview'}
        </button>
      </div>
    </div>
  );
};

export default InterviewLobby;
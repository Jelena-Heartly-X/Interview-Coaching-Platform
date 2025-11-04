// frontend/src/features/interviews/components/InterviewLobby.jsx
import React, { useState, useEffect } from 'react';
import { interviewApi } from '../services/interviewApi';
import './InterviewLobby.css';

const InterviewLobby = ({ onStartInterview }) => {
  const [slots, setSlots] = useState([]);
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchSlots = async () => {
      try {
        setLoading(true);
        // For now, we'll use mock data since the backend might not be ready
        // Uncomment this when your backend is ready:
        // const availableSlots = await interviewApi.getAvailableSlots();
        
        // Mock data for testing
        const mockSlots = [
          {
            id: '1',
            title: 'Frontend Developer Interview',
            topic: 'React',
            difficultyLevel: 'Medium',
            duration: 45,
            availableSlots: 5
          },
          {
            id: '2',
            title: 'Backend Developer Interview',
            topic: 'Spring Boot',
            difficultyLevel: 'Hard',
            duration: 60,
            availableSlots: 3
          }
        ];
        
        setSlots(mockSlots);
      } catch (err) {
        console.error('Error fetching slots:', err);
        setError('Failed to load interview slots. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchSlots();
  }, []);

  const handleStart = () => {
    if (selectedSlot) {
      onStartInterview(selectedSlot);
    } else {
      alert('Please select an interview slot first');
    }
  };

  if (loading) {
    return <div className="loading">Loading available slots...</div>;
  }

  if (error) {
    return <div className="error">{error}</div>;
  }

  return (
    <div className="interview-lobby">
      <h2>Select an Interview Slot</h2>
      {slots.length === 0 ? (
        <p>No interview slots available at the moment. Please check back later.</p>
      ) : (
        <>
          <ul className="slots-list">
            {slots.map((slot) => (
              <li 
                key={slot.id} 
                onClick={() => setSelectedSlot(slot)} 
                className={`slot-item ${selectedSlot?.id === slot.id ? 'selected' : ''}`}
              >
                <div className="slot-header">
                  <h3>{slot.title}</h3>
                  <span className={`difficulty ${slot.difficultyLevel.toLowerCase()}`}>
                    {slot.difficultyLevel}
                  </span>
                </div>
                <p>Topic: {slot.topic}</p>
                <p>Duration: {slot.duration} minutes</p>
                <p>Available Slots: {slot.availableSlots}</p>
              </li>
            ))}
          </ul>
          <button 
            onClick={handleStart} 
            disabled={!selectedSlot}
            className="start-button"
          >
            Start Interview
          </button>
        </>
      )}
    </div>
  );
};

export default InterviewLobby;
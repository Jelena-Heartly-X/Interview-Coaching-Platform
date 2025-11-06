import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export const useMockTest = () => {
  const navigate = useNavigate();
  const [currentTest, setCurrentTest] = useState(null);
  const [loading, setLoading] = useState(false);

  const startTest = async (testId) => {
    setLoading(true);
    try {
      // TODO: Replace with actual API call
      setTimeout(() => {
        const test = {
          id: testId,
          title: 'JavaScript Fundamentals Test',
          questions: [],
          duration: 30
        };
        setCurrentTest(test);
        navigate(`/mock-tests/attempt/${testId}`);
        setLoading(false);
      }, 500);
    } catch (error) {
      console.error('Error starting test:', error);
      setLoading(false);
    }
  };

  const submitTest = async (attemptData) => {
    setLoading(true);
    try {
      // TODO: Replace with actual API call
      setTimeout(() => {
        const results = {
          score: 85,
          totalQuestions: 10,
          correctAnswers: 8,
          timeTaken: 25
        };
        navigate(`/mock-tests/results/123`);
        setLoading(false);
      }, 500);
    } catch (error) {
      console.error('Error submitting test:', error);
      setLoading(false);
    }
  };

  return {
    currentTest,
    loading,
    startTest,
    submitTest
  };
};
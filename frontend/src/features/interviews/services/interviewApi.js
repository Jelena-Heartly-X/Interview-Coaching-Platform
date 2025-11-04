// frontend/src/features/interviews/services/interviewApi.js
import apiClient from '../../../shared/services/apiClient';

export const interviewApi = {
  // Get available interview slots
  getAvailableSlots: async () => {
    const response = await apiClient.get('/api/interview-slots/available');
    return response.data;
  },

  // Start a new interview
  startInterview: async (slotId) => {
    const response = await apiClient.post('/api/interviews/start', { slotId });
    return response.data;
  },

  // Submit an answer
  submitAnswer: async (interviewId, answerData) => {
    const response = await apiClient.post(
      `/api/interviews/${interviewId}/submit-answer`,
      answerData
    );
    return response.data;
  },

  // Complete interview
  completeInterview: async (interviewId) => {
    const response = await apiClient.post(
      `/api/interviews/${interviewId}/complete`
    );
    return response.data;
  },

  // Get interview details
  getInterview: async (interviewId) => {
    const response = await apiClient.get(`/api/interviews/${interviewId}`);
    return response.data;
  },
};

export default interviewApi;
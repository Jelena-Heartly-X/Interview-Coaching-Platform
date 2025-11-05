// frontend/src/features/interviews/services/interviewApi.js
import axios from 'axios';

// Create axios instance with base URL and headers
const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add a request interceptor to include the auth token
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add a response interceptor to handle errors
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Handle unauthorized access
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const interviewApi = {
  // Get available interview slots
  getAvailableSlots: async () => {
    try {
      const response = await apiClient.get('/interview-slots/available');
      return response.data;
    } catch (error) {
      console.error('Error fetching available slots:', error);
      throw error;
    }
  },

  // Start a new interview
  startInterview: async (interviewData) => {
    try {
      const response = await apiClient.post('/interviews/start', interviewData);
      return response.data;
    } catch (error) {
      console.error('Error starting interview:', error);
      throw error;
    }
  },

  // Get interview details
  getInterview: async (interviewId) => {
    try {
      const response = await apiClient.get(`/interviews/${interviewId}`);
      return response.data;
    } catch (error) {
      console.error('Error fetching interview:', error);
      throw error;
    }
  },

  // Get interview questions
  getInterviewQuestions: async (interviewId) => {
    try {
      const response = await apiClient.get(`/interviews/${interviewId}/questions`);
      return response.data;
    } catch (error) {
      console.error('Error fetching interview questions:', error);
      throw error;
    }
  },

  // Submit an answer
  submitAnswer: async (interviewId, questionId, answerData) => {
    try {
      const response = await apiClient.post(
        `/interviews/${interviewId}/questions/${questionId}/submit-answer`,
        answerData
      );
      return response.data;
    } catch (error) {
      console.error('Error submitting answer:', error);
      throw error;
    }
  },

  // Complete interview
  completeInterview: async (interviewId) => {
    try {
      const response = await apiClient.post(`/interviews/${interviewId}/complete`);
      return response.data;
    } catch (error) {
      console.error('Error completing interview:', error);
      throw error;
    }
  },

  // Get interview history for the current user
  getInterviewHistory: async () => {
    try {
      const response = await apiClient.get('/interviews/history');
      return response.data;
    } catch (error) {
      console.error('Error fetching interview history:', error);
      throw error;
    }
  },
};

export default interviewApi;
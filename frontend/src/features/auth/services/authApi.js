import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/auth';

export const authApi = {
  login: async (credentials) => {
    const response = await axios.post(`${API_BASE_URL}/login`, credentials);
    return response.data;
  },

  register: async (userData) => {
    const response = await axios.post(`${API_BASE_URL}/register`, userData);
    return response.data;
  },

  forgotPassword: async (email) => {
    const response = await axios.post(`${API_BASE_URL}/forgot-password`, { email });
    return response.data;
  },

  resetPassword: async (token, newPassword) => {
    const response = await axios.post(`${API_BASE_URL}/reset-password`, {
      token,
      newPassword
    });
    return response.data;
  },

  verifyOtp: async (email, otp) => {
    const response = await axios.post(`${API_BASE_URL}/verify-otp`, {
      email,
      otp
    });
    return response.data;
  },

  resendOtp: async (email) => {
    const response = await axios.post(`${API_BASE_URL}/resend-otp`, { email });
    return response.data;
  },

  logout: async () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    return { success: true, message: 'Logged out successfully' };
  },

  getCurrentUser: async () => {
    const token = localStorage.getItem('token');
    if (!token) {
      throw new Error('No token found');
    }
    
    const response = await axios.get(`${API_BASE_URL}/me`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    return response.data;
  }
};

// Axios interceptors
axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

axios.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default authApi;
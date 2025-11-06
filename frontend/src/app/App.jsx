import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Homepage from '../features/home/pages/Homepage';
import Login from '../features/auth/pages/Login/Login';
import Register from '../features/auth/pages/Register/Register';
import ForgotPassword from '../features/auth/pages/ForgotPassword/ForgotPassword';
import Dashboard from '../features/user/pages/Dashboard/Dashboard';
import { MockTestsHome, TestAttempt, TestResults } from '../features/mock-tests';
import './App.css';

// Temporary placeholder components
const InterviewsPage = () => <div>Interviews Module - Coming Soon!</div>;
const BanterArenaPage = () => <div>Banter Arena Module - Coming Soon!</div>;
const AICoachPage = () => <div>AI Coach Module - Coming Soon!</div>;

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          {/* Homepage Route */}
          <Route path="/" element={<Homepage />} />
          
          {/* Public Auth Routes */}
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/forgot-password" element={<ForgotPassword />} />
          
          {/* Protected Dashboard Routes */}
          <Route path="/dashboard" element={<Dashboard />} />
          
          {/* Mock Tests Routes */}
          <Route path="/mock-tests" element={<MockTestsHome />} />
          <Route path="/mock-tests/attempt/:testId" element={<TestAttempt />} />
          <Route path="/mock-tests/results/:attemptId" element={<TestResults />} />
          
          {/* Other Module Routes */}
          <Route path="/interviews" element={<InterviewsPage />} />
          <Route path="/banter-arena" element={<BanterArenaPage />} />
          <Route path="/ai-coach" element={<AICoachPage />} />
          
          {/* Catch all route - redirect to dashboard if logged in, else homepage */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
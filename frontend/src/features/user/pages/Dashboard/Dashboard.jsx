import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useMockTestData } from '../../../mock-tests/hooks/useMockTestData';
import './Dashboard.css';

const Dashboard = () => {
  const navigate = useNavigate();
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  const { mockTestData, loading: mockTestLoading } = useMockTestData();
  
  const [dashboardData, setDashboardData] = useState({
    stats: {
      testsCompleted: 0,
      averageScore: '0%',
      skillsImproved: 0,
      hoursPracticed: 0
    },
    modules: {
      mockTests: { 
        progress: 0, 
        available: 0 
      },
      interviews: { progress: 0, available: 0 },
      banterArena: { progress: 0, active: 0 },
      aiCoach: { progress: 0 }
    },
    recentActivity: []
  });

  // Update dashboard data when mockTestData changes
  useEffect(() => {
    setDashboardData(prev => ({
      ...prev,
      stats: {
        ...prev.stats,
        testsCompleted: mockTestData.completedTests,
        averageScore: `${mockTestData.averageScore}%`
      },
      modules: {
        ...prev.modules,
        mockTests: { 
          progress: mockTestData.progress, 
          available: mockTestData.availableTests 
        }
      }
    }));
  }, [mockTestData]);

  // Fetch additional dashboard data
  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        // TODO: Replace with actual API calls for other data
        setDashboardData(prev => ({
          ...prev,
          stats: {
            ...prev.stats,
            skillsImproved: 0,
            hoursPracticed: 0
          },
          modules: {
            ...prev.modules,
            interviews: { progress: 0, available: 0 },
            banterArena: { progress: 0, active: 0 },
            aiCoach: { progress: 0 }
          },
          recentActivity: []
        }));
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      }
    };

    fetchDashboardData();
  }, []);

  const modules = [
    {
      id: 'mock-tests',
      title: 'Mock Tests',
      icon: '📊',
      description: 'Practice with realistic tests and get AI-powered feedback',
      color: '#7C3AED',
      stats: `${dashboardData.modules.mockTests.available} Tests Available`,
      progress: dashboardData.modules.mockTests.progress
    },
    {
      id: 'interviews',
      title: 'Live Interviews',
      icon: '🎥',
      description: 'Simulate real interview scenarios with video recording',
      color: '#FBBF24',
      stats: `${dashboardData.modules.interviews.available} Interview Types`,
      progress: dashboardData.modules.interviews.progress
    },
    {
      id: 'banter-arena',
      title: 'Banter Arena',
      icon: '💬',
      description: 'Connect with peers and join community discussions',
      color: '#FB923C',
      stats: `${dashboardData.modules.banterArena.active} Active Users`,
      progress: dashboardData.modules.banterArena.progress
    },
    {
      id: 'ai-coach',
      title: 'AI Coach',
      icon: '🤖',
      description: 'Get personalized guidance and performance analysis',
      color: '#8B5CF6',
      stats: '24/7 Available',
      progress: dashboardData.modules.aiCoach.progress
    }
  ];

  const stats = [
    { value: dashboardData.stats.testsCompleted.toString(), label: 'Tests Completed', color: '#7C3AED' },
    { value: dashboardData.stats.averageScore, label: 'Avg. Score', color: '#FBBF24' },
    { value: dashboardData.stats.skillsImproved.toString(), label: 'Skills Improved', color: '#FB923C' },
    { value: dashboardData.stats.hoursPracticed.toString(), label: 'Hours Practiced', color: '#8B5CF6' }
  ];

  const handleModuleClick = (moduleId) => {
    if (moduleId === 'mock-tests') {
      navigate('/mock-tests');
    } else if (moduleId === 'interviews') {
      navigate('/interviews');
    } else if (moduleId === 'banter-arena') {
      navigate('/banter-arena');
    } else if (moduleId === 'ai-coach') {
      navigate('/ai-coach');
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = '/login';
  };

  return (
    <div className="dashboard">
      {/* Navigation Header */}
      <nav className="dashboard-nav">
        <div className="nav-container">
          <div className="nav-brand">
            <div className="logo">
              <span className="logo-icon">🚀</span>
              <span className="logo-text">InterviewPro</span>
            </div>
            <div className="nav-divider"></div>
            <div className="nav-title">Dashboard</div>
          </div>

          {/* Centered Navigation Buttons */}
          <div className="nav-center">
            <button 
              className="nav-btn active"
              onClick={() => navigate('/dashboard')}
            >
              <span className="btn-icon">📊</span>
              Dashboard
            </button>
            <button 
              className="nav-btn"
              onClick={() => navigate('/mock-tests')}
            >
              <span className="btn-icon">📝</span>
              Mock Tests
            </button>
            <button 
              className="nav-btn"
              onClick={() => navigate('/interviews')}
            >
              <span className="btn-icon">🎥</span>
              Interviews
            </button>
            <button 
              className="nav-btn"
              onClick={() => navigate('/banter-arena')}
            >
              <span className="btn-icon">💬</span>
              Banter
            </button>
            <button 
              className="nav-btn"
              onClick={() => navigate('/ai-coach')}
            >
              <span className="btn-icon">🤖</span>
              AI Coach
            </button>
          </div>

          {/* User Section */}
          <div className="nav-user">
            <div className="user-info">
              <div className="user-avatar">
                {user.fullName?.[0] || user.username?.[0] || 'U'}
              </div>
              <div className="user-details">
                <span className="user-name">{user.fullName || user.username}</span>
                <span className="user-role">Member</span>
              </div>
            </div>
            <button className="logout-btn" onClick={handleLogout}>
              <span className="logout-icon">🚪</span>
            </button>
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <main className="dashboard-main">
        {/* Welcome Section */}
        <section className="welcome-section">
          <div className="welcome-content">
            <div className="welcome-text">
              <h1>Welcome, {user.fullName || user.username}! 👋</h1>
              <p>Start your interview preparation journey. Track your progress as you learn and grow.</p>
            </div>
            <div className="welcome-stats">
              {stats.map((stat, index) => (
                <div key={index} className="stat-card">
                  <div 
                    className="stat-value" 
                    style={{ color: stat.color }}
                  >
                    {stat.value}
                  </div>
                  <div className="stat-label">{stat.label}</div>
                </div>
              ))}
            </div>
          </div>
        </section>

        {/* Modules Grid */}
        <section className="modules-section">
          <div className="section-header">
            <h2>Practice Modules</h2>
            <p>Choose your path to interview mastery</p>
          </div>
          
          <div className="modules-grid">
            {modules.map((module) => (
              <div 
                key={module.id}
                className="module-card"
                onClick={() => handleModuleClick(module.id)}
                style={{ '--accent-color': module.color }}
              >
                <div className="card-header">
                  <div className="module-icon">{module.icon}</div>
                  <div className="module-stats">{module.stats}</div>
                </div>
                
                <div className="card-content">
                  <h3 className="module-title">{module.title}</h3>
                  <p className="module-description">{module.description}</p>
                  
                  <div className="progress-section">
                    <div className="progress-info">
                      <span>Progress</span>
                      <span>{module.progress}%</span>
                    </div>
                    <div className="progress-bar">
                      <div 
                        className="progress-fill"
                        style={{ 
                          width: `${module.progress}%`,
                          backgroundColor: module.color
                        }}
                      ></div>
                    </div>
                  </div>
                </div>
                
                <div className="card-action">
                  <button className="action-btn">
                    Start Practice
                    <span className="action-arrow">→</span>
                  </button>
                </div>
                
                <div className="card-glow" style={{ backgroundColor: module.color }}></div>
              </div>
            ))}
          </div>
        </section>

        {/* Recent Activity & Quick Actions */}
        <section className="bottom-section">
          <div className="activity-panel">
            <div className="panel-header">
              <h3>Recent Activity</h3>
              <button className="view-all-btn">View All</button>
            </div>
            
            <div className="activity-list">
              {dashboardData.recentActivity.length === 0 ? (
                <div className="no-activity">
                  <div className="no-activity-icon">📊</div>
                  <div className="no-activity-text">
                    <h4>No activity yet</h4>
                    <p>Start practicing to see your progress here</p>
                  </div>
                </div>
              ) : (
                dashboardData.recentActivity.map((activity) => (
                  <div key={activity.id} className="activity-item">
                    <div className="activity-icon">{activity.icon}</div>
                    <div className="activity-content">
                      <h4>{activity.action}</h4>
                      <p>{activity.module} • {activity.time}</p>
                    </div>
                    {activity.score && (
                      <div 
                        className="activity-score"
                        style={{ backgroundColor: `${activity.module === 'Mock Tests' ? '#7C3AED' : '#8B5CF6'}20`, color: activity.module === 'Mock Tests' ? '#7C3AED' : '#8B5CF6' }}
                      >
                        {activity.score}
                      </div>
                    )}
                  </div>
                ))
              )}
            </div>
          </div>

          <div className="quick-actions-panel">
            <div className="panel-header">
              <h3>Quick Actions</h3>
            </div>
            
            <div className="actions-grid">
              <button className="quick-action" onClick={() => navigate('/mock-tests')}>
                <span className="action-icon">⚡</span>
                <span className="action-text">Quick Test</span>
              </button>
              <button className="quick-action">
                <span className="action-icon">📈</span>
                <span className="action-text">Progress Report</span>
              </button>
              <button className="quick-action">
                <span className="action-icon">🎯</span>
                <span className="action-text">Skill Assessment</span>
              </button>
              <button className="quick-action">
                <span className="action-icon">📚</span>
                <span className="action-text">Study Materials</span>
              </button>
            </div>
          </div>
        </section>
      </main>
    </div>
  );
};

export default Dashboard;
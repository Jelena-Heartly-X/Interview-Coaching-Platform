import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useMockTest } from '../../hooks/useMockTest';
import { mockTestApi } from '../../services/mockTestApi';
import './MockTestsHome.css';

const MockTestsHome = () => {
  const navigate = useNavigate();
  const { startTest, loading } = useMockTest();
  const [tests, setTests] = useState([]);
  const [filter, setFilter] = useState('all');

  useEffect(() => {
    const fetchTests = async () => {
      try {
        const availableTests = await mockTestApi.getAvailableTests();
        setTests(availableTests);
      } catch (error) {
        console.error('Error fetching tests:', error);
      }
    };

    fetchTests();
  }, []);

  const filteredTests = filter === 'all' 
    ? tests 
    : tests.filter(test => test.difficulty === filter);

  const getDifficultyColor = (difficulty) => {
    switch (difficulty) {
      case 'beginner': return '#10B981';
      case 'intermediate': return '#F59E0B';
      case 'advanced': return '#EF4444';
      default: return '#6B7280';
    }
  };

  const getDifficultyIcon = (difficulty) => {
    switch (difficulty) {
      case 'beginner': return '🌱';
      case 'intermediate': return '🚀';
      case 'advanced': return '🔥';
      default: return '📚';
    }
  };

  return (
    <div className="mock-tests-home">
      {/* Header */}
      <header className="mock-tests-header">
        <div className="header-content">
          <div className="header-text">
            <h1>Mock Tests</h1>
            <p>Practice with realistic tests and track your progress</p>
          </div>
          <div className="header-stats">
            <div className="stat">
              <span className="stat-value">{tests.length}</span>
              <span className="stat-label">Available Tests</span>
            </div>
            <div className="stat">
              <span className="stat-value">30m+</span>
              <span className="stat-label">Practice Time</span>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="mock-tests-main">
        {/* Filters */}
        <div className="filters-section">
          <div className="filter-buttons">
            <button 
              className={`filter-btn ${filter === 'all' ? 'active' : ''}`}
              onClick={() => setFilter('all')}
            >
              All Tests
            </button>
            <button 
              className={`filter-btn ${filter === 'beginner' ? 'active' : ''}`}
              onClick={() => setFilter('beginner')}
            >
              🌱 Beginner
            </button>
            <button 
              className={`filter-btn ${filter === 'intermediate' ? 'active' : ''}`}
              onClick={() => setFilter('intermediate')}
            >
              🚀 Intermediate
            </button>
            <button 
              className={`filter-btn ${filter === 'advanced' ? 'active' : ''}`}
              onClick={() => setFilter('advanced')}
            >
              🔥 Advanced
            </button>
          </div>
        </div>

        {/* Tests Grid */}
        <div className="tests-grid">
          {filteredTests.map((test) => (
            <div key={test.id} className="test-card">
              <div className="card-header">
                <div className="test-difficulty">
                  <span 
                    className="difficulty-icon"
                    style={{ color: getDifficultyColor(test.difficulty) }}
                  >
                    {getDifficultyIcon(test.difficulty)}
                  </span>
                  <span 
                    className="difficulty-badge"
                    style={{ 
                      backgroundColor: getDifficultyColor(test.difficulty) + '20',
                      color: getDifficultyColor(test.difficulty)
                    }}
                  >
                    {test.difficulty}
                  </span>
                </div>
                <div className="test-duration">
                  ⏱️ {test.duration} min
                </div>
              </div>

              <div className="card-content">
                <h3 className="test-title">{test.title}</h3>
                <p className="test-description">{test.description}</p>
                
                <div className="test-meta">
                  <div className="meta-item">
                    <span className="meta-icon">📊</span>
                    <span>{test.totalQuestions} questions</span>
                  </div>
                  <div className="meta-item">
                    <span className="meta-icon">🎯</span>
                    <span>{test.passingScore}% to pass</span>
                  </div>
                </div>
              </div>

              <div className="card-actions">
                <button 
                  className="start-test-btn"
                  onClick={() => startTest(test.id)}
                  disabled={loading}
                >
                  {loading ? 'Starting...' : 'Start Test'}
                </button>
                <button className="preview-btn">
                  Preview
                </button>
              </div>
            </div>
          ))}
        </div>

        {/* Empty State */}
        {filteredTests.length === 0 && (
          <div className="empty-state">
            <div className="empty-icon">📚</div>
            <h3>No tests found</h3>
            <p>Try changing your filters or check back later for new tests.</p>
          </div>
        )}
      </main>
    </div>
  );
};

export default MockTestsHome;
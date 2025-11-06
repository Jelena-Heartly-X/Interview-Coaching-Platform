import React, { useState } from 'react';
import { mockTestApi } from '../services/mockTestApi';

const DebugPanel = () => {
  const [results, setResults] = useState({});
  const [loading, setLoading] = useState(false);

  const testEndpoint = async (name, apiCall) => {
    setLoading(true);
    try {
      const response = await apiCall();
      setResults(prev => ({
        ...prev,
        [name]: {
          success: true,
          data: response.data,
          count: Array.isArray(response.data) ? response.data.length : 'N/A'
        }
      }));
    } catch (error) {
      setResults(prev => ({
        ...prev,
        [name]: {
          success: false,
          error: error.message,
          details: error.response?.data
        }
      }));
    } finally {
      setLoading(false);
    }
  };

  const runAllTests = async () => {
    setResults({});
    await testEndpoint('Active Tests', mockTestApi.getActiveTests);
    await testEndpoint('Active Courses', mockTestApi.getActiveCourses);
    await testEndpoint('All Tests', mockTestApi.getAllTests);
    await testEndpoint('All Courses', mockTestApi.getAllCourses);
  };

  return (
    <div style={{
      position: 'fixed',
      bottom: '20px',
      right: '20px',
      background: 'rgba(0,0,0,0.9)',
      color: 'white',
      padding: '20px',
      borderRadius: '10px',
      maxWidth: '400px',
      maxHeight: '80vh',
      overflow: 'auto',
      zIndex: 9999,
      fontSize: '12px'
    }}>
      <h3 style={{ marginTop: 0 }}>🔍 Debug Panel</h3>
      
      <button 
        onClick={runAllTests}
        disabled={loading}
        style={{
          background: '#7C3AED',
          color: 'white',
          border: 'none',
          padding: '10px 20px',
          borderRadius: '5px',
          cursor: 'pointer',
          marginBottom: '15px',
          width: '100%'
        }}
      >
        {loading ? 'Testing...' : 'Run All Tests'}
      </button>

      {Object.entries(results).map(([name, result]) => (
        <div key={name} style={{
          marginBottom: '15px',
          padding: '10px',
          background: result.success ? 'rgba(16,185,129,0.2)' : 'rgba(239,68,68,0.2)',
          borderRadius: '5px'
        }}>
          <strong>{name}:</strong>
          {result.success ? (
            <div>
              ✅ Success
              <br />
              Count: {result.count}
              <br />
              <details>
                <summary>View Data</summary>
                <pre style={{ fontSize: '10px', overflow: 'auto', maxHeight: '200px' }}>
                  {JSON.stringify(result.data, null, 2)}
                </pre>
              </details>
            </div>
          ) : (
            <div>
              ❌ Error: {result.error}
              <br />
              <pre style={{ fontSize: '10px' }}>
                {JSON.stringify(result.details, null, 2)}
              </pre>
            </div>
          )}
        </div>
      ))}

      <div style={{ marginTop: '15px', paddingTop: '15px', borderTop: '1px solid #444' }}>
        <strong>Quick Checks:</strong>
        <div>Token: {localStorage.getItem('token') ? '✅ Present' : '❌ Missing'}</div>
        <div>User: {localStorage.getItem('user') ? '✅ Present' : '❌ Missing'}</div>
        <div>API URL: http://localhost:8080/api/mocktest</div>
      </div>
    </div>
  );
};

export default DebugPanel;

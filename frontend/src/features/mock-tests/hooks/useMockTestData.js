import { useState, useEffect } from 'react';

export const useMockTestData = () => {
  const [mockTestData, setMockTestData] = useState({
    completedTests: 0,
    averageScore: 0,
    progress: 0, // Start at 0 for new users
    availableTests: 0,
    recentTests: []
  });

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMockTestData = async () => {
      setLoading(true);
      try {
        // TODO: Replace with actual API calls
        setTimeout(() => {
          // For new users, everything starts at 0
          const completedTests = 0;
          const availableTests = 10; // Total available tests
          const progress = 0; // New users start with 0% progress
          const averageScore = 0; // No tests taken yet
          
          setMockTestData({
            completedTests: completedTests,
            averageScore: averageScore,
            progress: progress,
            availableTests: availableTests,
            recentTests: [] // Empty array for new users
          });
          setLoading(false);
        }, 1000);
      } catch (error) {
        console.error('Error fetching mock test data:', error);
        setLoading(false);
      }
    };

    fetchMockTestData();
  }, []);

  return { mockTestData, loading };
};
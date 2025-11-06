// Temporary mock service - replace with actual API calls later
export const mockTestApi = {
  // Get all available mock tests
  getAvailableTests: async () => {
    // Simulate API delay
    await new Promise(resolve => setTimeout(resolve, 500));
    
    return [
      {
        id: 1,
        title: 'JavaScript Fundamentals',
        description: 'Test your basic JavaScript knowledge including variables, functions, and data types',
        category: 'Programming',
        difficulty: 'beginner',
        duration: 30,
        totalQuestions: 10,
        passingScore: 70
      },
      {
        id: 2,
        title: 'React Concepts',
        description: 'Advanced React patterns, hooks, and best practices',
        category: 'Frontend',
        difficulty: 'intermediate',
        duration: 45,
        totalQuestions: 15,
        passingScore: 75
      },
      {
        id: 3,
        title: 'Algorithms & Data Structures',
        description: 'Problem solving with common algorithms and data structures',
        category: 'Computer Science',
        difficulty: 'advanced',
        duration: 60,
        totalQuestions: 20,
        passingScore: 80
      },
      {
        id: 4,
        title: 'System Design',
        description: 'Design scalable systems and architecture patterns',
        category: 'Software Engineering',
        difficulty: 'advanced',
        duration: 90,
        totalQuestions: 8,
        passingScore: 75
      },
      {
        id: 5,
        title: 'HTML & CSS',
        description: 'Web development fundamentals and responsive design',
        category: 'Frontend',
        difficulty: 'beginner',
        duration: 25,
        totalQuestions: 12,
        passingScore: 65
      },
      {
        id: 6,
        title: 'Node.js Backend',
        description: 'Server-side JavaScript, Express, middleware',
        category: 'Backend',
        difficulty: 'intermediate',
        duration: 50,
        totalQuestions: 18,
        passingScore: 75
      },
      {
        id: 7,
        title: 'Database Design',
        description: 'SQL queries, normalization, database relationships',
        category: 'Database',
        difficulty: 'intermediate',
        duration: 45,
        totalQuestions: 15,
        passingScore: 75
      },
      {
        id: 8,
        title: 'TypeScript Fundamentals',
        description: 'Static typing, interfaces, and advanced types',
        category: 'Programming',
        difficulty: 'intermediate',
        duration: 40,
        totalQuestions: 12,
        passingScore: 70
      },
      {
        id: 9,
        title: 'CSS Advanced',
        description: 'Flexbox, Grid, animations and modern CSS features',
        category: 'Frontend',
        difficulty: 'advanced',
        duration: 35,
        totalQuestions: 10,
        passingScore: 75
      },
      {
        id: 10,
        title: 'Behavioral Questions',
        description: 'Soft skills, teamwork, problem-solving scenarios',
        category: 'Soft Skills',
        difficulty: 'beginner',
        duration: 30,
        totalQuestions: 8,
        passingScore: 70
      }
    ];
  },

  // Start a new test attempt
  startTest: async (testId) => {
    await new Promise(resolve => setTimeout(resolve, 300));
    return { 
      attemptId: Date.now(), 
      testId,
      questions: [],
      startedAt: new Date().toISOString()
    };
  },

  // Submit test answers
  submitTest: async (attemptId, answers) => {
    await new Promise(resolve => setTimeout(resolve, 500));
    return { 
      score: 0, // Start with 0 for first attempt
      totalQuestions: 10,
      correctAnswers: 0,
      timeTaken: 0,
      results: {} 
    };
  },

  // Get test results
  getResults: async (attemptId) => {
    await new Promise(resolve => setTimeout(resolve, 300));
    return { 
      score: 0, 
      analysis: {},
      recommendations: []
    };
  },

  // Get user's test history - empty for new users
  getTestHistory: async () => {
    await new Promise(resolve => setTimeout(resolve, 400));
    return []; // Empty array for new users
  }
};
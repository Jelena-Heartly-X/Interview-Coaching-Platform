-- Interview Module Database Schema
-- PostgreSQL

-- Interview Slots Table
CREATE TABLE IF NOT EXISTS interview_slots (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    topic VARCHAR(100) NOT NULL,
    difficulty_level VARCHAR(20) NOT NULL,
    duration_minutes INTEGER NOT NULL DEFAULT 60,
    scheduled_date_time TIMESTAMP,
    max_participants INTEGER DEFAULT 1,
    mentor_id BIGINT REFERENCES users(id),
    status VARCHAR(20) DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Questions Table
CREATE TABLE IF NOT EXISTS questions (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    description TEXT NOT NULL,
    question_type VARCHAR(50) NOT NULL,
    category VARCHAR(100) NOT NULL,
    sub_category VARCHAR(100),
    difficulty_level VARCHAR(20) NOT NULL,
    time_limit_seconds INTEGER DEFAULT 900,
    points INTEGER DEFAULT 10,
    hints TEXT,
    solution TEXT,
    code_template TEXT,
    constraints_info TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Test Cases Table
CREATE TABLE IF NOT EXISTS test_cases (
    id BIGSERIAL PRIMARY KEY,
    question_id BIGINT REFERENCES questions(id) ON DELETE CASCADE,
    input_data TEXT NOT NULL,
    expected_output TEXT NOT NULL,
    is_sample BOOLEAN DEFAULT FALSE,
    is_hidden BOOLEAN DEFAULT FALSE,
    points INTEGER DEFAULT 1,
    explanation TEXT
);

-- Interviews Table
CREATE TABLE IF NOT EXISTS interviews (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) NOT NULL,
    slot_id BIGINT REFERENCES interview_slots(id),
    title VARCHAR(200),
    topic VARCHAR(100),
    difficulty_level VARCHAR(20),
    status VARCHAR(20) DEFAULT 'SCHEDULED',
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    total_questions INTEGER DEFAULT 0,
    attempted_questions INTEGER DEFAULT 0,
    correct_answers INTEGER DEFAULT 0,
    total_score INTEGER DEFAULT 0,
    max_score INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Interview Responses Table
CREATE TABLE IF NOT EXISTS interview_responses (
    id BIGSERIAL PRIMARY KEY,
    interview_id BIGINT REFERENCES interviews(id) ON DELETE CASCADE NOT NULL,
    question_id BIGINT REFERENCES questions(id),
    user_answer TEXT,
    code_submission TEXT,
    programming_language VARCHAR(50),
    is_correct BOOLEAN DEFAULT FALSE,
    score_obtained INTEGER DEFAULT 0,
    max_score INTEGER DEFAULT 10,
    time_taken_seconds INTEGER,
    hints_used INTEGER DEFAULT 0,
    test_cases_passed INTEGER DEFAULT 0,
    test_cases_total INTEGER DEFAULT 0,
    execution_output TEXT,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Interview Evaluations Table
CREATE TABLE IF NOT EXISTS interview_evaluations (
    id BIGSERIAL PRIMARY KEY,
    interview_id BIGINT REFERENCES interviews(id) ON DELETE CASCADE NOT NULL,
    overall_score INTEGER,
    code_quality_score INTEGER,
    logical_reasoning_score INTEGER,
    time_management_score INTEGER,
    problem_solving_score INTEGER,
    ai_feedback TEXT,
    strengths TEXT,
    weaknesses TEXT,
    improvement_suggestions TEXT,
    confidence_level VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User Skills Analytics Table
CREATE TABLE IF NOT EXISTS user_skills_analytics (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) NOT NULL,
    topic VARCHAR(100) NOT NULL,
    sub_category VARCHAR(100),
    total_attempts INTEGER DEFAULT 0,
    successful_attempts INTEGER DEFAULT 0,
    accuracy_percentage DECIMAL(5,2) DEFAULT 0.0,
    average_time_seconds INTEGER,
    last_attempted_at TIMESTAMP,
    skill_level VARCHAR(20),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, topic)
);

-- Indexes for Performance
CREATE INDEX IF NOT EXISTS idx_interview_slots_status ON interview_slots(status);
CREATE INDEX IF NOT EXISTS idx_interview_slots_topic ON interview_slots(topic);
CREATE INDEX IF NOT EXISTS idx_questions_category ON questions(category, difficulty_level);
CREATE INDEX IF NOT EXISTS idx_interviews_user ON interviews(user_id, status);
CREATE INDEX IF NOT EXISTS idx_user_analytics_user_topic ON user_skills_analytics(user_id, topic);

-- Sample Questions (DSA Category)
INSERT INTO questions (title, description, question_type, category, sub_category, difficulty_level, time_limit_seconds, points, hints, code_template, constraints_info) VALUES
('Reverse a Linked List', 'Given the head of a singly linked list, reverse the list and return the reversed list.', 'CODING', 'DSA', 'Linked Lists', 'INTERMEDIATE', 900, 20, 'Use iterative approach with 3 pointers: prev, current, and next', 'class ListNode {\n  int val;\n  ListNode next;\n}\n\npublic ListNode reverseList(ListNode head) {\n  // Your code here\n}', 'Number of nodes: [0, 5000], Node values: [-5000, 5000]'),
('Two Sum', 'Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.', 'CODING', 'DSA', 'Arrays', 'BEGINNER', 600, 15, 'Use a HashMap to store visited numbers', 'public int[] twoSum(int[] nums, int target) {\n  // Your code here\n}', 'Array length: [2, 10^4], Values: [-10^9, 10^9]'),
('Binary Tree Level Order Traversal', 'Given the root of a binary tree, return the level order traversal of its nodes'' values.', 'CODING', 'DSA', 'Trees', 'INTERMEDIATE', 1200, 25, 'Use BFS with a queue', 'public List<List<Integer>> levelOrder(TreeNode root) {\n  // Your code here\n}', 'Nodes: [0, 2000], Node values: [-1000, 1000]'),
('What is Normalization?', 'Explain database normalization and its different normal forms.', 'THEORETICAL', 'DBMS', 'Normalization', 'BEGINNER', 600, 10, 'Discuss 1NF, 2NF, 3NF, and BCNF', NULL, NULL),
('Design Twitter', 'Design a simplified version of Twitter with the ability to post tweets, follow users, and view timeline.', 'SYSTEM_DESIGN', 'SYSTEM_DESIGN', 'Social Media', 'ADVANCED', 1800, 30, 'Consider scalability, database design, caching, and API design', NULL, 'Focus on core features: posting, following, timeline generation');

-- Sample Test Cases for "Two Sum"
INSERT INTO test_cases (question_id, input_data, expected_output, is_sample, points, explanation) VALUES
(2, '[2,7,11,15], target=9', '[0,1]', TRUE, 5, 'nums[0] + nums[1] = 2 + 7 = 9'),
(2, '[3,2,4], target=6', '[1,2]', TRUE, 5, 'nums[1] + nums[2] = 2 + 4 = 6'),
(2, '[3,3], target=6', '[0,1]', FALSE, 5, 'Same number used twice');

-- Sample Interview Slots
INSERT INTO interview_slots (title, description, topic, difficulty_level, duration_minutes, status) VALUES
('DSA Mock Interview - Arrays & Strings', 'Practice interview focused on array and string problems', 'DSA', 'INTERMEDIATE', 60, 'AVAILABLE'),
('System Design - Scalability', 'Design scalable distributed systems', 'SYSTEM_DESIGN', 'ADVANCED', 90, 'AVAILABLE'),
('DBMS Fundamentals', 'Database management concepts and SQL', 'DBMS', 'BEGINNER', 45, 'AVAILABLE');

-- mock.sql
-- Complete Mock Test Database Schema and Data for Interview Coaching Platform
-- Run this file in your PostgreSQL database to set up the entire mock test module

-- =============================================
-- 1. ENUM CREATION
-- =============================================

-- Create enum for difficulty levels
DO $$ BEGIN
    CREATE TYPE difficulty_level AS ENUM ('easy', 'medium', 'hard');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

-- Create enum for question types
DO $$ BEGIN
    CREATE TYPE question_type AS ENUM ('mcq', 'coding', 'system_design', 'behavioral', 'short_answer');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

-- =============================================
-- 2. TABLE CREATION
-- =============================================

-- Table for subjects/topics
CREATE TABLE IF NOT EXISTS subjects (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    icon VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Enhanced questions table
CREATE TABLE IF NOT EXISTS questions (
    id SERIAL PRIMARY KEY,
    subject_id INTEGER REFERENCES subjects(id) ON DELETE CASCADE,
    difficulty difficulty_level NOT NULL,
    question_type question_type DEFAULT 'mcq',
    question_text TEXT NOT NULL,
    question_description TEXT,
    options JSONB,
    correct_answer TEXT NOT NULL,
    explanation TEXT,
    solution_hint TEXT,
    tags VARCHAR(255)[],
    time_limit_seconds INTEGER DEFAULT 120,
    points INTEGER DEFAULT 1,
    times_asked INTEGER DEFAULT 0,
    times_answered_correctly INTEGER DEFAULT 0,
    times_answered_incorrectly INTEGER DEFAULT 0,
    average_time_taken DECIMAL(8,2) DEFAULT 0.00,
    success_rate DECIMAL(5,2) DEFAULT 0.00,
    is_active BOOLEAN DEFAULT TRUE,
    created_by INTEGER, -- Will reference users(id) when users table exists
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Enhanced courses table
CREATE TABLE IF NOT EXISTS courses (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    level difficulty_level,
    estimated_duration_hours INTEGER,
    prerequisites INTEGER[],
    learning_objectives TEXT[],
    target_companies VARCHAR(255)[],
    is_featured BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Enhanced mock tests table
CREATE TABLE IF NOT EXISTS mock_tests (
    id SERIAL PRIMARY KEY,
    course_id INTEGER REFERENCES courses(id) ON DELETE SET NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    instructions TEXT,
    duration_minutes INTEGER DEFAULT 60,
    total_questions INTEGER NOT NULL,
    total_points INTEGER DEFAULT 100,
    passing_score DECIMAL(5,2) DEFAULT 70.00,
    max_attempts INTEGER DEFAULT 3,
    shuffle_questions BOOLEAN DEFAULT TRUE,
    show_results_immediately BOOLEAN DEFAULT FALSE,
    is_timed BOOLEAN DEFAULT TRUE,
    is_active BOOLEAN DEFAULT TRUE,
    created_by INTEGER, -- Will reference users(id) when users table exists
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Enhanced mock test questions junction table
CREATE TABLE IF NOT EXISTS mock_test_questions (
    mock_test_id INTEGER REFERENCES mock_tests(id) ON DELETE CASCADE,
    question_id INTEGER REFERENCES questions(id) ON DELETE CASCADE,
    question_order INTEGER NOT NULL,
    points INTEGER DEFAULT 1,
    required BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (mock_test_id, question_id)
);

-- Enhanced user attempts table
CREATE TABLE IF NOT EXISTS user_mock_test_attempts (
    id SERIAL PRIMARY KEY,
    user_id INTEGER, -- Will reference users(id) when users table exists
    mock_test_id INTEGER REFERENCES mock_tests(id) ON DELETE CASCADE,
    attempt_number INTEGER DEFAULT 1,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    time_taken_seconds INTEGER,
    score DECIMAL(5,2) DEFAULT 0.00,
    total_questions INTEGER,
    correct_answers INTEGER DEFAULT 0,
    incorrect_answers INTEGER DEFAULT 0,
    skipped_questions INTEGER DEFAULT 0,
    percentage_score DECIMAL(5,2) DEFAULT 0.00,
    status VARCHAR(20) DEFAULT 'in_progress',
    feedback TEXT,
    ai_feedback JSONB,
    is_passed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Enhanced user answers table
CREATE TABLE IF NOT EXISTS user_answers (
    id SERIAL PRIMARY KEY,
    attempt_id INTEGER REFERENCES user_mock_test_attempts(id) ON DELETE CASCADE,
    question_id INTEGER REFERENCES questions(id) ON DELETE CASCADE,
    user_response TEXT,
    is_correct BOOLEAN,
    points_earned INTEGER DEFAULT 0,
    time_taken_seconds INTEGER,
    question_sequence INTEGER,
    marked_for_review BOOLEAN DEFAULT FALSE,
    confidence_level INTEGER CHECK (confidence_level >= 1 AND confidence_level <= 5),
    answered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Enhanced gamification tables
CREATE TABLE IF NOT EXISTS badges (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    icon_url VARCHAR(255),
    badge_type VARCHAR(50),
    category VARCHAR(50),
    points_required INTEGER,
    rarity VARCHAR(20) DEFAULT 'common',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_badges (
    user_id INTEGER, -- Will reference users(id) when users table exists
    badge_id INTEGER REFERENCES badges(id) ON DELETE CASCADE,
    awarded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    context TEXT,
    PRIMARY KEY (user_id, badge_id)
);

CREATE TABLE IF NOT EXISTS user_points (
    user_id INTEGER PRIMARY KEY, -- Will reference users(id) when users table exists
    total_points INTEGER DEFAULT 0,
    level INTEGER DEFAULT 1,
    experience_points INTEGER DEFAULT 0,
    tests_completed INTEGER DEFAULT 0,
    questions_answered INTEGER DEFAULT 0,
    correct_answers INTEGER DEFAULT 0,
    streak_days INTEGER DEFAULT 0,
    last_activity_date DATE,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS leaderboards (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    course_id INTEGER REFERENCES courses(id) ON DELETE SET NULL,
    start_date DATE,
    end_date DATE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS leaderboard_entries (
    leaderboard_id INTEGER REFERENCES leaderboards(id) ON DELETE CASCADE,
    user_id INTEGER, -- Will reference users(id) when users table exists
    rank INTEGER NOT NULL,
    score INTEGER NOT NULL,
    tests_completed INTEGER DEFAULT 0,
    average_score DECIMAL(5,2) DEFAULT 0.00,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (leaderboard_id, user_id)
);

-- User progress tracking
CREATE TABLE IF NOT EXISTS user_subject_progress (
    user_id INTEGER, -- Will reference users(id) when users table exists
    subject_id INTEGER REFERENCES subjects(id) ON DELETE CASCADE,
    questions_attempted INTEGER DEFAULT 0,
    correct_answers INTEGER DEFAULT 0,
    average_score DECIMAL(5,2) DEFAULT 0.00,
    average_time_taken DECIMAL(8,2) DEFAULT 0.00,
    strength_level INTEGER DEFAULT 1,
    last_practiced TIMESTAMP,
    PRIMARY KEY (user_id, subject_id)
);

-- =============================================
-- 3. INDEXES FOR PERFORMANCE
-- =============================================

CREATE INDEX IF NOT EXISTS idx_questions_subject_difficulty ON questions(subject_id, difficulty);
CREATE INDEX IF NOT EXISTS idx_questions_tags ON questions USING GIN(tags);
CREATE INDEX IF NOT EXISTS idx_questions_success_rate ON questions(success_rate);
CREATE INDEX IF NOT EXISTS idx_user_attempts_user_test ON user_mock_test_attempts(user_id, mock_test_id);
CREATE INDEX IF NOT EXISTS idx_user_attempts_status ON user_mock_test_attempts(status);
CREATE INDEX IF NOT EXISTS idx_user_answers_attempt_question ON user_answers(attempt_id, question_id);
CREATE INDEX IF NOT EXISTS idx_user_progress_user_subject ON user_subject_progress(user_id, subject_id);
CREATE INDEX IF NOT EXISTS idx_leaderboard_entries_score ON leaderboard_entries(score DESC);
CREATE INDEX IF NOT EXISTS idx_user_points_total ON user_points(total_points DESC);

-- =============================================
-- 4. FUNCTIONS AND TRIGGERS
-- =============================================

-- Update timestamp function
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Apply triggers to all tables with updated_at
CREATE OR REPLACE TRIGGER update_subjects_timestamp 
    BEFORE UPDATE ON subjects 
    FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE OR REPLACE TRIGGER update_questions_timestamp 
    BEFORE UPDATE ON questions 
    FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE OR REPLACE TRIGGER update_courses_timestamp 
    BEFORE UPDATE ON courses 
    FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE OR REPLACE TRIGGER update_mock_tests_timestamp 
    BEFORE UPDATE ON mock_tests 
    FOR EACH ROW EXECUTE FUNCTION update_timestamp();

-- Function to calculate success rate
CREATE OR REPLACE FUNCTION update_question_success_rate()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE questions 
    SET success_rate = CASE 
        WHEN times_asked > 0 THEN (times_answered_correctly::DECIMAL / times_asked) * 100 
        ELSE 0 
    END
    WHERE id = NEW.question_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER update_success_rate 
    AFTER INSERT OR UPDATE ON user_answers 
    FOR EACH ROW EXECUTE FUNCTION update_question_success_rate();

-- =============================================
-- 5. DATA POPULATION
-- =============================================

-- Clear existing data (optional - comment out if you want to keep existing data)
-- TRUNCATE TABLE subjects, questions, courses, mock_tests, mock_test_questions, badges CASCADE;

-- Insert Subjects
INSERT INTO subjects (name, description, icon) VALUES
('DBMS', 'Database Management Systems', '🗄️'),
('OOPS', 'Object-Oriented Programming', '🔄'),
('OS', 'Operating Systems', '💻'),
('Linear Algebra', 'Mathematical concepts in linear algebra', '📐'),
('Statistics', 'Statistical methods and analysis', '📊'),
('Probability', 'Probability theory', '🎲'),
('Stochastic Models', 'Stochastic processes and models', '📈'),
('Algorithms', 'Algorithm design techniques and analysis', '⚡'),
('DSA', 'Data Structures and Algorithms', '🛠️'),
('Computer Networks', 'Computer networking concepts', '🌐'),
('System Design', 'Software architecture and design patterns', '🏗️'),
('Behavioral', 'Soft skills and situational questions', '💬')
ON CONFLICT (name) DO NOTHING;

-- Insert Courses
INSERT INTO courses (name, description, level, estimated_duration_hours, prerequisites, learning_objectives, target_companies, is_featured) VALUES
('Full Stack Developer Prep', 'Comprehensive full-stack development interview preparation', 'medium', 40, 
 '{}', 
 '{"Master frontend concepts", "Understand backend architecture", "Learn database design", "Practice system design"}',
 '{"Google", "Meta", "Amazon", "Microsoft"}', TRUE),
('Data Structures Mastery', 'Deep dive into data structures and algorithms', 'hard', 60,
 '{}',
 '{"Master complex data structures", "Solve 100+ coding problems", "Optimize time/space complexity", "Ace technical interviews"}',
 '{"Google", "Amazon", "Apple", "Netflix"}', TRUE),
('System Design Pro', 'Advanced system design and architecture', 'hard', 45,
 '{2}',
 '{"Design scalable systems", "Understand microservices", "Learn caching strategies", "Master database scaling"}',
 '{"Google", "Amazon", "Microsoft", "Uber"}', TRUE),
('Behavioral Interview Prep', 'Master soft skills and situational questions', 'easy', 15,
 '{}',
 '{"Answer STAR questions", "Handle pressure situations", "Communicate effectively", "Show leadership skills"}',
 '{"All Top Tech Companies"}', FALSE),
('Database Expert', 'Advanced database concepts and optimization', 'medium', 30,
 '{}',
 '{"Master SQL optimization", "Understand indexing", "Learn transaction management", "Design efficient schemas"}',
 '{"Oracle", "Amazon", "Google", "Snowflake"}', FALSE)
ON CONFLICT DO NOTHING;

-- Insert Mock Tests
INSERT INTO mock_tests (course_id, name, description, instructions, duration_minutes, total_questions, total_points, passing_score, max_attempts, shuffle_questions, is_timed, is_active) VALUES
(1, 'Full Stack Fundamentals', 'Test your full-stack development knowledge', 'Answer all questions. You have 60 minutes. Good luck!', 60, 20, 100, 70.0, 3, TRUE, TRUE, TRUE),
(1, 'Advanced Full Stack', 'Challenging full-stack concepts', 'Focus on system design and optimization questions', 90, 25, 125, 75.0, 2, TRUE, TRUE, TRUE),
(2, 'DSA Beginner Test', 'Basic data structures and algorithms', 'Perfect for beginners starting their DSA journey', 45, 15, 75, 65.0, 5, TRUE, TRUE, TRUE),
(2, 'DSA Advanced Challenge', 'Advanced algorithms and complex problems', 'For experienced developers preparing for FAANG', 120, 30, 150, 80.0, 3, TRUE, TRUE, TRUE),
(3, 'System Design Basics', 'Fundamental system design concepts', 'Focus on scalability and architecture patterns', 60, 20, 100, 70.0, 3, TRUE, TRUE, TRUE),
(4, 'Behavioral Assessment', 'Soft skills and situational judgment', 'Be honest and thoughtful in your responses', 30, 10, 50, 60.0, 1, FALSE, FALSE, TRUE)
ON CONFLICT DO NOTHING;

-- Insert Questions (25 core questions)
INSERT INTO questions (subject_id, difficulty, question_type, question_text, question_description, options, correct_answer, explanation, solution_hint, tags, time_limit_seconds, points) VALUES
-- DBMS Questions
(1, 'easy', 'mcq', 'What does ACID stand for in database transactions?', 'Understanding transaction properties', '{"A": "Atomicity, Consistency, Isolation, Durability", "B": "Availability, Consistency, Integrity, Durability", "C": "Atomicity, Concurrency, Isolation, Durability", "D": "Automation, Consistency, Integrity, Data"}', 'A', 'ACID properties ensure reliable transaction processing. Atomicity: all or nothing, Consistency: valid state transition, Isolation: concurrent transactions don''t interfere, Durability: committed transactions persist.', 'Think about what each letter represents in transaction reliability', '{"transactions", "acid", "database"}', 90, 1),
(1, 'medium', 'mcq', 'Which normal form eliminates transitive dependencies?', 'Database normalization concepts', '{"A": "First Normal Form", "B": "Second Normal Form", "C": "Third Normal Form", "D": "Boyce-Codd Normal Form"}', 'C', 'Third Normal Form (3NF) requires that there are no transitive dependencies - all non-prime attributes must depend only on the primary key.', 'Remember the order of normal forms and what each eliminates', '{"normalization", "3nf", "database-design"}', 120, 2),
(1, 'hard', 'mcq', 'What is the difference between DELETE and TRUNCATE in SQL?', 'Understanding SQL operations', '{"A": "DELETE is DML, TRUNCATE is DDL", "B": "DELETE is faster than TRUNCATE", "C": "TRUNCATE can be rolled back", "D": "DELETE doesn''t reset identity columns"}', 'A', 'DELETE is a DML operation that can be rolled back and fires triggers. TRUNCATE is a DDL operation that cannot be rolled back, doesn''t fire triggers, and resets identity columns.', 'Consider the type of operation and their behavior in transactions', '{"sql", "delete", "truncate", "dml-ddl"}', 150, 3),

-- OOPS Questions
(2, 'easy', 'mcq', 'What is encapsulation in OOP?', 'Basic OOP concept', '{"A": "Binding data and methods together", "B": "Creating multiple forms", "C": "Inheriting properties", "D": "Hiding implementation"}', 'A', 'Encapsulation is the bundling of data and methods that operate on that data within a single unit (class), restricting direct access to some components.', 'Think about what "capsule" means - containing everything inside', '{"oop", "encapsulation", "classes"}', 60, 1),
(2, 'medium', 'mcq', 'Which principle is violated when a class has multiple responsibilities?', 'SOLID principles', '{"A": "Single Responsibility Principle", "B": "Open/Closed Principle", "C": "Liskov Substitution", "D": "Interface Segregation"}', 'A', 'The Single Responsibility Principle states that a class should have only one reason to change, meaning it should have only one job or responsibility.', 'Think about what "single responsibility" means', '{"solid", "srp", "design-principles"}', 120, 2),

-- Algorithms Questions
(8, 'easy', 'coding', 'Implement binary search', 'Write a function to perform binary search on a sorted array', '{}', 'def binary_search(arr, target):\n    left, right = 0, len(arr) - 1\n    while left <= right:\n        mid = (left + right) // 2\n        if arr[mid] == target:\n            return mid\n        elif arr[mid] < target:\n            left = mid + 1\n        else:\n            right = mid - 1\n    return -1', 'Binary search works by repeatedly dividing the search interval in half. Time complexity: O(log n), Space complexity: O(1).', 'Remember the array must be sorted. Use two pointers approach.', '{"binary-search", "searching", "algorithms"}', 300, 5),
(8, 'medium', 'coding', 'Find the longest substring without repeating characters', 'Given a string, find the length of the longest substring without repeating characters', '{}', 'def length_of_longest_substring(s):\n    char_set = set()\n    left = 0\n    max_length = 0\n    \n    for right in range(len(s)):\n        while s[right] in char_set:\n            char_set.remove(s[left])\n            left += 1\n        char_set.add(s[right])\n        max_length = max(max_length, right - left + 1)\n    return max_length', 'Use sliding window technique with two pointers and a set to track characters in current window.', 'Think about using a sliding window and a set to track unique characters', '{"sliding-window", "strings", "hash-set"}', 450, 8),

-- System Design Questions
(11, 'medium', 'system_design', 'Design Twitter feed', 'How would you design Twitter''s news feed system?', '{}', 'Key components:\n1. Load Balancer\n2. Web Servers\n3. Database Sharding\n4. Cache (Redis)\n5. Message Queue\n6. Fan-out Service', 'Consider read-heavy workload, real-time updates, and scalability. Use write-through cache and fan-out on write approach.', 'Think about read vs write patterns and how to handle millions of users', '{"system-design", "twitter", "scalability", "cache"}', 900, 15),

-- Behavioral Questions
(12, 'easy', 'behavioral', 'Tell me about a time you faced a difficult challenge', 'Standard behavioral question to assess problem-solving', '{}', 'Use STAR method:\n- Situation: Briefly describe context\n- Task: What was your responsibility\n- Action: Specific steps you took\n- Result: Outcome and learnings', 'The STAR method helps structure your answer clearly and comprehensively. Focus on your specific actions and measurable results.', 'Remember STAR: Situation, Task, Action, Result', '{"behavioral", "star", "problem-solving"}', 300, 5)
ON CONFLICT DO NOTHING;

-- Insert Badges
INSERT INTO badges (name, description, icon_url, badge_type, category, points_required, rarity) VALUES
('First Test', 'Complete your first mock test', '/badges/first-test.png', 'achievement', 'milestone', 10, 'common'),
('Perfect Score', 'Score 100% on any test', '/badges/perfect-score.png', 'achievement', 'test_performance', 50, 'rare'),
('Speed Demon', 'Complete a test in half the allotted time', '/badges/speed-demon.png', 'achievement', 'speed', 30, 'rare'),
('Consistent Learner', 'Complete 10 tests', '/badges/consistent-learner.png', 'achievement', 'consistency', 100, 'common'),
('Algorithm Master', 'Score 90%+ on DSA tests', '/badges/algo-master.png', 'skill', 'test_performance', 150, 'epic'),
('System Architect', 'Ace system design questions', '/badges/system-architect.png', 'skill', 'test_performance', 200, 'epic')
ON CONFLICT DO NOTHING;

-- Create Sample Leaderboards
INSERT INTO leaderboards (name, type, start_date, end_date, is_active) VALUES
('Global All-Time', 'global', '2024-01-01', NULL, TRUE),
('Weekly Challenge', 'weekly', CURRENT_DATE, CURRENT_DATE + 7, TRUE),
('DSA Masters', 'course_specific', '2024-01-01', NULL, TRUE)
ON CONFLICT DO NOTHING;

-- =============================================
-- 6. VERIFICATION QUERIES
-- =============================================

-- Display summary of inserted data
SELECT 
    'Mock Test Database Setup Complete!' as message,
    (SELECT COUNT(*) FROM subjects) as total_subjects,
    (SELECT COUNT(*) FROM courses) as total_courses,
    (SELECT COUNT(*) FROM mock_tests) as total_tests,
    (SELECT COUNT(*) FROM questions) as total_questions,
    (SELECT COUNT(*) FROM badges) as total_badges;

-- Display sample data
SELECT 'Sample Subjects:' as info;
SELECT name, description FROM subjects LIMIT 5;

SELECT 'Sample Questions by Type:' as info;
SELECT question_type, COUNT(*) as count 
FROM questions 
GROUP BY question_type 
ORDER BY count DESC;

SELECT 'Mock Tests Overview:' as info;
SELECT mt.name, c.name as course, mt.duration_minutes, mt.total_questions
FROM mock_tests mt 
LEFT JOIN courses c ON mt.course_id = c.id;
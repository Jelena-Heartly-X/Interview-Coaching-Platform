# 🔍 DEBUG 500 ERROR - Step-by-Step Guide

## ⚠️ **CURRENT SITUATION**

You're getting a **500 Internal Server Error** when trying to start an interview with a slot.

```
POST http://localhost:8080/api/interviews/start 500 (Internal Server Error)
Request: {topic: 'DSA', difficultyLevel: 'INTERMEDIATE', questionCount: 5, slotId: 1}
```

---

## 🎯 **I'VE ADDED DETAILED LOGGING**

The backend now has comprehensive logging to capture the **EXACT ERROR**.

### **Changes Made:**

1. ✅ **InterviewController.java** - Added detailed console logging
2. ✅ **InterviewStartRequest.java** - Added toString() for debugging

---

## 📋 **WHAT TO DO NOW**

### **Step 1: Backend is Running** ✅

Backend server is running on: **http://localhost:8080**

### **Step 2: Try to Start Interview Again**

```
1. Go to: http://localhost:3000/interviews
2. Click on time slot (ID 1 or any slot)
3. Select DSA + Intermediate
4. Click "Start Interview"
5. It will fail with 500 error (expected)
```

### **Step 3: CHECK BACKEND CONSOLE OUTPUT**

**IMPORTANT:** After you try to start the interview, immediately check your backend console (where `mvn spring-boot:run` is running).

You will see output like this:

```
===== START INTERVIEW REQUEST =====
Request: InterviewStartRequest{slotId=1, topic='DSA', difficultyLevel='INTERMEDIATE', questionCount=5}
Topic: DSA
Difficulty: INTERMEDIATE
Question Count: 5
Slot ID: 1
Username: testuser
User found: 1
Calling interviewService.startInterview...
===== START INTERVIEW ERROR =====
Error Type: <EXACT ERROR TYPE>
Error Message: <EXACT ERROR MESSAGE>
Stack Trace:
<FULL STACK TRACE>
===================================
```

---

## 🔎 **POSSIBLE ERRORS AND SOLUTIONS**

### **Error 1: No Questions Found**

**Error Message:**
```
No questions found for category 'DSA' with difficulty 'INTERMEDIATE'
```

**Solution:** You need to add questions to the database!

```sql
-- Run this SQL in your PostgreSQL database
INSERT INTO questions (title, description, question_type, category, difficulty_level, time_limit_seconds, points, solution, created_at)
VALUES 
('Two Sum', 'Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.', 'CODING', 'DSA', 'INTERMEDIATE', 900, 10, 'Use HashMap to store complements', CURRENT_TIMESTAMP),
('Reverse Linked List', 'Reverse a singly linked list.', 'CODING', 'DSA', 'INTERMEDIATE', 900, 10, 'Use iterative or recursive approach', CURRENT_TIMESTAMP),
('Binary Search', 'Implement binary search algorithm.', 'CODING', 'DSA', 'INTERMEDIATE', 900, 10, 'Divide and conquer approach', CURRENT_TIMESTAMP),
('What is normalization?', 'Explain database normalization and its types.', 'THEORETICAL', 'DBMS', 'INTERMEDIATE', 900, 10, 'Process of organizing data to reduce redundancy', CURRENT_TIMESTAMP),
('Explain OOP principles', 'What are the four pillars of OOP?', 'THEORETICAL', 'OOP', 'INTERMEDIATE', 900, 10, 'Encapsulation, Inheritance, Polymorphism, Abstraction', CURRENT_TIMESTAMP);
```

---

### **Error 2: DifficultyLevel Enum Mismatch**

**Error Message:**
```
IllegalArgumentException: No enum constant Question.DifficultyLevel.MEDIUM
```

**Solution:** The enum only accepts: `BEGINNER`, `INTERMEDIATE`, `ADVANCED`

Fix frontend to never send 'EASY', 'MEDIUM', 'HARD'.

---

### **Error 3: Slot Not Found**

**Error Message:**
```
Invalid or already booked slot
```

**Solution:** The slot doesn't exist or is already booked.

```sql
-- Check slots in database
SELECT * FROM interview_slots WHERE id = 1;

-- Create a slot if none exist
INSERT INTO interview_slots (title, description, topic, difficulty_level, duration_minutes, scheduled_date_time, status, booked, created_at, updated_at)
VALUES 
('Mock Interview - DSA', 'Practice interview for Data Structures', 'DSA', 'INTERMEDIATE', 60, '2025-11-10 10:00:00', 'AVAILABLE', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
```

---

### **Error 4: User Not Found**

**Error Message:**
```
User not found with username: testuser
```

**Solution:** The user doesn't exist in database.

```sql
-- Check if user exists
SELECT * FROM users WHERE username = 'testuser';

-- If not, register through frontend: http://localhost:3000/register
-- Or insert manually (with bcrypt password)
```

---

### **Error 5: Database Connection Issues**

**Error Message:**
```
PSQLException: Connection refused
```

**Solution:** PostgreSQL is not running.

```bash
# Check if PostgreSQL is running
# Windows: Check Services
services.msc

# Or check if port 5432 is listening
netstat -ano | findstr :5432
```

**Check application.properties:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/interview_coaching
spring.datasource.username=postgres
spring.datasource.password=yourpassword
```

---

### **Error 6: JPA/Hibernate Constraint Violation**

**Error Message:**
```
ConstraintViolationException: NOT NULL constraint violated
```

**Solution:** Some required field is missing.

Check the Interview entity fields:
- `user_id` (required)
- `topic`
- `difficulty_level`
- `status`

---

## 🚀 **NEXT STEPS**

### **1. Try Starting Interview Again**

Go ahead and try to start an interview now. The backend is ready with detailed logging.

### **2. Copy the Error Output**

After you get the 500 error, **copy the entire error output** from the backend console (the part between the `=====` lines).

### **3. Share the Error**

Share the complete error message so I can provide the exact fix.

---

## 📊 **EXPECTED BACKEND OUTPUT (Success)**

When it works correctly, you should see:

```
===== START INTERVIEW REQUEST =====
Request: InterviewStartRequest{slotId=1, topic='DSA', difficultyLevel='INTERMEDIATE', questionCount=5}
Topic: DSA
Difficulty: INTERMEDIATE
Question Count: 5
Slot ID: 1
Username: testuser
User found: 1
Calling interviewService.startInterview...
Slot ID provided: 1
Slot found and available: 1
==========================================
Selecting questions for interview:
Topic: DSA
Difficulty: INTERMEDIATE
Requested count: 5
==========================================
Found 3 questions with exact match (category + difficulty)
==========================================
FINAL: Selected 3 questions (all from 'DSA' category)
1. [ID:1] Two Sum (Category: DSA, Difficulty: INTERMEDIATE)
2. [ID:2] Reverse Linked List (Category: DSA, Difficulty: INTERMEDIATE)
3. [ID:3] Binary Search (Category: DSA, Difficulty: INTERMEDIATE)
==========================================
Interview will have 3 questions
Slot 1 marked as booked by user 1
Interview created with ID: 5
===== START INTERVIEW SUCCESS =====
```

---

## 🎯 **MY HYPOTHESIS**

Based on the errors you're experiencing, I suspect **ONE OF THESE IS THE ISSUE**:

1. ⚠️ **Most Likely:** No questions exist in the database for DSA + INTERMEDIATE
2. ⚠️ **Possible:** The user 'testuser' doesn't exist
3. ⚠️ **Possible:** Slot ID 1 doesn't exist or is already booked
4. ⚠️ **Less Likely:** Database connection issue

---

## ✅ **QUICK DATABASE CHECK**

Run these SQL queries to verify:

```sql
-- 1. Check if questions exist
SELECT COUNT(*) as question_count, category, difficulty_level 
FROM questions 
GROUP BY category, difficulty_level
ORDER BY category, difficulty_level;

-- 2. Check if slots exist
SELECT id, title, topic, difficulty_level, booked, status 
FROM interview_slots 
ORDER BY id;

-- 3. Check if user exists
SELECT id, username, email 
FROM users 
WHERE username = 'testuser' OR username = 'admin';

-- 4. Check recent interviews
SELECT id, user_id, slot_id, topic, difficulty_level, status, start_time 
FROM interviews 
ORDER BY id DESC 
LIMIT 5;
```

---

## 🔧 **QUICK FIX: Add Sample Data**

If you have NO DATA in the database, run this:

```sql
-- Add sample questions
INSERT INTO questions (title, description, question_type, category, difficulty_level, time_limit_seconds, points, solution, created_at)
VALUES 
-- DSA Questions
('Two Sum', 'Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target. You may assume that each input would have exactly one solution, and you may not use the same element twice.', 'CODING', 'DSA', 'BEGINNER', 900, 10, 'Use HashMap to store value-index pairs. For each number, check if target - current number exists in the map.', CURRENT_TIMESTAMP),
('Reverse Linked List', 'Given the head of a singly linked list, reverse the list, and return the reversed list.', 'CODING', 'DSA', 'INTERMEDIATE', 900, 15, 'Use three pointers: previous, current, next. Iterate through the list reversing the next pointer direction.', CURRENT_TIMESTAMP),
('Binary Search Tree Validation', 'Given the root of a binary tree, determine if it is a valid binary search tree (BST).', 'CODING', 'DSA', 'INTERMEDIATE', 900, 15, 'Use recursive DFS with min/max bounds. Left subtree must be < root, right subtree must be > root.', CURRENT_TIMESTAMP),
('Merge K Sorted Lists', 'Merge k sorted linked lists and return it as one sorted list.', 'CODING', 'DSA', 'ADVANCED', 1200, 20, 'Use a min heap (priority queue) to track the smallest elements from each list.', CURRENT_TIMESTAMP),

-- DBMS Questions
('What is Normalization?', 'Explain database normalization and its normal forms (1NF, 2NF, 3NF, BCNF).', 'THEORETICAL', 'DBMS', 'BEGINNER', 600, 10, 'Normalization is the process of organizing data to reduce redundancy. 1NF: Atomic values, 2NF: No partial dependencies, 3NF: No transitive dependencies, BCNF: Every determinant is a candidate key.', CURRENT_TIMESTAMP),
('ACID Properties', 'Explain the ACID properties of database transactions with examples.', 'THEORETICAL', 'DBMS', 'INTERMEDIATE', 600, 15, 'Atomicity: All or nothing. Consistency: Valid state. Isolation: Concurrent transactions dont interfere. Durability: Changes persist after commit.', CURRENT_TIMESTAMP),
('Database Indexing', 'What is database indexing? Explain different types of indexes and when to use them.', 'THEORETICAL', 'DBMS', 'INTERMEDIATE', 600, 15, 'Indexes speed up data retrieval. B-tree index (default), Hash index (equality searches), Bitmap index (low cardinality), Full-text index (text searches).', CURRENT_TIMESTAMP),

-- OOP Questions
('Four Pillars of OOP', 'Explain the four pillars of Object-Oriented Programming with examples.', 'THEORETICAL', 'OOP', 'BEGINNER', 600, 10, 'Encapsulation: Bundling data and methods. Inheritance: Reusing code through parent-child relationships. Polymorphism: Same interface, different implementations. Abstraction: Hiding complex details.', CURRENT_TIMESTAMP),
('Interface vs Abstract Class', 'What is the difference between an interface and an abstract class? When would you use each?', 'THEORETICAL', 'OOP', 'INTERMEDIATE', 600, 15, 'Interface: Contract (what to do), multiple inheritance, all methods abstract (Java 8+ can have default). Abstract class: Partial implementation (how to do), single inheritance, can have concrete methods and state.', CURRENT_TIMESTAMP),
('SOLID Principles', 'Explain the SOLID principles of object-oriented design with examples.', 'THEORETICAL', 'OOP', 'ADVANCED', 900, 20, 'S: Single Responsibility, O: Open/Closed, L: Liskov Substitution, I: Interface Segregation, D: Dependency Inversion. Each promotes maintainable, flexible code.', CURRENT_TIMESTAMP);

-- Add sample slots
INSERT INTO interview_slots (title, description, topic, difficulty_level, duration_minutes, scheduled_date_time, max_participants, status, booked, created_at, updated_at)
VALUES 
('Mock Interview - DSA', 'Practice interview for Data Structures and Algorithms', 'DSA', 'INTERMEDIATE', 60, '2025-11-10 10:00:00', 1, 'AVAILABLE', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Mock Interview - DBMS', 'Practice interview for Database Management Systems', 'DBMS', 'BEGINNER', 45, '2025-11-11 14:00:00', 1, 'AVAILABLE', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Mock Interview - OOP', 'Practice interview for Object-Oriented Programming', 'OOP', 'INTERMEDIATE', 60, '2025-11-12 16:00:00', 1, 'AVAILABLE', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
```

---

## 📞 **CONTACT POINT**

**Backend is now running with detailed logging.**

**Try to start an interview again, and then check the backend console output. Copy the error and we'll fix it immediately!**

---

**Backend:** http://localhost:8080 ✅  
**Frontend:** http://localhost:3000/interviews ✅  
**PostgreSQL:** localhost:5432/interview_coaching

**The detailed error will tell us EXACTLY what's wrong.** 🔍

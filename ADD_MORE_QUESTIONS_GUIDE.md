# 📚 ADD MORE QUESTIONS TO DATABASE - Complete Guide

## 🎯 **WHY YOU NEED MORE QUESTIONS**

**Current Problem:**
- All difficulty levels show same questions
- Not enough variety per category
- Need concept-wise + difficulty-wise combinations

**Current Question Count:**
| Category | Current Count | Need Per Difficulty |
|----------|---------------|---------------------|
| DSA | 4 questions | 3-5 per difficulty (9-15 total) |
| DBMS | 3 questions | 3-5 per difficulty (9-15 total) |
| OS | 2 questions | 3-5 per difficulty (9-15 total) |
| OOP | 2 questions | 3-5 per difficulty (9-15 total) |
| WEB_DEV | 2 questions | 3-5 per difficulty (9-15 total) |
| SYSTEM_DESIGN | 3 questions | 3-5 per difficulty (9-15 total) |

---

## 🗄️ **HOW TO ADD QUESTIONS TO DATABASE**

### **Method 1: Direct SQL Insert (Recommended)**

**Step 1: Create SQL File**

Create: `backend/src/main/resources/add_questions.sql`

```sql
-- ============================================
-- DSA QUESTIONS - BEGINNER
-- ============================================
INSERT INTO questions (title, description, question_type, category, sub_category, difficulty_level, time_limit_seconds, points, hints, solution, code_template, constraints_info, created_at) 
VALUES 
('Find Maximum Element', 'Write a function to find the maximum element in an array of integers.', 'CODING', 'DSA', 'Arrays', 'BEGINNER', 600, 8, 'Iterate through the array and keep track of the maximum value.', 'Iterate through array, compare each element with current max.', 'public int findMax(int[] arr) {\n    // Write your code here\n}', 'Array length: 1 to 1000', NOW()),

('Reverse a String', 'Write a function to reverse a given string.', 'CODING', 'DSA', 'Strings', 'BEGINNER', 600, 8, 'Use two pointers or StringBuilder.', 'Use two pointers from start and end.', 'public String reverseString(String str) {\n    // Write your code here\n}', 'String length: 1 to 1000', NOW()),

('Check Palindrome', 'Write a function to check if a string is a palindrome.', 'CODING', 'DSA', 'Strings', 'BEGINNER', 600, 8, 'Compare characters from both ends.', 'Use two pointers approach.', 'public boolean isPalindrome(String str) {\n    // Write your code here\n}', 'String length: 1 to 1000', NOW());

-- ============================================
-- DSA QUESTIONS - INTERMEDIATE
-- ============================================
INSERT INTO questions (title, description, question_type, category, sub_category, difficulty_level, time_limit_seconds, points, hints, solution, code_template, constraints_info, created_at) 
VALUES 
('Longest Substring Without Repeating', 'Find the length of the longest substring without repeating characters.', 'CODING', 'DSA', 'Strings', 'INTERMEDIATE', 1200, 15, 'Use sliding window technique with a HashMap.', 'Use sliding window with HashSet to track characters.', 'public int lengthOfLongestSubstring(String s) {\n    // Write your code here\n}', 'String length: 0 to 50000', NOW()),

('Valid Parentheses', 'Given a string containing just the characters ''('', '')'', ''{'', ''}'', ''['' and '']'', determine if the input string is valid.', 'CODING', 'DSA', 'Stack', 'INTERMEDIATE', 1200, 15, 'Use a stack to match opening and closing brackets.', 'Push opening brackets to stack, pop for closing brackets.', 'public boolean isValid(String s) {\n    // Write your code here\n}', 'String length: 1 to 10000', NOW()),

('Rotate Array', 'Rotate an array to the right by k steps.', 'CODING', 'DSA', 'Arrays', 'INTERMEDIATE', 1200, 15, 'Reverse the array in parts.', 'Reverse entire array, then reverse first k and remaining.', 'public void rotate(int[] nums, int k) {\n    // Write your code here\n}', 'Array length: 1 to 100000', NOW());

-- ============================================
-- DSA QUESTIONS - ADVANCED
-- ============================================
INSERT INTO questions (title, description, question_type, category, sub_category, difficulty_level, time_limit_seconds, points, hints, solution, code_template, constraints_info, created_at) 
VALUES 
('LRU Cache', 'Design and implement a Least Recently Used (LRU) cache.', 'CODING', 'DSA', 'Design', 'ADVANCED', 1800, 25, 'Use HashMap + Doubly Linked List.', 'Combine HashMap for O(1) access and Doubly LL for LRU ordering.', 'class LRUCache {\n    // Implement LRU Cache\n}', 'Cache capacity: 1 to 3000', NOW()),

('Median of Two Sorted Arrays', 'Find the median of two sorted arrays.', 'CODING', 'DSA', 'Binary Search', 'ADVANCED', 1800, 25, 'Use binary search on the smaller array.', 'Binary search to partition arrays into two halves.', 'public double findMedianSortedArrays(int[] nums1, int[] nums2) {\n    // Write your code here\n}', 'Array lengths: 0 to 1000', NOW()),

('Regular Expression Matching', 'Implement regular expression matching with ''.'' and ''*''.', 'CODING', 'DSA', 'Dynamic Programming', 'ADVANCED', 1800, 25, 'Use Dynamic Programming with 2D array.', 'Build DP table for pattern matching.', 'public boolean isMatch(String s, String p) {\n    // Write your code here\n}', 'String length: 0 to 30', NOW());

-- ============================================
-- DBMS QUESTIONS - BEGINNER
-- ============================================
INSERT INTO questions (title, description, question_type, category, sub_category, difficulty_level, time_limit_seconds, points, hints, solution, code_template, constraints_info, created_at) 
VALUES 
('Select All Records', 'Write a SQL query to select all columns from a table named ''employees''.', 'CODING', 'DBMS', 'SQL', 'BEGINNER', 600, 8, 'Use SELECT * FROM syntax.', 'SELECT * FROM employees;', 'SELECT \n-- Write your SQL query here\n', NULL, NOW()),

('Filter with WHERE', 'Write a SQL query to find all employees with salary greater than 50000.', 'CODING', 'DBMS', 'SQL', 'BEGINNER', 600, 8, 'Use WHERE clause with comparison operator.', 'SELECT * FROM employees WHERE salary > 50000;', 'SELECT \n-- Write your SQL query here\n', NULL, NOW()),

('What is Normalization?', 'Explain database normalization and its benefits.', 'THEORETICAL', 'DBMS', 'Concepts', 'BEGINNER', 600, 8, NULL, 'Normalization is the process of organizing data to reduce redundancy.', NULL, NULL, NOW());

-- ============================================
-- DBMS QUESTIONS - INTERMEDIATE
-- ============================================
INSERT INTO questions (title, description, question_type, category, sub_category, difficulty_level, time_limit_seconds, points, hints, solution, code_template, constraints_info, created_at) 
VALUES 
('JOIN Tables', 'Write a SQL query to join employees and departments tables.', 'CODING', 'DBMS', 'SQL', 'INTERMEDIATE', 1200, 15, 'Use INNER JOIN with ON clause.', 'SELECT * FROM employees e INNER JOIN departments d ON e.dept_id = d.id;', 'SELECT \n-- Write your SQL query here\n', NULL, NOW()),

('GROUP BY and HAVING', 'Find departments with average salary greater than 60000.', 'CODING', 'DBMS', 'SQL', 'INTERMEDIATE', 1200, 15, 'Use GROUP BY with HAVING clause.', 'SELECT dept_id, AVG(salary) FROM employees GROUP BY dept_id HAVING AVG(salary) > 60000;', 'SELECT \n-- Write your SQL query here\n', NULL, NOW()),

('ACID Properties', 'Explain ACID properties in database transactions.', 'THEORETICAL', 'DBMS', 'Concepts', 'INTERMEDIATE', 900, 12, NULL, 'Atomicity Consistency Isolation Durability - ensures reliable transactions.', NULL, NULL, NOW());

-- ============================================
-- DBMS QUESTIONS - ADVANCED
-- ============================================
INSERT INTO questions (title, description, question_type, category, sub_category, difficulty_level, time_limit_seconds, points, hints, solution, code_template, constraints_info, created_at) 
VALUES 
('Complex Subquery', 'Write a query to find employees earning more than their department average.', 'CODING', 'DBMS', 'SQL', 'ADVANCED', 1800, 25, 'Use correlated subquery.', 'SELECT * FROM employees e WHERE salary > (SELECT AVG(salary) FROM employees WHERE dept_id = e.dept_id);', 'SELECT \n-- Write your SQL query here\n', NULL, NOW()),

('Database Indexing', 'Explain different types of database indexes and when to use them.', 'THEORETICAL', 'DBMS', 'Performance', 'ADVANCED', 1200, 20, NULL, 'B-Tree, Hash, Bitmap indexes - used for query optimization.', NULL, NULL, NOW()),

('Window Functions', 'Use window functions to rank employees by salary within each department.', 'CODING', 'DBMS', 'SQL', 'ADVANCED', 1800, 25, 'Use ROW_NUMBER() OVER (PARTITION BY ORDER BY).', 'SELECT employee_name, salary, ROW_NUMBER() OVER (PARTITION BY dept_id ORDER BY salary DESC) FROM employees;', 'SELECT \n-- Write your SQL query here\n', NULL, NOW());

-- ============================================
-- OOP QUESTIONS - BEGINNER
-- ============================================
INSERT INTO questions (title, description, question_type, category, sub_category, difficulty_level, time_limit_seconds, points, hints, solution, code_template, constraints_info, created_at) 
VALUES 
('Create a Class', 'Create a simple Car class with properties: brand, model, year.', 'CODING', 'OOP', 'Classes', 'BEGINNER', 600, 8, 'Define class with private fields and public getters/setters.', 'Create class with fields, constructor, and methods.', 'public class Car {\n    // Define class members\n}', NULL, NOW()),

('What is Encapsulation?', 'Explain encapsulation in Object-Oriented Programming.', 'THEORETICAL', 'OOP', 'Concepts', 'BEGINNER', 600, 8, NULL, 'Encapsulation is bundling data and methods that operate on that data within a single unit.', NULL, NULL, NOW()),

('Constructor Overloading', 'Demonstrate constructor overloading in a Rectangle class.', 'CODING', 'OOP', 'Constructors', 'BEGINNER', 600, 8, 'Create multiple constructors with different parameters.', 'Define multiple constructors with different parameter lists.', 'public class Rectangle {\n    // Define overloaded constructors\n}', NULL, NOW());

-- ============================================
-- Continue for INTERMEDIATE and ADVANCED OOP, OS, WEB_DEV, SYSTEM_DESIGN...
-- ============================================
```

**Step 2: Run SQL File**

**Option A: Using pgAdmin**
```
1. Open pgAdmin
2. Connect to interview_coaching database
3. Click Tools → Query Tool
4. Open add_questions.sql
5. Execute (F5)
```

**Option B: Using Command Line**
```powershell
psql -U postgres -d interview_coaching -f backend/src/main/resources/add_questions.sql
```

---

### **Method 2: Using Spring Boot DataLoader (Auto-load on startup)**

**Step 1: Create DataLoader Class**

Create: `backend/src/main/java/com/interviewcoaching/config/QuestionDataLoader.java`

```java
package com.interviewcoaching.config;

import com.interviewcoaching.models.interview.Question;
import com.interviewcoaching.repositories.interview.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class QuestionDataLoader implements CommandLineRunner {

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only load if database is empty
        if (questionRepository.count() < 20) {
            loadDSAQuestions();
            loadDBMSQuestions();
            loadOOPQuestions();
            loadOSQuestions();
            loadWebDevQuestions();
            loadSystemDesignQuestions();
            
            System.out.println("✅ Questions loaded successfully!");
        }
    }

    private void loadDSAQuestions() {
        // BEGINNER
        createQuestion("Find Maximum", "Find max element in array", 
            "CODING", "DSA", "Arrays", "BEGINNER", 600, 8);
        createQuestion("Reverse String", "Reverse a given string", 
            "CODING", "DSA", "Strings", "BEGINNER", 600, 8);
        createQuestion("Check Palindrome", "Check if string is palindrome", 
            "CODING", "DSA", "Strings", "BEGINNER", 600, 8);
        
        // INTERMEDIATE  
        createQuestion("Longest Substring", "Find longest substring without repeating", 
            "CODING", "DSA", "Strings", "INTERMEDIATE", 1200, 15);
        createQuestion("Valid Parentheses", "Check if parentheses are valid", 
            "CODING", "DSA", "Stack", "INTERMEDIATE", 1200, 15);
        createQuestion("Rotate Array", "Rotate array by k steps", 
            "CODING", "DSA", "Arrays", "INTERMEDIATE", 1200, 15);
        
        // ADVANCED
        createQuestion("LRU Cache", "Implement LRU cache", 
            "CODING", "DSA", "Design", "ADVANCED", 1800, 25);
        createQuestion("Median of Arrays", "Find median of two sorted arrays", 
            "CODING", "DSA", "Binary Search", "ADVANCED", 1800, 25);
        createQuestion("Regex Matching", "Implement regex matching", 
            "CODING", "DSA", "Dynamic Programming", "ADVANCED", 1800, 25);
    }

    private void createQuestion(String title, String description, 
        String type, String category, String subCategory, 
        String difficulty, int timeLimit, int points) {
        
        Question q = new Question();
        q.setTitle(title);
        q.setDescription(description);
        q.setQuestionType(Question.QuestionType.valueOf(type));
        q.setCategory(category);
        q.setSubCategory(subCategory);
        q.setDifficultyLevel(Question.DifficultyLevel.valueOf(difficulty));
        q.setTimeLimitSeconds(timeLimit);
        q.setPoints(points);
        q.setCodeTemplate("// Write your code here");
        
        questionRepository.save(q);
    }

    // Similar methods for other categories...
}
```

**Restart Backend** - Questions auto-load on startup!

---

## 📝 **QUICK QUESTION TEMPLATE**

Use this template for adding questions:

```sql
INSERT INTO questions (
    title, 
    description, 
    question_type,  -- 'CODING', 'THEORETICAL', 'SYSTEM_DESIGN'
    category,       -- 'DSA', 'DBMS', 'OS', 'OOP', 'WEB_DEV', 'SYSTEM_DESIGN'
    sub_category, 
    difficulty_level, -- 'BEGINNER', 'INTERMEDIATE', 'ADVANCED'
    time_limit_seconds, 
    points, 
    hints, 
    solution, 
    code_template, 
    constraints_info, 
    created_at
) VALUES (
    'Question Title',
    'Question Description',
    'CODING',
    'DSA',
    'Arrays',
    'BEGINNER',
    600,
    10,
    'Optional hint',
    'Optional solution',
    'public void solve() {\n    // Code here\n}',
    'Constraints info',
    NOW()
);
```

---

## 🎯 **RECOMMENDED QUESTION DISTRIBUTION**

For each category, aim for:

| Difficulty | Coding Questions | Theory Questions | Total |
|------------|------------------|------------------|-------|
| BEGINNER | 3 | 2 | 5 |
| INTERMEDIATE | 3 | 2 | 5 |
| ADVANCED | 3 | 2 | 5 |
| **Total per category** | **9** | **6** | **15** |

**Total across all 6 categories:** ~90 questions

---

## ✅ **VERIFY QUESTIONS ADDED**

### **Check via SQL:**
```sql
-- Count questions by category and difficulty
SELECT 
    category, 
    difficulty_level, 
    COUNT(*) as count 
FROM questions 
GROUP BY category, difficulty_level 
ORDER BY category, difficulty_level;
```

### **Expected Output:**
```
 category      | difficulty_level | count
---------------|------------------|-------
 DSA           | BEGINNER         | 5
 DSA           | INTERMEDIATE     | 5
 DSA           | ADVANCED         | 5
 DBMS          | BEGINNER         | 5
 DBMS          | INTERMEDIATE     | 5
 ...
```

### **Check via Backend:**
Start an interview and verify different questions appear for each difficulty!

---

## 🚀 **COMPLETE SQL FILE (READY TO USE)**

I'll create a complete SQL file with all questions...

---

## 📌 **AFTER ADDING QUESTIONS**

1. **Restart Backend** (if using DataLoader)
2. **Test Each Category + Difficulty Combination:**
   - DSA + Beginner
   - DSA + Intermediate
   - DSA + Advanced
   - DBMS + Beginner
   - etc.

3. **Verify Different Questions Appear**

---

## 🎉 **BENEFITS AFTER ADDING MORE QUESTIONS**

✅ **Different questions** for each difficulty level  
✅ **Better interview variety**  
✅ **More realistic practice**  
✅ **Topic-specific learning**  
✅ **Progressive difficulty**

---

**Your interview module will be MUCH better with more questions!** 🚀

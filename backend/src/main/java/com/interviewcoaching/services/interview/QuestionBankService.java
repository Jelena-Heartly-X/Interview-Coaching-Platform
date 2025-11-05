package com.interviewcoaching.services.interview;

import com.interviewcoaching.models.interview.Question;
import com.interviewcoaching.models.interview.TestCase;
import com.interviewcoaching.repositories.interview.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionBankService {
    
    @Autowired
    private QuestionRepository questionRepository;
    
    /**
     * Seed the database with sample questions for all categories
     */
    @Transactional
    public void seedQuestions() {
        long existingCount = questionRepository.count();
        System.out.println("Existing questions count: " + existingCount);
        
        if (existingCount > 0) {
            System.out.println("Questions already exist. Skipping seed.");
            return; // Already seeded
        }
        
        System.out.println("Starting to seed questions...");
        List<Question> questions = new ArrayList<>();
        
        // DSA Questions
        questions.addAll(createDSAQuestions());
        System.out.println("Created DSA questions: " + questions.size());
        
        // DBMS Questions
        questions.addAll(createDBMSQuestions());
        System.out.println("Created DBMS questions: " + questions.size());
        
        // OS Questions
        questions.addAll(createOSQuestions());
        System.out.println("Created OS questions: " + questions.size());
        
        // OOP Questions
        questions.addAll(createOOPQuestions());
        System.out.println("Created OOP questions: " + questions.size());
        
        // Web Development Questions
        questions.addAll(createWebDevQuestions());
        System.out.println("Created Web Dev questions: " + questions.size());
        
        // System Design Questions
        questions.addAll(createSystemDesignQuestions());
        System.out.println("Total questions to save: " + questions.size());
        
        List<Question> saved = questionRepository.saveAll(questions);
        System.out.println("Successfully saved " + saved.size() + " questions!");
    }
    
    /**
     * Force reseed - deletes all questions and creates new ones
     */
    @Transactional
    public void forceReseedQuestions() {
        System.out.println("Force reseeding - Deleting all existing questions...");
        questionRepository.deleteAll();
        System.out.println("All questions deleted. Starting fresh seed...");
        
        List<Question> questions = new ArrayList<>();
        
        // DSA Questions
        questions.addAll(createDSAQuestions());
        
        // DBMS Questions
        questions.addAll(createDBMSQuestions());
        
        // OS Questions
        questions.addAll(createOSQuestions());
        
        // OOP Questions
        questions.addAll(createOOPQuestions());
        
        // Web Development Questions
        questions.addAll(createWebDevQuestions());
        
        // System Design Questions
        questions.addAll(createSystemDesignQuestions());
        
        System.out.println("Total questions to save: " + questions.size());
        List<Question> saved = questionRepository.saveAll(questions);
        System.out.println("Successfully saved " + saved.size() + " questions!");
    }
    
    private List<Question> createDSAQuestions() {
        List<Question> questions = new ArrayList<>();
        
        // Arrays - Easy
        Question q1 = new Question();
        q1.setTitle("Two Sum");
        q1.setDescription("Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.\n\nYou may assume that each input would have exactly one solution, and you may not use the same element twice.");
        q1.setQuestionType(Question.QuestionType.CODING);
        q1.setCategory("DSA");
        q1.setSubCategory("Arrays");
        q1.setDifficultyLevel(Question.DifficultyLevel.BEGINNER);
        q1.setTimeLimitSeconds(900);
        q1.setPoints(10);
        q1.setHints("Consider using a HashMap to store the complement of each number.");
        q1.setSolution("Use a HashMap to store visited numbers and their indices. For each number, check if its complement exists in the map.");
        q1.setCodeTemplate("public int[] twoSum(int[] nums, int target) {\n    // Write your code here\n    \n}");
        q1.setConstraintsInfo("2 <= nums.length <= 10^4\n-10^9 <= nums[i] <= 10^9\n-10^9 <= target <= 10^9");
        
        // Add test cases
        TestCase tc1 = new TestCase();
        tc1.setQuestion(q1);
        tc1.setInputData("nums = [2,7,11,15], target = 9");
        tc1.setExpectedOutput("[0,1]");
        tc1.setIsSample(true);
        tc1.setIsHidden(false);
        tc1.setPoints(5);
        tc1.setExplanation("nums[0] + nums[1] = 2 + 7 = 9");
        q1.getTestCases().add(tc1);
        
        TestCase tc2 = new TestCase();
        tc2.setQuestion(q1);
        tc2.setInputData("nums = [3,2,4], target = 6");
        tc2.setExpectedOutput("[1,2]");
        tc2.setIsSample(false);
        tc2.setIsHidden(true);
        tc2.setPoints(5);
        q1.getTestCases().add(tc2);
        
        questions.add(q1);
        
        // Arrays - Medium
        Question q2 = new Question();
        q2.setTitle("Container With Most Water");
        q2.setDescription("Given n non-negative integers a1, a2, ..., an, where each represents a point at coordinate (i, ai). Find two lines, which together with x-axis forms a container, such that the container contains the most water.");
        q2.setQuestionType(Question.QuestionType.CODING);
        q2.setCategory("DSA");
        q2.setSubCategory("Arrays");
        q2.setDifficultyLevel(Question.DifficultyLevel.INTERMEDIATE);
        q2.setTimeLimitSeconds(1200);
        q2.setPoints(15);
        q2.setHints("Use two pointers approach starting from both ends.");
        q2.setSolution("Use two pointers, one at the start and one at the end. Calculate area and move the pointer with smaller height inward.");
        q2.setCodeTemplate("public int maxArea(int[] height) {\n    // Write your code here\n    \n}");
        q2.setConstraintsInfo("2 <= height.length <= 10^5\n0 <= height[i] <= 10^4");
        questions.add(q2);
        
        // Strings - Easy
        Question q3 = new Question();
        q3.setTitle("Valid Palindrome");
        q3.setDescription("Given a string s, determine if it is a palindrome, considering only alphanumeric characters and ignoring cases.");
        q3.setQuestionType(Question.QuestionType.CODING);
        q3.setCategory("DSA");
        q3.setSubCategory("Strings");
        q3.setDifficultyLevel(Question.DifficultyLevel.BEGINNER);
        q3.setTimeLimitSeconds(600);
        q3.setPoints(8);
        q3.setHints("Use two pointers from start and end, skip non-alphanumeric characters.");
        q3.setCodeTemplate("public boolean isPalindrome(String s) {\n    // Write your code here\n    \n}");
        questions.add(q3);
        
        // Trees - Medium
        Question q4 = new Question();
        q4.setTitle("Binary Tree Level Order Traversal");
        q4.setDescription("Given the root of a binary tree, return the level order traversal of its nodes' values. (i.e., from left to right, level by level).");
        q4.setQuestionType(Question.QuestionType.CODING);
        q4.setCategory("DSA");
        q4.setSubCategory("Trees");
        q4.setDifficultyLevel(Question.DifficultyLevel.INTERMEDIATE);
        q4.setTimeLimitSeconds(1200);
        q4.setPoints(15);
        q4.setHints("Use BFS (Breadth-First Search) with a queue.");
        q4.setCodeTemplate("public List<List<Integer>> levelOrder(TreeNode root) {\n    // Write your code here\n    \n}");
        questions.add(q4);
        
        // Graphs - Advanced
        Question q5 = new Question();
        q5.setTitle("Course Schedule");
        q5.setDescription("There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1. You are given an array prerequisites where prerequisites[i] = [ai, bi] indicates that you must take course bi first if you want to take course ai. Return true if you can finish all courses. Otherwise, return false.");
        q5.setQuestionType(Question.QuestionType.CODING);
        q5.setCategory("DSA");
        q5.setSubCategory("Graphs");
        q5.setDifficultyLevel(Question.DifficultyLevel.ADVANCED);
        q5.setTimeLimitSeconds(1800);
        q5.setPoints(20);
        q5.setHints("This is a cycle detection problem in a directed graph. Use topological sort or DFS.");
        q5.setSolution("Use Kahn's algorithm for topological sorting or DFS with cycle detection.");
        q5.setCodeTemplate("public boolean canFinish(int numCourses, int[][] prerequisites) {\n    // Write your code here\n    \n}");
        questions.add(q5);
        
        return questions;
    }
    
    private List<Question> createDBMSQuestions() {
        List<Question> questions = new ArrayList<>();
        
        // DBMS - Beginner
        Question q1 = new Question();
        q1.setTitle("What is ACID in DBMS?");
        q1.setDescription("Explain the ACID properties in database management systems with examples.");
        q1.setQuestionType(Question.QuestionType.THEORETICAL);
        q1.setCategory("DBMS");
        q1.setSubCategory("Fundamentals");
        q1.setDifficultyLevel(Question.DifficultyLevel.BEGINNER);
        q1.setTimeLimitSeconds(600);
        q1.setPoints(10);
        q1.setHints("ACID stands for Atomicity, Consistency, Isolation, Durability.");
        q1.setSolution("ACID properties ensure reliable database transactions:\n- Atomicity: All or nothing\n- Consistency: Database remains valid\n- Isolation: Concurrent transactions don't interfere\n- Durability: Committed transactions persist");
        questions.add(q1);
        
        // DBMS - Intermediate
        Question q2 = new Question();
        q2.setTitle("SQL Query Optimization");
        q2.setDescription("Write an optimized SQL query to find the second highest salary from an Employee table.");
        q2.setQuestionType(Question.QuestionType.CODING);
        q2.setCategory("DBMS");
        q2.setSubCategory("SQL");
        q2.setDifficultyLevel(Question.DifficultyLevel.INTERMEDIATE);
        q2.setTimeLimitSeconds(900);
        q2.setPoints(12);
        q2.setHints("Use DISTINCT, ORDER BY, and LIMIT/OFFSET.");
        q2.setSolution("SELECT DISTINCT salary FROM Employee ORDER BY salary DESC LIMIT 1 OFFSET 1");
        q2.setCodeTemplate("SELECT \n-- Write your SQL query here\n");
        questions.add(q2);
        
        // DBMS - Advanced
        Question q3 = new Question();
        q3.setTitle("Database Normalization");
        q3.setDescription("Explain 1NF, 2NF, 3NF, and BCNF with examples. Normalize the given denormalized table.");
        q3.setQuestionType(Question.QuestionType.THEORETICAL);
        q3.setCategory("DBMS");
        q3.setSubCategory("Normalization");
        q3.setDifficultyLevel(Question.DifficultyLevel.ADVANCED);
        q3.setTimeLimitSeconds(1200);
        q3.setPoints(15);
        questions.add(q3);
        
        return questions;
    }
    
    private List<Question> createOSQuestions() {
        List<Question> questions = new ArrayList<>();
        
        Question q1 = new Question();
        q1.setTitle("Process vs Thread");
        q1.setDescription("Explain the difference between a process and a thread. Discuss memory allocation, context switching, and communication.");
        q1.setQuestionType(Question.QuestionType.THEORETICAL);
        q1.setCategory("OS");
        q1.setSubCategory("Process Management");
        q1.setDifficultyLevel(Question.DifficultyLevel.BEGINNER);
        q1.setTimeLimitSeconds(600);
        q1.setPoints(10);
        questions.add(q1);
        
        Question q2 = new Question();
        q2.setTitle("Deadlock Detection");
        q2.setDescription("Explain the four necessary conditions for deadlock and methods to prevent, avoid, and detect deadlocks.");
        q2.setQuestionType(Question.QuestionType.THEORETICAL);
        q2.setCategory("OS");
        q2.setSubCategory("Synchronization");
        q2.setDifficultyLevel(Question.DifficultyLevel.INTERMEDIATE);
        q2.setTimeLimitSeconds(900);
        q2.setPoints(12);
        q2.setHints("Four conditions: Mutual Exclusion, Hold and Wait, No Preemption, Circular Wait.");
        questions.add(q2);
        
        return questions;
    }
    
    private List<Question> createOOPQuestions() {
        List<Question> questions = new ArrayList<>();
        
        Question q1 = new Question();
        q1.setTitle("OOP Principles");
        q1.setDescription("Explain the four pillars of Object-Oriented Programming: Encapsulation, Abstraction, Inheritance, and Polymorphism with Java examples.");
        q1.setQuestionType(Question.QuestionType.THEORETICAL);
        q1.setCategory("OOP");
        q1.setSubCategory("Fundamentals");
        q1.setDifficultyLevel(Question.DifficultyLevel.BEGINNER);
        q1.setTimeLimitSeconds(900);
        q1.setPoints(10);
        questions.add(q1);
        
        Question q2 = new Question();
        q2.setTitle("Design Pattern Implementation");
        q2.setDescription("Implement the Singleton design pattern in Java. Ensure thread-safety and explain different implementation approaches.");
        q2.setQuestionType(Question.QuestionType.CODING);
        q2.setCategory("OOP");
        q2.setSubCategory("Design Patterns");
        q2.setDifficultyLevel(Question.DifficultyLevel.INTERMEDIATE);
        q2.setTimeLimitSeconds(1200);
        q2.setPoints(15);
        q2.setCodeTemplate("public class Singleton {\n    // Implement thread-safe Singleton pattern\n    \n}");
        questions.add(q2);
        
        return questions;
    }
    
    private List<Question> createWebDevQuestions() {
        List<Question> questions = new ArrayList<>();
        
        Question q1 = new Question();
        q1.setTitle("RESTful API Design");
        q1.setDescription("Design a RESTful API for a blogging platform. Include endpoints for CRUD operations on posts and comments.");
        q1.setQuestionType(Question.QuestionType.THEORETICAL);
        q1.setCategory("WEB_DEV");
        q1.setSubCategory("Backend");
        q1.setDifficultyLevel(Question.DifficultyLevel.INTERMEDIATE);
        q1.setTimeLimitSeconds(1200);
        q1.setPoints(15);
        questions.add(q1);
        
        Question q2 = new Question();
        q2.setTitle("JWT Authentication");
        q2.setDescription("Explain how JWT (JSON Web Token) authentication works. Implement a simple JWT token generation and verification in Java.");
        q2.setQuestionType(Question.QuestionType.CODING);
        q2.setCategory("WEB_DEV");
        q2.setSubCategory("Security");
        q2.setDifficultyLevel(Question.DifficultyLevel.ADVANCED);
        q2.setTimeLimitSeconds(1500);
        q2.setPoints(18);
        questions.add(q2);
        
        return questions;
    }
    
    private List<Question> createSystemDesignQuestions() {
        List<Question> questions = new ArrayList<>();
        
        Question q1 = new Question();
        q1.setTitle("Design URL Shortener");
        q1.setDescription("Design a URL shortening service like bit.ly. Discuss the architecture, database design, API endpoints, and scaling strategies.");
        q1.setQuestionType(Question.QuestionType.SYSTEM_DESIGN);
        q1.setCategory("SYSTEM_DESIGN");
        q1.setSubCategory("Web Services");
        q1.setDifficultyLevel(Question.DifficultyLevel.INTERMEDIATE);
        q1.setTimeLimitSeconds(1800);
        q1.setPoints(20);
        q1.setHints("Consider: Hash function, collision handling, database choice, caching, rate limiting.");
        questions.add(q1);
        
        Question q2 = new Question();
        q2.setTitle("Design Twitter");
        q2.setDescription("Design a simplified version of Twitter. Include feed generation, user following, tweet posting, and timeline features.");
        q2.setQuestionType(Question.QuestionType.SYSTEM_DESIGN);
        q2.setCategory("SYSTEM_DESIGN");
        q2.setSubCategory("Social Media");
        q2.setDifficultyLevel(Question.DifficultyLevel.ADVANCED);
        q2.setTimeLimitSeconds(2400);
        q2.setPoints(25);
        q2.setHints("Fan-out on write vs read, database sharding, caching strategies, notification system.");
        questions.add(q2);
        
        return questions;
    }
}

-- ============================================================
-- FULL QUESTION INSERT SCRIPT (PostgreSQL)
-- Categories: DSA, DBMS, OOP, OS, WEB_DEV, SYSTEM_DESIGN
-- Levels: BEGINNER, INTERMEDIATE, ADVANCED
-- 15 questions per category (5 beginner + 5 intermediate + 5 advanced)
-- Each category approx. 9 THEORETICAL + 6 CODING
-- ============================================================

-- ===========================
-- DSA (15)
-- ===========================
INSERT INTO questions (title, description, question_type, category, sub_category, difficulty_level, time_limit_seconds, points, hints, solution, code_template, constraints_info, created_at)
VALUES
-- DSA - BEGINNER (5)
('Find Maximum Element', 'Write a function to find the maximum element in an array of integers.', 'CODING', 'DSA', 'Arrays', 'BEGINNER', 600, 8, 'Iterate through the array and keep track of the maximum value.', 'Iterate through array comparing each element to current max.', 'public int findMax(int[] arr) {\n    // Write your code here\n}', 'Array length: 1 to 1000', NOW()),
('Reverse a String', 'Write a function to reverse a given string.', 'CODING', 'DSA', 'Strings', 'BEGINNER', 600, 8, 'Use two pointers or StringBuilder reverse.', 'Swap characters from ends or use language built-ins.', 'public String reverseString(String s) {\n    // Write your code here\n}', 'String length: 1 to 1000', NOW()),
('Array vs LinkedList', 'Explain the main differences between arrays and linked lists and when to use each.', 'THEORETICAL', 'DSA', 'Arrays/Lists', 'BEGINNER', 420, 6, NULL, 'Arrays: contiguous memory, O(1) random access. LinkedList: dynamic, cheap insert/delete.', NULL, NULL, NOW()),
('What is a Stack?', 'Define a stack and give one example where it is used in programs.', 'THEORETICAL', 'DSA', 'Stack', 'BEGINNER', 420, 6, NULL, 'Stack is LIFO. Use-case: function call stack, undo operations.', NULL, NULL, NOW()),
('Big-O Basics', 'Explain what Big-O notation describes and convert simple loops to time complexity.', 'THEORETICAL', 'DSA', 'Complexity', 'BEGINNER', 420, 6, NULL, 'Big-O describes asymptotic upper bound. Single loop -> O(n). Nested -> O(n^2).', NULL, NULL, NOW());

-- DSA - INTERMEDIATE (5)
('Longest Substring Without Repeating Characters', 'Find length of longest substring without repeating characters.', 'CODING', 'DSA', 'Strings', 'INTERMEDIATE', 1200, 15, 'Use sliding window with a hashmap or array of last indices.', 'Sliding window expanding and contracting based on seen characters.', 'public int lengthOfLongestSubstring(String s) {\n    // Write your code here\n}', 'String length: up to 50000', NOW()),
('Valid Parentheses', 'Given a string of brackets, determine if the string is valid.', 'CODING', 'DSA', 'Stack', 'INTERMEDIATE', 1200, 15, 'Use stack to push opening brackets and pop when matching.', 'Match pairs using stack and map of pairs.', 'public boolean isValid(String s) {\n    // Write your code here\n}', 'String length: up to 10000', NOW()),
('Binary Search Explanation', 'Explain binary search and list the conditions when it can be applied.', 'THEORETICAL', 'DSA', 'Search', 'INTERMEDIATE', 600, 10, NULL, 'Binary search requires a monotonic (sorted) array or function. Complexity O(log n).', NULL, NULL, NOW()),
('Two Sum Variation', 'Given an array and target, return indices of two numbers that add up to target.', 'CODING', 'DSA', 'Hashing', 'INTERMEDIATE', 1200, 15, 'Use hashmap to store complement indices.', 'One-pass hashmap solution gives O(n) time.', 'public int[] twoSum(int[] nums, int target) {\n    // Write your code here\n}', 'Array length: up to 100000', NOW()),
('Graph BFS vs DFS', 'Compare BFS and DFS and provide common use-cases for each.', 'THEORETICAL', 'DSA', 'Graph', 'INTERMEDIATE', 600, 10, NULL, 'BFS for shortest path in unweighted graph; DFS for topological sort, connectivity.', NULL, NULL, NOW());

-- DSA - ADVANCED (5)
('LRU Cache', 'Design and implement a Least Recently Used cache with get and put operations.', 'CODING', 'DSA', 'Design', 'ADVANCED', 1800, 25, 'Use HashMap + Doubly Linked List for O(1) operations.', 'Maintain map to nodes and a DLL for order.', 'class LRUCache {\n    // Implement get and put\n}', 'Capacity: 1 to 3000', NOW()),
('Median of Two Sorted Arrays', 'Find the median of two sorted arrays in O(log(min(n,m))) time.', 'CODING', 'DSA', 'Binary Search', 'ADVANCED', 1800, 25, 'Binary search partition technique on smaller array.', 'Partition arrays into halves with equal left size.', 'public double findMedianSortedArrays(int[] nums1, int[] nums2) {\n    // Write your code here\n}', 'Each array length: 0 to 100000', NOW()),
('Dynamic Programming Concepts', 'Explain memoization vs tabulation and when to use each.', 'THEORETICAL', 'DSA', 'DP', 'ADVANCED', 900, 18, NULL, 'Memoization: top-down caching. Tabulation: bottom-up DP.', NULL, NULL, NOW()),
('Detect Cycle in Directed Graph', 'Given a directed graph, detect if it contains a cycle.', 'CODING', 'DSA', 'Graph', 'ADVANCED', 1800, 25, 'Use DFS with recursion stack or use Kahn''s algorithm for topological sort.', 'Use DFS visited + recursion stack to detect back edges.', 'public boolean hasCycle(int n, List<Integer>[] adj) {\n    // Write your code here\n}', 'Nodes: up to 100000, edges: up to 200000', NOW()),
('Regular Expression Matching', 'Implement regular expression matching with \'.\' and ''*'' where ''*'' means zero or more of previous element.', 'CODING', 'DSA', 'DP', 'ADVANCED', 1800, 25, 'Use DP table where dp[i][j] indicates match status.', 'Build DP bottom-up for pattern and string indices.', 'public boolean isMatch(String s, String p) {\n    // Write your code here\n}', 'String length: up to 30', NOW());

-- ===========================
-- DBMS (15)
-- ===========================
INSERT INTO questions (title, description, question_type, category, sub_category, difficulty_level, time_limit_seconds, points, hints, solution, code_template, constraints_info, created_at)
VALUES
-- DBMS - BEGINNER (5)
('Select All Records', 'Write a SQL query to select all columns from a table named ''employees''.', 'CODING', 'DBMS', 'SQL', 'BEGINNER', 600, 8, 'Use SELECT * FROM table.', 'SELECT * FROM employees;', 'SELECT \n-- Write your SQL query here\n', NULL, NOW()),
('Filter with WHERE', 'Find all employees with salary greater than 50000.', 'CODING', 'DBMS', 'SQL', 'BEGINNER', 600, 8, 'Use WHERE clause with comparison operator.', 'SELECT * FROM employees WHERE salary > 50000;', 'SELECT \n-- Write your SQL query here\n', NULL, NOW()),
('Primary vs Foreign Key', 'Explain primary key and foreign key constraints and their role.', 'THEORETICAL', 'DBMS', 'Constraints', 'BEGINNER', 420, 6, NULL, 'Primary key uniquely identifies a row; foreign key references another table''s primary key.', NULL, NULL, NOW()),
('Normalization Basics', 'What is database normalization and why is it used?', 'THEORETICAL', 'DBMS', 'Concepts', 'BEGINNER', 420, 6, NULL, 'Normalization reduces redundancy and improves consistency (1NF,2NF,3NF).', NULL, NULL, NOW()),
('Aggregate Functions', 'Give examples of aggregate functions and a simple query using AVG.', 'THEORETICAL', 'DBMS', 'SQL', 'BEGINNER', 420, 6, NULL, 'AVG, SUM, COUNT, MIN, MAX. Example: SELECT AVG(salary) FROM employees;', NULL, NULL, NOW());

-- DBMS - INTERMEDIATE (5)
('Join Tables', 'Write SQL to join employees and departments to get employee name and department name.', 'CODING', 'DBMS', 'SQL', 'INTERMEDIATE', 1200, 15, 'Use INNER JOIN with ON clause.', 'SELECT e.name, d.name FROM employees e INNER JOIN departments d ON e.dept_id = d.id;', 'SELECT \n-- Write your SQL query here\n', NULL, NOW()),
('GROUP BY and HAVING', 'Find departments with average salary greater than 60000.', 'CODING', 'DBMS', 'SQL', 'INTERMEDIATE', 1200, 15, 'Use GROUP BY and HAVING to filter aggregated results.', 'SELECT dept_id, AVG(salary) FROM employees GROUP BY dept_id HAVING AVG(salary) > 60000;', 'SELECT \n-- Write your SQL query here\n', NULL, NOW()),
('Transactions and ACID', 'Explain transactions and ACID properties with an example.', 'THEORETICAL', 'DBMS', 'Concepts', 'INTERMEDIATE', 900, 12, NULL, 'A transaction is a sequence of DB ops that is atomic, consistent, isolated, durable.', NULL, NULL, NOW()),
('Indexes and Performance', 'How do database indexes improve query performance and what are tradeoffs?', 'THEORETICAL', 'DBMS', 'Performance', 'INTERMEDIATE', 900, 12, NULL, 'Indexes speed up reads but cost space and slow writes; choose columns wisely.', NULL, NULL, NOW()),
('Second Highest Salary', 'Write SQL to find the second highest salary in employees table.', 'CODING', 'DBMS', 'SQL', 'INTERMEDIATE', 1200, 15, 'Use subqueries or window functions.', 'Example using window: SELECT DISTINCT salary FROM (SELECT salary, DENSE_RANK() OVER (ORDER BY salary DESC) dr FROM employees) t WHERE dr = 2;', 'SELECT \n-- Write your SQL query here\n', NULL, NOW());

-- DBMS - ADVANCED (5)
('Complex Correlated Subquery', 'Find employees earning more than their department average.', 'CODING', 'DBMS', 'SQL', 'ADVANCED', 1800, 25, 'Use correlated subquery referencing outer alias.', 'SELECT * FROM employees e WHERE salary > (SELECT AVG(salary) FROM employees WHERE dept_id = e.dept_id);', 'SELECT \n-- Write your SQL query here\n', NULL, NOW()),
('Window Functions', 'Use window functions to rank employees by salary within department.', 'CODING', 'DBMS', 'SQL', 'ADVANCED', 1800, 25, 'Use ROW_NUMBER or RANK with PARTITION BY.', 'SELECT name, ROW_NUMBER() OVER (PARTITION BY dept_id ORDER BY salary DESC) rn FROM employees;', 'SELECT \n-- Write your SQL query here\n', NULL, NOW()),
('Query Optimization', 'Explain basic query optimization techniques and EXPLAIN output interpretation.', 'THEORETICAL', 'DBMS', 'Performance', 'ADVANCED', 1200, 20, NULL, 'Use indexes, avoid SELECT *, rewrite joins, check EXPLAIN plan for sequential scans.', NULL, NULL, NOW()),
('Isolation Levels', 'Describe transaction isolation levels and anomalies they prevent.', 'THEORETICAL', 'DBMS', 'Concepts', 'ADVANCED', 1200, 20, NULL, 'Read uncommitted, read committed, repeatable read, serializable - prevent dirty/phantom/non-repeatable reads accordingly.', NULL, NULL, NOW()),
('Partitioning Strategies', 'Explain horizontal vs vertical partitioning and when to use them.', 'THEORETICAL', 'DBMS', 'Architecture', 'ADVANCED', 1200, 20, NULL, 'Horizontal partitions split rows, vertical splits columns. Use for scalability and locality.', NULL, NULL, NOW());

-- ===========================
-- OOP (15)
-- ===========================
INSERT INTO questions (title, description, question_type, category, sub_category, difficulty_level, time_limit_seconds, points, hints, solution, code_template, constraints_info, created_at)
VALUES
-- OOP - BEGINNER (5)
('Create a Class', 'Create a simple Car class with properties brand, model, year.', 'CODING', 'OOP', 'Classes', 'BEGINNER', 600, 8, 'Define private fields and constructor with getters/setters.', 'Provide class with fields and accessors.', 'public class Car {\n    private String brand;\n    private String model;\n    private int year;\n    // constructor, getters and setters\n}', NULL, NOW()),
('What is Encapsulation?', 'Explain encapsulation in OOP and its benefits.', 'THEORETICAL', 'OOP', 'Concepts', 'BEGINNER', 420, 6, NULL, 'Encapsulation bundles data and methods; enforces access control.', NULL, NULL, NOW()),
('Constructor Overloading', 'Demonstrate constructor overloading in a Rectangle class.', 'CODING', 'OOP', 'Constructors', 'BEGINNER', 600, 8, 'Create multiple constructors with different parameter lists.', 'Provide overloaded constructors example.', 'public class Rectangle {\n    private int w, h;\n    public Rectangle() { this.w = 1; this.h = 1; }\n    public Rectangle(int w, int h) { this.w = w; this.h = h; }\n}', NULL, NOW()),
('Class vs Object', 'Define class and object with real world example.', 'THEORETICAL', 'OOP', 'Concepts', 'BEGINNER', 420, 6, NULL, 'Class is blueprint; object is instance (e.g., Car class and myCar object).', NULL, NULL, NOW()),
('Access Modifiers', 'Explain public, private, protected and default access modifiers.', 'THEORETICAL', 'OOP', 'Basics', 'BEGINNER', 420, 6, NULL, 'public: everyone; private: class only; protected: package + subclasses; default: package.', NULL, NULL, NOW());

-- OOP - INTERMEDIATE (5)
('Inheritance Example', 'Demonstrate inheritance using Vehicle and Car classes.', 'CODING', 'OOP', 'Inheritance', 'INTERMEDIATE', 900, 12, 'Use extends and super() to call parent constructor.', 'Show method overriding and reuse.', 'public class Vehicle { protected int wheels; }\npublic class Car extends Vehicle { public Car(){ super(); } }', NULL, NOW()),
('Polymorphism', 'Show runtime polymorphism example using method overriding.', 'THEORETICAL', 'OOP', 'Polymorphism', 'INTERMEDIATE', 600, 10, NULL, 'Use parent reference to hold child instance and call overridden method.', NULL, NULL, NOW()),
('Abstract Classes vs Interfaces', 'Explain differences and examples for Java.', 'THEORETICAL', 'OOP', 'Concepts', 'INTERMEDIATE', 900, 12, NULL, 'Abstract classes can hold implemented methods and state; interfaces define contracts (Java 8+ default methods exist).', NULL, NULL, NOW()),
('Design Singleton', 'Implement thread-safe Singleton pattern in Java.', 'CODING', 'OOP', 'Design Patterns', 'INTERMEDIATE', 900, 12, 'Use double-checked locking or enum-based singleton.', 'Provide thread-safe getInstance implementation.', 'public class Singleton {\n    private static volatile Singleton instance;\n    private Singleton() {}\n    public static Singleton getInstance(){\n        if(instance==null){\n            synchronized(Singleton.class){\n                if(instance==null) instance = new Singleton();\n            }\n        }\n        return instance;\n    }\n}', NULL, NOW()),
('SOLID Principles Intro', 'List and briefly explain SOLID principles.', 'THEORETICAL', 'OOP', 'Principles', 'INTERMEDIATE', 600, 10, NULL, 'Single Responsibility, Open/Closed, Liskov, Interface Segregation, Dependency Inversion.', NULL, NULL, NOW());

-- OOP - ADVANCED (5)
('Design Patterns Comparison', 'Compare Factory, Builder, and Strategy patterns and their use-cases.', 'THEORETICAL', 'OOP', 'Design Patterns', 'ADVANCED', 1200, 20, NULL, 'Factory: create objects; Builder: construct complex objects; Strategy: swap algorithms at runtime.', NULL, NULL, NOW()),
('Dependency Injection', 'Explain DI and show simple example using constructor injection.', 'THEORETICAL', 'OOP', 'Architecture', 'ADVANCED', 1200, 20, NULL, 'DI decouples creation of dependencies from usage; use frameworks or manual injection.', NULL, NULL, NOW()),
('Design a Plugin System', 'Design a plugin architecture that allows runtime extension of app features.', 'THEORETICAL', 'OOP', 'Architecture', 'ADVANCED', 1500, 25, NULL, 'Use interfaces, dynamic loading, versioning, and isolation for plugins.', NULL, NULL, NOW()),
('Multiple Inheritance Problems', 'Explain diamond problem and how Java avoids it.', 'THEORETICAL', 'OOP', 'Inheritance', 'ADVANCED', 900, 15, NULL, 'Java uses interfaces (no multiple class inheritance) and default method conflict rules.', NULL, NULL, NOW()),
('Refactoring Example', 'Describe refactoring steps to convert duplicated code into reusable method.', 'THEORETICAL', 'OOP', 'Maintenance', 'ADVANCED', 900, 15, NULL, 'Identify duplication, extract method, update callers, run tests.', NULL, NULL, NOW());

-- ===========================
-- OS (15)
-- ===========================
INSERT INTO questions (title, description, question_type, category, sub_category, difficulty_level, time_limit_seconds, points, hints, solution, code_template, constraints_info, created_at)
VALUES
-- OS - BEGINNER (5)
('What is an Operating System?', 'Define an operating system and list its main functions.', 'THEORETICAL', 'OS', 'Basics', 'BEGINNER', 600, 8, NULL, 'Manages hardware, provides process and memory management, handles I/O and file systems.', NULL, NULL, NOW()),
('Process vs Thread', 'Explain the difference between a process and a thread.', 'THEORETICAL', 'OS', 'Basics', 'BEGINNER', 420, 6, NULL, 'Process: independent execution with its own memory. Thread: lightweight within process sharing memory.', NULL, NULL, NOW()),
('System Calls', 'What are system calls and how do user-space programs use them?', 'THEORETICAL', 'OS', 'Interfaces', 'BEGINNER', 420, 6, NULL, 'System calls request kernel services via traps or interrupts; e.g., read, write, fork.', NULL, NULL, NOW()),
('Memory Hierarchy', 'Describe memory hierarchy and why caches are faster than main memory.', 'THEORETICAL', 'OS', 'Memory', 'BEGINNER', 420, 6, NULL, 'Caches are smaller and closer to CPU with faster access times; exploit temporal/spatial locality.', NULL, NULL, NOW()),
('File System Basics', 'Explain what a file system is and common operations supported.', 'THEORETICAL', 'OS', 'Storage', 'BEGINNER', 420, 6, NULL, 'File systems organize data into files/directories and provide operations like create, read, write, delete.', NULL, NULL, NOW());

-- OS - INTERMEDIATE (5)
('Producer-Consumer Problem', 'Solve producer-consumer using semaphores or condition variables.', 'CODING', 'OS', 'Synchronization', 'INTERMEDIATE', 1200, 15, 'Use buffer, semaphores for full and empty, and mutex.', 'Implement producer/consumer with proper synchronization primitives.', 'class ProducerConsumer {\n    // Implement producers and consumers with semaphores\n}', NULL, NOW()),
('Deadlock Conditions', 'List and explain four conditions necessary for deadlock.', 'THEORETICAL', 'OS', 'Deadlock', 'INTERMEDIATE', 600, 10, NULL, 'Mutual Exclusion, Hold and Wait, No Preemption, Circular Wait.', NULL, NULL, NOW()),
('Paging vs Segmentation', 'Compare paging and segmentation in memory management.', 'THEORETICAL', 'OS', 'Memory', 'INTERMEDIATE', 600, 10, NULL, 'Paging divides memory into equal frames; segmentation uses variable-sized segments by logical division.', NULL, NULL, NOW()),
('Context Switching', 'Explain what happens during a context switch and its cost.', 'THEORETICAL', 'OS', 'Scheduling', 'INTERMEDIATE', 600, 10, NULL, 'Save CPU registers, update process control block, TLB flush cost may be incurred.', NULL, NULL, NOW()),
('Scheduling Algorithms', 'Compare Round Robin, SJF, and Priority scheduling and their tradeoffs.', 'THEORETICAL', 'OS', 'Scheduling', 'INTERMEDIATE', 900, 12, NULL, 'RR is fair with time quantum; SJF optimal for throughput but needs prior knowledge; priority may starve low-priority tasks.', NULL, NULL, NOW());

-- OS - ADVANCED (5)
('Banker''s Algorithm', 'Describe and implement Banker''s algorithm for deadlock avoidance.', 'THEORETICAL', 'OS', 'Deadlock', 'ADVANCED', 1500, 25, NULL, 'Use allocation, max, need matrices and check for safe sequence.', NULL, NULL, NOW()),
('Page Replacement Algorithms', 'Explain LRU, FIFO, and Optimal page replacement and complexity.', 'THEORETICAL', 'OS', 'Memory', 'ADVANCED', 1200, 20, NULL, 'LRU uses recency, FIFO uses arrival order, Optimal replaces page used farthest in future (theoretical).', NULL, NULL, NOW()),
('Implement Mutex with Atomic Ops', 'Design a simple spinlock using atomic compare-and-swap operations.', 'CODING', 'OS', 'Concurrency', 'ADVANCED', 1800, 25, 'Use atomic CAS to acquire lock and release by setting variable to 0.', 'Spin until CAS succeeds.', 'class SpinLock { private AtomicInteger lock = new AtomicInteger(0); void lock(){ while(!lock.compareAndSet(0,1)); } void unlock(){ lock.set(0);} }', NULL, NOW()),
('Virtual Memory Design', 'Design virtual memory addressing and translation using page tables.', 'THEORETICAL', 'OS', 'Memory', 'ADVANCED', 1200, 20, NULL, 'Discuss single-level, multi-level page tables and TLB caching.', NULL, NULL, NOW()),
('Concurrency Bugs', 'List common concurrency bugs and general approaches to avoid them.', 'THEORETICAL', 'OS', 'Concurrency', 'ADVANCED', 1200, 20, NULL, 'Race conditions, deadlocks, livelocks; use locks, atomic ops, stateless design to avoid.', NULL, NULL, NOW());

-- ===========================
-- WEB_DEV (15)
-- ===========================
INSERT INTO questions (title, description, question_type, category, sub_category, difficulty_level, time_limit_seconds, points, hints, solution, code_template, constraints_info, created_at)
VALUES
-- WEB_DEV - BEGINNER (5)
('HTML Basics', 'Create a simple webpage with heading and paragraph.', 'CODING', 'WEB_DEV', 'Frontend', 'BEGINNER', 600, 8, 'Use <h1> and <p> tags.', '<!DOCTYPE html>\n<html>\n<body>\n<h1>Title</h1>\n<p>Paragraph</p>\n</body>\n</html>', '<!DOCTYPE html>\n<html>\n<body>\n<!-- Write your code here -->\n</body>\n</html>', NULL, NOW()),
('CSS Styling', 'Center content and add background color using CSS.', 'CODING', 'WEB_DEV', 'Frontend', 'BEGINNER', 600, 8, 'Use text-align and background-color properties.', '.center{ text-align:center; } body{ background-color: #f0f0f0; }', '/* CSS here */', NULL, NOW()),
('What is HTTP?', 'Explain the role of HTTP in client-server communication.', 'THEORETICAL', 'WEB_DEV', 'Networking', 'BEGINNER', 420, 6, NULL, 'HTTP is application layer protocol for requests/responses over TCP.', NULL, NULL, NOW()),
('DOM Basics', 'What is the DOM and how do you manipulate it in JS?', 'THEORETICAL', 'WEB_DEV', 'Frontend', 'BEGINNER', 420, 6, NULL, 'DOM is document object model; use document.querySelector and element methods to update.', NULL, NULL, NOW()),
('JavaScript Variables', 'Explain let vs var vs const in JavaScript.', 'THEORETICAL', 'WEB_DEV', 'Frontend', 'BEGINNER', 420, 6, NULL, 'var: function-scoped hoisting; let/const: block-scoped; const cannot be reassigned.', NULL, NULL, NOW());

-- WEB_DEV - INTERMEDIATE (5)
('Responsive Layout', 'Build a responsive card layout using CSS Grid/Flexbox.', 'CODING', 'WEB_DEV', 'Frontend', 'INTERMEDIATE', 1200, 15, 'Use media queries and auto-fit grid.', '<div class="cards">...</div>', '<div class="cards">\n <!-- Write markup and CSS here -->\n</div>', NULL, NOW()),
('REST API Design', 'Explain REST principles and design a simple resource-based API.', 'THEORETICAL', 'WEB_DEV', 'Backend', 'INTERMEDIATE', 900, 12, NULL, 'Use stateless operations, appropriate HTTP verbs, resource URIs, and status codes.', NULL, NULL, NOW()),
('AJAX / Fetch', 'Write JS code to fetch JSON data from API and render it.', 'CODING', 'WEB_DEV', 'Frontend', 'INTERMEDIATE', 1200, 15, 'Use fetch with async/await and handle errors.', 'Example: const res = await fetch(url); const data = await res.json();', 'async function getData(){\n const res = await fetch(\"/api/data\");\n const data = await res.json();\n // render\n}', NULL, NOW()),
('Database for Web App', 'Choose a database for a blog platform and justify choice.', 'THEORETICAL', 'WEB_DEV', 'Backend', 'INTERMEDIATE', 900, 12, NULL, 'Relational DB if structured relations needed; NoSQL for flexible schema or high write volumes; tradeoffs explained.', NULL, NULL, NOW()),
('Authentication Methods', 'Explain sessions vs JWT and when to use each.', 'THEORETICAL', 'WEB_DEV', 'Security', 'INTERMEDIATE', 900, 12, NULL, 'Sessions: server-side state; JWT: stateless tokens; consider revocation and security.', NULL, NULL, NOW());

-- WEB_DEV - ADVANCED (5)
('JWT Authentication', 'Implement JWT-based auth and protected routes in Node.js.', 'CODING', 'WEB_DEV', 'Backend', 'ADVANCED', 1800, 25, 'Use jsonwebtoken package and middleware to verify tokens.', 'Set signed token on login and validate on protected routes.', 'const jwt = require(\"jsonwebtoken\");\n// sign and verify tokens', NULL, NOW()),
('React State Management', 'Compare Context API vs Redux and when to use each.', 'THEORETICAL', 'WEB_DEV', 'Frontend', 'ADVANCED', 1200, 20, NULL, 'Context suited for low-frequency global state; Redux for complex predictable state with middleware.', NULL, NULL, NOW()),
('Web Security Threats', 'List common web vulnerabilities (XSS, CSRF, SQLi) and prevention techniques.', 'THEORETICAL', 'WEB_DEV', 'Security', 'ADVANCED', 1200, 20, NULL, 'Escape output, use parameterized queries, CSRF tokens, CSP headers.', NULL, NULL, NOW()),
('Optimize Frontend Performance', 'Discuss techniques to reduce TTFB and improve LCP.', 'THEORETICAL', 'WEB_DEV', 'Performance', 'ADVANCED', 1200, 20, NULL, 'Use CDNs, minimize render-blocking CSS/JS, lazy-load images, compress assets.', NULL, NULL, NOW()),
('Design Real-time Feature', 'Design real-time notifications system for a social app.', 'THEORETICAL', 'WEB_DEV', 'Architecture', 'ADVANCED', 1800, 25, NULL, 'Use WebSockets or server-sent events; use message broker for scaling; deliver via push notifications.', NULL, NULL, NOW());

-- ===========================
-- SYSTEM_DESIGN (15)
-- ===========================
INSERT INTO questions (title, description, question_type, category, sub_category, difficulty_level, time_limit_seconds, points, hints, solution, code_template, constraints_info, created_at)
VALUES
-- SYSTEM_DESIGN - BEGINNER (5)
('Load Balancer Basics', 'Explain what a load balancer is and basic algorithms (round robin, least connections).', 'THEORETICAL', 'SYSTEM_DESIGN', 'Networking', 'BEGINNER', 900, 10, NULL, 'Load balancer distributes requests across instances using algorithms like round-robin or least-connections.', NULL, NULL, NOW()),
('Caching Basics', 'What is caching and where to place caches in an application stack?', 'THEORETICAL', 'SYSTEM_DESIGN', 'Caching', 'BEGINNER', 600, 8, NULL, 'Caches can be placed in browser, CDN, app server, or DB layer for reducing latency and load.', NULL, NULL, NOW()),
('Scale Vertically vs Horizontally', 'Describe vertical and horizontal scaling and trade-offs.', 'THEORETICAL', 'SYSTEM_DESIGN', 'Scalability', 'BEGINNER', 600, 8, NULL, 'Vertical: bigger machine; horizontal: more machines. Horizontal is more resilient and scalable.', NULL, NULL, NOW()),
('Monolith vs Microservices', 'Compare monolithic and microservice architectures and when to choose each.', 'THEORETICAL', 'SYSTEM_DESIGN', 'Architecture', 'BEGINNER', 600, 8, NULL, 'Monolith easier to build; microservices better for independent deployment and scaling but complex.', NULL, NULL, NOW()),
('Design Checklist', 'List key non-functional requirements to consider while designing a system.', 'THEORETICAL', 'SYSTEM_DESIGN', 'Requirements', 'BEGINNER', 600, 8, NULL, 'Scalability, reliability, availability, performance, maintainability, security, cost.', NULL, NULL, NOW());

-- SYSTEM_DESIGN - INTERMEDIATE (5)
('URL Shortener', 'Design a scalable URL shortener similar to bit.ly.', 'THEORETICAL', 'SYSTEM_DESIGN', 'Design', 'INTERMEDIATE', 1500, 20, NULL, 'Use hashing or base62 encode IDs, consider collision handling, redirection, analytics, and caching.', NULL, NULL, NOW()),
('Design a CDN', 'High-level design for a CDN serving static assets globally.', 'THEORETICAL', 'SYSTEM_DESIGN', 'Networking', 'INTERMEDIATE', 1500, 20, NULL, 'Edge caches, origin, TTL, cache invalidation, geo-routing, and load balancing.', NULL, NULL, NOW()),
('Rate Limiting', 'Design rate limiting for APIs and discuss algorithms.', 'THEORETICAL', 'SYSTEM_DESIGN', 'Security', 'INTERMEDIATE', 900, 12, NULL, 'Token bucket and leaky bucket algorithms; store counters in in-memory store like Redis.', NULL, NULL, NOW()),
('Design Notification Service', 'Design a service to deliver email and push notifications at scale.', 'THEORETICAL', 'SYSTEM_DESIGN', 'Messaging', 'INTERMEDIATE', 1500, 20, NULL, 'Use queues, backoff/retries, batching, idempotency, and monitoring.', NULL, NULL, NOW()),
('Consistent Hashing', 'Explain consistent hashing and where it is used.', 'THEORETICAL', 'SYSTEM_DESIGN', 'Distributed Systems', 'INTERMEDIATE', 900, 12, NULL, 'Used in distributed caches and sharding to reduce remapping on node changes.', NULL, NULL, NOW());

-- SYSTEM_DESIGN - ADVANCED (5)
('Design YouTube-like System', 'Design a video streaming and recommendation architecture at scale.', 'THEORETICAL', 'SYSTEM_DESIGN', 'Architecture', 'ADVANCED', 2400, 30, NULL, 'Consider storage, transcoding, CDN, metadata, recommendations, and batch/real-time pipelines.', NULL, NULL, NOW()),
('Design a Chat System', 'Design a real-time chat system with presence and message delivery guarantees.', 'THEORETICAL', 'SYSTEM_DESIGN', 'Communication', 'ADVANCED', 2400, 30, NULL, 'WebSockets, message brokers, persistence, replication, and offline delivery strategies.', NULL, NULL, NOW()),
('Database Sharding Strategy', 'Explain strategies to shard a large user database and how to route queries.', 'THEORETICAL', 'SYSTEM_DESIGN', 'Data', 'ADVANCED', 1500, 22, NULL, 'Range, hash, directory-based sharding; consider rebalancing and cross-shard joins.', NULL, NULL, NOW()),
('Design for High Availability', 'Describe architecture choices to achieve 99.99% availability.', 'THEORETICAL', 'SYSTEM_DESIGN', 'Reliability', 'ADVANCED', 1500, 22, NULL, 'Multi-AZ deployments, failover, replication, circuit breakers, retries, monitoring and auto-healing.', NULL, NULL, NOW()),
('Event Sourcing vs CRUD', 'Compare event sourcing with traditional CRUD and when to choose event sourcing.', 'THEORETICAL', 'SYSTEM_DESIGN', 'Architecture', 'ADVANCED', 1500, 22, NULL, 'Event sourcing stores state as events, enabling auditability and replay but is more complex.', NULL, NULL, NOW());

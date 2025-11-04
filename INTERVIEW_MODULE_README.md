# Interview Module - Complete Documentation

## 🎯 Overview

A comprehensive interview coaching module built from scratch with **Spring Boot** backend, featuring:

- ✅ Interview Slot Management
- ✅ Question Bank System with Test Cases
- ✅ Live Coding Environment Support
- ✅ Auto-Evaluation & Scoring
- ✅ AI-Powered Feedback
- ✅ Skills Analytics Dashboard

---

## 📊 Database Schema

### Core Tables:
1. **interview_slots** - Scheduled interview slots
2. **questions** - Question bank (DSA, DBMS, System Design, etc.)
3. **test_cases** - Test cases for coding questions
4. **interviews** - Active/completed interview sessions
5. **interview_responses** - User answers and submissions
6. **interview_evaluations** - AI-generated feedback
7. **user_skills_analytics** - Progress tracking

---

## 🔧 Setup Instructions

### 1. Database Setup

Run the SQL schema file:
```bash
psql -U postgres -d interview_coaching -f backend/src/main/resources/interview-schema.sql
```

Or let Spring Boot auto-create tables (already configured in `application.properties`):
```properties
spring.jpa.hibernate.ddl-auto=update
```

### 2. Clean & Rebuild Project

**Important:** Clear IDE cache to resolve lint errors:

```bash
# In IntelliJ IDEA
File → Invalidate Caches / Restart

# Or rebuild from command line
cd backend
mvn clean install
```

### 3. Run the Application

```bash
cd backend
mvn spring-boot:run
```

Server will start at: `http://localhost:8080`

---

## 📡 API Endpoints

### **Interview Management**

#### Start Interview
```http
POST /api/interviews/start
Content-Type: application/json

{
  "slotId": 1,               // Optional
  "topic": "DSA",
  "difficultyLevel": "INTERMEDIATE",
  "questionCount": 5
}

Response: { interview, questions[], message }
```

#### Submit Answer
```http
POST /api/interviews/{interviewId}/submit-answer

{
  "questionId": 1,
  "answer": "Text answer for theoretical questions",
  "codeSubmission": "public class Solution {...}",
  "programmingLanguage": "JAVA",
  "timeTakenSeconds": 450,
  "hintsUsed": 1
}

Response: { response, message }
```

#### Complete Interview
```http
POST /api/interviews/{interviewId}/complete

Response: { evaluation, message }
```

#### Get Interview History
```http
GET /api/interviews/history

Response: [ interviews[] ]
```

#### Get Interview Details
```http
GET /api/interviews/{interviewId}

Response: { interview details }
```

---

### **Question Management**

#### Get All Questions
```http
GET /api/questions

Response: [ questions[] ]
```

#### Create Question
```http
POST /api/questions

{
  "title": "Two Sum",
  "description": "Find two numbers that add up to target",
  "questionType": "CODING",
  "category": "DSA",
  "subCategory": "Arrays",
  "difficultyLevel": "BEGINNER",
  "timeLimitSeconds": 600,
  "points": 15,
  "hints": "Use HashMap",
  "codeTemplate": "public int[] twoSum(...) {}",
  "constraintsInfo": "n <= 10^4"
}
```

#### Add Test Case
```http
POST /api/questions/{questionId}/test-cases

{
  "inputData": "[2,7,11,15], target=9",
  "expectedOutput": "[0,1]",
  "isSample": true,
  "isHidden": false,
  "points": 5,
  "explanation": "2 + 7 = 9"
}
```

#### Get Test Cases
```http
GET /api/questions/{questionId}/test-cases

Response: [ testCases[] ]
```

---

### **Interview Slots**

#### Get Available Slots
```http
GET /api/interview-slots/available

Response: [ slots[] ]
```

#### Create Slot
```http
POST /api/interview-slots

{
  "title": "DSA Mock Interview",
  "description": "Practice arrays and strings",
  "topic": "DSA",
  "difficultyLevel": "INTERMEDIATE",
  "durationMinutes": 60,
  "scheduledDateTime": "2025-01-10T10:00:00",
  "maxParticipants": 1
}
```

#### Book Slot
```http
PUT /api/interview-slots/{id}/book
```

---

### **Analytics**

#### Get My Analytics
```http
GET /api/analytics/my-analytics

Response: [ analytics by topic ]
```

#### Get Top Skills
```http
GET /api/analytics/top-skills

Response: [ top performing topics ]
```

#### Get Weak Areas
```http
GET /api/analytics/weak-areas

Response: [ areas needing improvement ]
```

#### Get Progress
```http
GET /api/analytics/progress

Response: { progress: 75.5 }
```

#### Get Suggestions
```http
GET /api/analytics/suggestions

Response: { suggestions: "Focus on improving: DBMS, OS" }
```

---

## 🎨 Features

### 1. **Question Types Supported**
- **CODING**: Algorithmic problems with test cases
- **MCQ**: Multiple choice questions
- **THEORETICAL**: Concept-based questions
- **SYSTEM_DESIGN**: Architecture problems

### 2. **Topics Covered**
- DSA (Data Structures & Algorithms)
- DBMS (Database Management)
- OS (Operating Systems)
- CN (Computer Networks)
- OOP (Object-Oriented Programming)
- WEB_DEV (Web Development)
- SYSTEM_DESIGN

### 3. **Difficulty Levels**
- BEGINNER
- INTERMEDIATE
- ADVANCED

### 4. **Evaluation Metrics**
- Overall Score (%)
- Code Quality Score
- Logical Reasoning Score
- Time Management Score
- Problem Solving Score

### 5. **Skills Analytics**
- Accuracy percentage by topic
- Total attempts vs successful attempts
- Average time per question
- Skill level progression (BEGINNER → EXPERT)

---

## 🧪 Testing the API

### Using Postman/Curl:

```bash
# 1. Start an interview
curl -X POST http://localhost:8080/api/interviews/start \
  -H "Content-Type: application/json" \
  -d '{"topic":"DSA","difficultyLevel":"INTERMEDIATE","questionCount":3}'

# 2. Submit an answer
curl -X POST http://localhost:8080/api/interviews/1/submit-answer \
  -H "Content-Type: application/json" \
  -d '{"questionId":1,"answer":"Using HashMap approach","timeTakenSeconds":300}'

# 3. Complete interview
curl -X POST http://localhost:8080/api/interviews/1/complete

# 4. Get analytics
curl http://localhost:8080/api/analytics/my-analytics
```

---

## 📝 Code Structure

```
backend/src/main/java/com/interviewcoaching/
├── models/interview/
│   ├── Interview.java
│   ├── InterviewSlot.java
│   ├── Question.java
│   ├── TestCase.java
│   ├── InterviewResponse.java
│   ├── InterviewEvaluation.java
│   └── UserSkillsAnalytics.java
│
├── repositories/interview/
│   ├── InterviewRepository.java
│   ├── InterviewSlotRepository.java
│   ├── QuestionRepository.java
│   ├── TestCaseRepository.java
│   ├── InterviewResponseRepository.java
│   ├── InterviewEvaluationRepository.java
│   └── UserSkillsAnalyticsRepository.java
│
├── services/interview/
│   ├── InterviewService.java
│   ├── InterviewSlotService.java
│   ├── QuestionService.java
│   ├── CodeEvaluationService.java
│   └── UserSkillsAnalyticsService.java
│
├── controllers/interview/
│   ├── InterviewController.java
│   ├── QuestionController.java
│   ├── InterviewSlotController.java
│   └── AnalyticsController.java
│
└── dto/interview/
    ├── InterviewStartRequest.java
    ├── AnswerSubmitRequest.java
    ├── InterviewSlotRequest.java
    └── QuestionRequest.java
```

---

## 🚀 Next Steps

### 1. **Frontend Integration**
Create React components to:
- Display interview lobby with slot selection
- Show questions one by one
- Embed Monaco Editor for code submission
- Display real-time evaluation feedback
- Show analytics dashboard

### 2. **Code Execution Engine**
Integrate with:
- **Judge0 API** (recommended)
- **HackerRank API**
- Custom Docker containers

### 3. **AI Enhancement**
- Integrate OpenAI API for better feedback
- Add code review suggestions
- Natural language understanding for theoretical answers

### 4. **Additional Features**
- Video recording during interviews
- Peer-to-peer mock interviews
- Interview scheduling with calendar integration
- Email notifications
- Leaderboards

---

## 🐛 Troubleshooting

### IDE Lint Errors
If you see "type already defined" or "syntax errors":
1. Close IDE
2. Run `mvn clean`
3. Delete `.idea` folder (IntelliJ) or `.project` files (Eclipse)
4. Reimport project
5. Rebuild

### Database Connection Issues
Check `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/interview_coaching
spring.datasource.username=postgres
spring.datasource.password=root
```

---

## 📞 Support

For issues or questions, check:
- Spring Boot logs: `backend/logs/`
- Database logs: Check PostgreSQL logs
- API responses: Use Postman/Curl for debugging

---

**Module Created By:** AI Assistant
**Last Updated:** November 2025
**Version:** 1.0.0

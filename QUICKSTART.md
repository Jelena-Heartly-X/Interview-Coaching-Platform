# 🚀 QUICK START GUIDE

## Get Your Interview Module Running in 5 Minutes!

---

## ⚡ STEP 1: Seed the Database

Run this command in PowerShell:

```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/questions/seed" -Method POST -ContentType "application/json"
```

**Expected Output:**
```json
{
  "message": "Questions seeded successfully"
}
```

✅ This adds 20+ interview questions to your database!

---

## ⚡ STEP 2: Verify Questions Were Added

Open in browser or Postman:
```
http://localhost:8080/api/questions
```

**You should see:**
- Array of 20+ questions
- Different categories (DSA, DBMS, OS, OOP, etc.)
- Different difficulty levels

---

## ⚡ STEP 3: Open Frontend

Your frontend should already be running on:
```
http://localhost:3001
```

If not, start it:
```bash
cd frontend
npm start
```

---

## ⚡ STEP 4: Test the Interview Flow

1. **Navigate to Interview Lobby**
   - Should already be there when you open the app

2. **Configure Your Interview:**
   - Interview Type: Technical
   - Difficulty Level: Medium
   - Duration: 30 minutes

3. **Click "Start Interview"**
   - The button should now work!
   - You'll be redirected to the interview room

4. **Answer Questions:**
   - Write code or text answers
   - Submit when done

5. **Complete Interview:**
   - Click "Submit Interview" on last question
   - View your results and AI feedback!

---

## 📊 CHECK YOUR DATABASE

Open your PostgreSQL database and run these queries:

```sql
-- Check questions
SELECT COUNT(*) FROM questions;
-- Should return: 20+

-- Check questions by category
SELECT category, COUNT(*) FROM questions GROUP BY category;

-- Check test cases
SELECT COUNT(*) FROM test_cases;

-- After taking an interview:

-- Check interviews
SELECT * FROM interviews;

-- Check interview questions
SELECT * FROM interview_questions;

-- Check responses
SELECT * FROM interview_responses;

-- Check evaluations
SELECT * FROM interview_evaluations;

-- Check analytics
SELECT * FROM user_skills_analytics;
```

---

## 🎯 WHAT TO EXPECT

### ✅ Working Features:

1. **Interview Configuration:**
   - Select type, difficulty, duration
   - Optional slot selection

2. **Question Display:**
   - Random questions based on difficulty
   - Different types (coding, theory, MCQ)

3. **Code Editor:**
   - Monaco editor for coding questions
   - Syntax highlighting
   - Multiple language support

4. **Answer Submission:**
   - Stores your code
   - Mock execution (randomly passes 70% of tests)
   - Calculates scores

5. **AI Feedback:**
   - Overall score
   - Code quality analysis
   - Time management feedback
   - Strengths and weaknesses
   - Improvement suggestions

6. **Analytics:**
   - Tracks your progress
   - Identifies weak areas
   - Shows improvement trends
   - Personalized recommendations

---

## 🔍 TESTING CHECKLIST

- [ ] Backend running on port 8080
- [ ] Frontend running on port 3001
- [ ] Questions seeded (20+ in database)
- [ ] Can start interview
- [ ] Can view questions
- [ ] Can submit answers
- [ ] Can complete interview
- [ ] Can view results
- [ ] Can see analytics

---

## 🐛 COMMON ISSUES & FIXES

### Issue: "Start Interview" button disabled
**Fix:** Questions not seeded. Run seed command again.

### Issue: No questions showing
**Fix:**
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/questions/seed" -Method POST
```

### Issue: Backend not running
**Fix:**
```bash
cd backend
mvn spring-boot:run
```

### Issue: Frontend not running
**Fix:**
```bash
cd frontend
npm install
npm start
```

### Issue: Database connection error
**Fix:** Check `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/interview_coaching
spring.datasource.username=postgres
spring.datasource.password=root
```

---

## 🎉 SUCCESS INDICATORS

You'll know everything is working when:

1. ✅ You can see 20+ questions at `/api/questions`
2. ✅ Start Interview button is clickable
3. ✅ Questions appear in interview room
4. ✅ Can submit answers
5. ✅ Can complete interview
6. ✅ See results with AI feedback
7. ✅ Analytics data updates

---

## 📱 OPTIONAL: Seed Interview Slots

If you want pre-scheduled slots:

```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/interview-slots/seed" -Method POST
```

This creates 5 sample slots for different topics.

---

## 🚀 NEXT: What to Do After Testing

1. **Add More Questions:**
   - Use POST `/api/questions` endpoint
   - Create your own question bank

2. **Customize Difficulty:**
   - Adjust in `QuestionBankService.java`
   - Add more categories

3. **Enhance UI:**
   - Update `InterviewRoom.jsx`
   - Create results dashboard
   - Add charts with Recharts

4. **Get Judge0 API Key:**
   - Sign up at RapidAPI
   - Add to `application.properties`
   - Enable real code execution

5. **Add OpenAI:**
   - Get API key from OpenAI
   - Enable AI feedback
   - Get smarter evaluations

---

## 💡 PRO TIPS

- **Save Database State:**
  ```bash
  pg_dump interview_coaching > backup.sql
  ```

- **Reset Data:**
  ```sql
  DELETE FROM interview_responses;
  DELETE FROM interview_questions;
  DELETE FROM interviews;
  DELETE FROM user_skills_analytics;
  ```

- **Test Without Login:**
  - Security is disabled for testing
  - Works with mock user data

- **Check Logs:**
  - Backend logs show all queries
  - Frontend console shows API calls

---

## 📞 READY FOR MORE?

Tell me what you want to do next:

1. 🎨 Build analytics dashboard
2. 🤖 Integrate real AI feedback
3. 💻 Enable real code execution
4. 📊 Add more question types
5. 🎯 Create admin panel
6. 📱 Make mobile responsive
7. 🚀 Deploy to production

**Let's build something amazing! 🎉**

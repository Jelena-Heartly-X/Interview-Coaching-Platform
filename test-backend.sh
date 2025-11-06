#!/bin/bash

echo "🔍 Testing Mock Test Backend..."
echo ""

echo "1️⃣ Testing Health Check..."
curl -s http://localhost:8080/api/mocktest/tests/health
echo ""
echo ""

echo "2️⃣ Testing Data Count..."
curl -s http://localhost:8080/api/mocktest/tests/debug/count | python -m json.tool 2>/dev/null || curl -s http://localhost:8080/api/mocktest/tests/debug/count
echo ""
echo ""

echo "3️⃣ Testing Active Tests..."
curl -s http://localhost:8080/api/mocktest/tests/active | python -m json.tool 2>/dev/null | head -20 || echo "Error fetching tests"
echo ""
echo ""

echo "4️⃣ Testing Active Courses..."
curl -s http://localhost:8080/api/mocktest/tests/courses/active | python -m json.tool 2>/dev/null | head -20 || echo "Error fetching courses"
echo ""
echo ""

echo "✅ Test Complete!"
echo ""
echo "If you see data above, backend is working!"
echo "If you see errors or empty arrays, check backend logs."

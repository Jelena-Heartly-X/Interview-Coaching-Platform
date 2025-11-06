-- Reset all booked slots to available
UPDATE interview_slots 
SET booked = false, 
    booked_by = NULL, 
    status = 'AVAILABLE'
WHERE booked = true;

-- Check slots after reset
SELECT id, title, topic, difficulty_level, booked, status, booked_by 
FROM interview_slots 
ORDER BY id;

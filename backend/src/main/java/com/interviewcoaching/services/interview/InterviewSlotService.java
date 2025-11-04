package com.interviewcoaching.services.interview;

import com.interviewcoaching.models.interview.InterviewSlot;
import com.interviewcoaching.repositories.interview.InterviewSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InterviewSlotService {
    
    @Autowired
    private InterviewSlotRepository slotRepository;
    
    /**
     * Create a new interview slot
     */
    public InterviewSlot createSlot(InterviewSlot slot) {
        slot.setCreatedAt(LocalDateTime.now());
        slot.setUpdatedAt(LocalDateTime.now());
        slot.setStatus("AVAILABLE");
        return slotRepository.save(slot);
    }
    
    /**
     * Get all available slots
     */
    public List<InterviewSlot> getAvailableSlots() {
        return slotRepository.findByStatus("AVAILABLE");
    }
    
    /**
     * Get upcoming available slots
     */
    public List<InterviewSlot> getUpcomingSlots() {
        return slotRepository.findUpcomingAvailableSlots(LocalDateTime.now());
    }
    
    /**
     * Get slots by topic
     */
    public List<InterviewSlot> getSlotsByTopic(String topic) {
        return slotRepository.findByTopicOrderByScheduledDateTimeAsc(topic);
    }
    
    /**
     * Get slots by topic and difficulty
     */
    public List<InterviewSlot> getSlotsByTopicAndDifficulty(String topic, String difficulty) {
        return slotRepository.findByTopicAndDifficultyLevel(topic, difficulty);
    }
    
    /**
     * Get slot by ID
     */
    public Optional<InterviewSlot> getSlotById(Long id) {
        return slotRepository.findById(id);
    }
    
    /**
     * Update slot status
     */
    public InterviewSlot updateSlotStatus(Long slotId, String status) {
        Optional<InterviewSlot> slotOpt = slotRepository.findById(slotId);
        if (slotOpt.isPresent()) {
            InterviewSlot slot = slotOpt.get();
            slot.setStatus(status);
            slot.setUpdatedAt(LocalDateTime.now());
            return slotRepository.save(slot);
        }
        throw new RuntimeException("Interview slot not found with ID: " + slotId);
    }
    
    /**
     * Book a slot for an interview
     */
    public InterviewSlot bookSlot(Long slotId) {
        return updateSlotStatus(slotId, "BOOKED");
    }
    
    /**
     * Mark slot as completed
     */
    public InterviewSlot completeSlot(Long slotId) {
        return updateSlotStatus(slotId, "COMPLETED");
    }
    
    /**
     * Cancel a slot
     */
    public InterviewSlot cancelSlot(Long slotId) {
        return updateSlotStatus(slotId, "CANCELLED");
    }
    
    /**
     * Get slots created by a mentor
     */
    public List<InterviewSlot> getSlotsByMentor(Long mentorId) {
        return slotRepository.findByMentorId(mentorId);
    }
    
    /**
     * Delete a slot
     */
    public void deleteSlot(Long slotId) {
        slotRepository.deleteById(slotId);
    }

    

}


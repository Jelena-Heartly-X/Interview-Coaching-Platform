package com.interviewcoaching.controllers.interview;

import com.interviewcoaching.dto.interview.InterviewSlotRequest;
import com.interviewcoaching.models.interview.InterviewSlot;
import com.interviewcoaching.services.interview.InterviewSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interview-slots")
@CrossOrigin(origins = "http://localhost:3000")
public class InterviewSlotController {
    
    @Autowired
    private InterviewSlotService slotService;
    
    /**
     * Get all available slots
     */
    @GetMapping("/available")
    public ResponseEntity<?> getAvailableSlots() {
        List<InterviewSlot> slots = slotService.getAvailableSlots();
        return ResponseEntity.ok(slots);
    }
    
    /**
     * Get upcoming slots
     */
    @GetMapping("/upcoming")
    public ResponseEntity<?> getUpcomingSlots() {
        List<InterviewSlot> slots = slotService.getUpcomingSlots();
        return ResponseEntity.ok(slots);
    }
    
    /**
     * Get slots by topic
     */
    @GetMapping("/topic/{topic}")
    public ResponseEntity<?> getSlotsByTopic(@PathVariable String topic) {
        List<InterviewSlot> slots = slotService.getSlotsByTopic(topic);
        return ResponseEntity.ok(slots);
    }
    
    /**
     * Get slots by topic and difficulty
     */
    @GetMapping("/filter")
    public ResponseEntity<?> getSlotsByFilter(
            @RequestParam String topic,
            @RequestParam String difficulty) {
        List<InterviewSlot> slots = slotService.getSlotsByTopicAndDifficulty(topic, difficulty);
        return ResponseEntity.ok(slots);
    }
    
    /**
     * Get slot by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getSlot(@PathVariable Long id) {
        try {
            InterviewSlot slot = slotService.getSlotById(id)
                .orElseThrow(() -> new RuntimeException("Slot not found"));
            return ResponseEntity.ok(slot);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    /**
     * Create a new interview slot
     */
    @PostMapping
    public ResponseEntity<?> createSlot(@Valid @RequestBody InterviewSlotRequest request) {
        try {
            InterviewSlot slot = new InterviewSlot();
            slot.setTitle(request.getTitle());
            slot.setDescription(request.getDescription());
            slot.setTopic(request.getTopic());
            slot.setDifficultyLevel(request.getDifficultyLevel());
            slot.setDurationMinutes(request.getDurationMinutes());
            slot.setScheduledDateTime(request.getScheduledDateTime());
            slot.setMaxParticipants(request.getMaxParticipants());
            
            InterviewSlot savedSlot = slotService.createSlot(slot);
            
            Map<String, Object> response = new HashMap<>();
            response.put("slot", savedSlot);
            response.put("message", "Interview slot created successfully");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    /**
     * Book a slot
     */
    @PutMapping("/{id}/book")
    public ResponseEntity<?> bookSlot(@PathVariable Long id) {
        try {
            InterviewSlot slot = slotService.bookSlot(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("slot", slot);
            response.put("message", "Slot booked successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    /**
     * Cancel a slot
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelSlot(@PathVariable Long id) {
        try {
            InterviewSlot slot = slotService.cancelSlot(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("slot", slot);
            response.put("message", "Slot cancelled successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    /**
     * Delete a slot
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSlot(@PathVariable Long id) {
        try {
            slotService.deleteSlot(id);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Slot deleted successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}

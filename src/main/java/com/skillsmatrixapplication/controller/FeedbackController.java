package com.skillsmatrixapplication.controller;

import com.skillsmatrixapplication.persistence.entity.Feedback;
import com.skillsmatrixapplication.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/currentEmployee")
    public ResponseEntity<List<Feedback>> getCurrentEmployeeFeedback() {
        List<Feedback> feedbackList = feedbackService.getCurrentEmployeeFeedback();
        return ResponseEntity.ok(feedbackList);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Feedback>> getFeedbackByEmployee(@PathVariable Long employeeId) {
        List<Feedback> feedbackList = feedbackService.getFeedbackByEmployeeId(employeeId);
        return ResponseEntity.ok(feedbackList);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Feedback>> getFeedbackByOwner(@PathVariable Long ownerId) {
        List<Feedback> feedbackList = feedbackService.getFeedbackByOwnerId(ownerId);
        return ResponseEntity.ok(feedbackList);
    }

    @PostMapping
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback) {
        Feedback createdFeedback = feedbackService.createFeedback(feedback);
        return ResponseEntity.ok(createdFeedback);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable Long id, @RequestBody Feedback feedback) {
        Optional<Feedback> updatedFeedback = feedbackService.updateFeedback(id, feedback);
        return updatedFeedback.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }
}

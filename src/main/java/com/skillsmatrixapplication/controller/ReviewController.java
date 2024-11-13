package com.skillsmatrixapplication.controller;

import com.skillsmatrixapplication.dto.CreateReviewRequest;
import com.skillsmatrixapplication.persistence.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.skillsmatrixapplication.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<Review>> getReviews(@RequestParam Long ownerId) {
        List<Review> reviews = reviewService.getReviewsByOwnerId(ownerId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/currentOwner")
    public ResponseEntity<List<Review>> getCurrentOwnersEmployeeReviews() {
        List<Review> reviews = reviewService.getCurrentOwnersEmployeeReviews();
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/createReviewRequest")
    public ResponseEntity<Void> createReviewRequest(@RequestBody CreateReviewRequest createReviewRequest) {
        reviewService.createReviewRequest(createReviewRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{reviewId}/approve")
    public ResponseEntity<Void> approveReview(@PathVariable Long reviewId) {
        reviewService.approveReview(reviewId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{reviewId}/reject")
    public ResponseEntity<Void> rejectReview(@PathVariable Long reviewId) {
        reviewService.rejectReview(reviewId);
        return ResponseEntity.ok().build();
    }
}

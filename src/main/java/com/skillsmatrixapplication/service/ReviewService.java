package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.enums.ReviewStatus;
import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.persistence.entity.Review;
import com.skillsmatrixapplication.persistence.repository.EmployeeRepository;
import com.skillsmatrixapplication.persistence.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, EmployeeRepository employeeRepository) {
        this.reviewRepository = reviewRepository;
        this.employeeRepository = employeeRepository;
    }

    public void createReviewRequest() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmployeeEmail = authentication.getName();

        Employee employee = employeeRepository.findByEmail(currentEmployeeEmail)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Review review = new Review();
        review.setEmployee(employee);
        review.setReviewDate(LocalDate.now());
        review.setStatus(ReviewStatus.NEW);

        reviewRepository.save(review);
    }

    public List<Review> getReviewsByOwnerId(Long ownerId) {
        List<Employee> employees = employeeRepository.findEmployeesByOwnerId(ownerId);
        List<Review> reviews = new ArrayList<>();

        for (Employee employee : employees) {
            reviews.addAll(reviewRepository.findByEmployeeId(employee.getId()));
        }

        return reviews;
    }

    public List<Review> getCurrentOwnersEmployeeReviews() {
        String currentOwnerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee currentOwner = employeeRepository.findByEmail(currentOwnerEmail)
                .orElseThrow(() -> new RuntimeException("Current owner not found"));

        return getReviewsByOwnerId(currentOwner.getId());
    }

    public void approveReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        review.setStatus(ReviewStatus.APPROVED);
        reviewRepository.save(review);
    }

    public void rejectReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        review.setStatus(ReviewStatus.REJECTED);
        reviewRepository.save(review);
    }

}

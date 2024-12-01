package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.dto.CreateReviewRequest;
import com.skillsmatrixapplication.dto.EmployeeResponse;
import com.skillsmatrixapplication.dto.ReviewResponse;
import com.skillsmatrixapplication.enums.CareerLevel;
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
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, EmployeeRepository employeeRepository) {
        this.reviewRepository = reviewRepository;
        this.employeeRepository = employeeRepository;
    }

    public void createReviewRequest(CreateReviewRequest createReviewRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmployeeEmail = authentication.getName();

        Employee employee = employeeRepository.findByEmail(currentEmployeeEmail)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Review review = new Review();
        review.setEmployee(employee);
        review.setOwner(employee.getOwners().stream().findAny().orElse(null));
        review.setReviewDate(LocalDate.now());
        review.setStatus(ReviewStatus.NEW);

        review.setCareerLevel(CareerLevel.valueOf(createReviewRequest.getCareerLevel()));
        review.setEvaluatedCareerLevel(CareerLevel.valueOf(createReviewRequest.getEvaluatedCareerLevel()));

        review.setReviewText(createReviewRequest.getReviewText());
        review.setScore(createReviewRequest.getScore());

        reviewRepository.save(review);
    }

    public List<ReviewResponse> getReviewsByOwnerId(Long ownerId) {
        List<Employee> employees = employeeRepository.findEmployeesManagedByOwner(ownerId);

        return employees.stream()
                .flatMap(employee -> reviewRepository.findByEmployeeId(employee.getId()).stream()
                        .map(review -> new ReviewResponse(
                                review.getId(),
                                review.getReviewText(),
                                review.getScore(),
                                review.getCareerLevel(),
                                review.getEvaluatedCareerLevel(),
                                EmployeeResponse.of(review.getEmployee()),
                                EmployeeResponse.of(review.getOwner()),
                                review.getReviewDate(),
                                review.getStatus()
                        ))
                )
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> getCurrentOwnersEmployeeReviews() {
        String currentOwnerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee currentOwner = employeeRepository.findByEmail(currentOwnerEmail)
                .orElseThrow(() -> new RuntimeException("Current owner not found"));

        return getReviewsByOwnerId(currentOwner.getId());
    }

    public void approveReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));
        review.setStatus(ReviewStatus.APPROVED);
        Employee employee = review.getEmployee();
        if (employee != null) {
            employee.setCareerLevel(review.getEvaluatedCareerLevel());
        } else {
            throw new RuntimeException("Employee not associated with the review");
        }
        reviewRepository.save(review);
        employeeRepository.save(employee);
    }


    public void rejectReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow();
        review.setStatus(ReviewStatus.REJECTED);
        reviewRepository.save(review);
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}

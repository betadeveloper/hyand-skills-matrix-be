package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.dto.CreateFeedbackRequest;
import com.skillsmatrixapplication.dto.GoalResponse;
import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.persistence.entity.Feedback;
import com.skillsmatrixapplication.persistence.repository.EmployeeRepository;
import com.skillsmatrixapplication.persistence.repository.FeedbackRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final EmployeeRepository employeeRepository;


    public FeedbackService(FeedbackRepository feedbackRepository, EmployeeRepository employeeRepository) {
        this.feedbackRepository = feedbackRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Feedback> getCurrentEmployeeFeedback() {
        String currentEmployeeEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return feedbackRepository.findByEmployeeEmail(currentEmployeeEmail);
    }

    public List<Feedback> getFeedbackByEmployeeId(Long employeeId) {
        return feedbackRepository.findByEmployeeId(employeeId);
    }

    public List<Feedback> getFeedbackByOwnerId(Long ownerId) {
        return feedbackRepository.findByOwnerId(ownerId);
    }

    @Transactional
    public Feedback createFeedback(CreateFeedbackRequest feedbackDTO) {
        Employee employee = employeeRepository.findById(feedbackDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        Employee owner = employeeRepository.findById(feedbackDTO.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        Feedback feedback = new Feedback();
        feedback.setFeedbackText(feedbackDTO.getFeedbackText());
        feedback.setEmployee(employee);
        feedback.setOwner(owner);

        return feedbackRepository.save(feedback);
    }

    public Optional<Feedback> updateFeedback(Long id, Feedback updatedFeedback) {
        return feedbackRepository.findById(id).map(existingFeedback -> {
            existingFeedback.setFeedbackText(updatedFeedback.getFeedbackText());
            return feedbackRepository.save(existingFeedback);
        });
    }

    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }
}

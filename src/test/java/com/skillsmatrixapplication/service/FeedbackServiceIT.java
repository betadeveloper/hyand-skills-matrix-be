package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.dto.CreateFeedbackRequest;
import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.persistence.entity.Feedback;
import com.skillsmatrixapplication.persistence.repository.EmployeeRepository;
import com.skillsmatrixapplication.persistence.repository.FeedbackRepository;
import com.skillsmatrixapplication.service.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FeedbackServiceIT {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private FeedbackService feedbackService;

    @BeforeEach
    void setUp() {
        feedbackRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    void testGetFeedbackByEmployeeId() {
        Employee employee = new Employee();
        employee.setEmail("employee@example.com");
        employee = employeeRepository.save(employee);

        Feedback feedback = new Feedback();
        feedback.setEmployee(employee);
        feedback.setFeedbackText("Great teamwork!");
        feedbackRepository.save(feedback);

        List<Feedback> feedbacks = feedbackService.getFeedbackByEmployeeId(employee.getId());

        assertEquals(1, feedbacks.size());
        assertEquals("Great teamwork!", feedbacks.get(0).getFeedbackText());
    }

    @Test
    void testCreateFeedback() {
        Employee employee = new Employee();
        employee.setEmail("employee@example.com");
        employee = employeeRepository.save(employee);

        Employee owner = new Employee();
        owner.setEmail("owner@example.com");
        owner = employeeRepository.save(owner);

        CreateFeedbackRequest request = new CreateFeedbackRequest();
        request.setEmployeeId(employee.getId());
        request.setOwnerId(owner.getId());
        request.setFeedbackText("Excellent performance!");

        Feedback feedback = feedbackService.createFeedback(request);

        assertNotNull(feedback.getId());
        assertEquals("Excellent performance!", feedback.getFeedbackText());
    }

    @Test
    void testUpdateFeedback() {
        Employee employee = new Employee();
        employee = employeeRepository.save(employee);

        Feedback feedback = new Feedback();
        feedback.setEmployee(employee);
        feedback.setFeedbackText("Initial Feedback");
        feedback = feedbackRepository.save(feedback);

        Feedback updatedDetails = new Feedback();
        updatedDetails.setFeedbackText("Updated Feedback");

        Optional<Feedback> updatedFeedback = feedbackService.updateFeedback(feedback.getId(), updatedDetails);

        assertTrue(updatedFeedback.isPresent());
        assertEquals("Updated Feedback", updatedFeedback.get().getFeedbackText());
    }

    @Test
    void testDeleteFeedback() {
        Feedback feedback = new Feedback();
        feedback.setFeedbackText("To be deleted");
        feedback = feedbackRepository.save(feedback);

        feedbackService.deleteFeedback(feedback.getId());

        assertFalse(feedbackRepository.existsById(feedback.getId()));
    }
}

package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.dto.CreateFeedbackRequest;
import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.persistence.entity.Feedback;
import com.skillsmatrixapplication.persistence.repository.EmployeeRepository;
import com.skillsmatrixapplication.persistence.repository.FeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedbackServiceTest {

    private FeedbackRepository feedbackRepository;
    private EmployeeRepository employeeRepository;
    private FeedbackService feedbackService;

    @BeforeEach
    void setUp() {
        feedbackRepository = mock(FeedbackRepository.class);
        employeeRepository = mock(EmployeeRepository.class);
        feedbackService = new FeedbackService(feedbackRepository, employeeRepository);
    }

    @Test
    void testGetCurrentEmployeeFeedback() {
        String currentEmail = "test@example.com";
        Feedback feedback = new Feedback();
        feedback.setFeedbackText("Great work!");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(currentEmail);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(feedbackRepository.findByEmployeeEmail(currentEmail)).thenReturn(List.of(feedback));

        List<Feedback> feedbacks = feedbackService.getCurrentEmployeeFeedback();

        assertEquals(1, feedbacks.size());
        assertEquals("Great work!", feedbacks.get(0).getFeedbackText());
        verify(feedbackRepository, times(1)).findByEmployeeEmail(currentEmail);
    }

    @Test
    void testCreateFeedback() {
        CreateFeedbackRequest request = new CreateFeedbackRequest();
        request.setEmployeeId(1L);
        request.setOwnerId(2L);
        request.setFeedbackText("Excellent collaboration!");

        Employee employee = new Employee();
        employee.setId(1L);
        Employee owner = new Employee();
        owner.setId(2L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(owner));
        when(feedbackRepository.save(any(Feedback.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Feedback createdFeedback = feedbackService.createFeedback(request);

        assertEquals("Excellent collaboration!", createdFeedback.getFeedbackText());
        assertEquals(employee, createdFeedback.getEmployee());
        assertEquals(owner, createdFeedback.getOwner());
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void testUpdateFeedback() {
        Long feedbackId = 1L;
        Feedback existingFeedback = new Feedback();
        existingFeedback.setFeedbackText("Initial Feedback");
        Feedback updatedFeedback = new Feedback();
        updatedFeedback.setFeedbackText("Updated Feedback");

        when(feedbackRepository.findById(feedbackId)).thenReturn(Optional.of(existingFeedback));
        when(feedbackRepository.save(existingFeedback)).thenReturn(existingFeedback);

        Optional<Feedback> result = feedbackService.updateFeedback(feedbackId, updatedFeedback);

        assertTrue(result.isPresent());
        assertEquals("Updated Feedback", result.get().getFeedbackText());
        verify(feedbackRepository, times(1)).save(existingFeedback);
    }

    @Test
    void testDeleteFeedback() {
        Long feedbackId = 1L;
        doNothing().when(feedbackRepository).deleteById(feedbackId);

        feedbackService.deleteFeedback(feedbackId);

        verify(feedbackRepository, times(1)).deleteById(feedbackId);
    }
}

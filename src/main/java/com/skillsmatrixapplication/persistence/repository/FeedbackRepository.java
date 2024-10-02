package com.skillsmatrixapplication.persistence.repository;

import com.skillsmatrixapplication.persistence.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByEmployeeEmail(String email);

    List<Feedback> findByEmployeeId(Long employeeId);

    List<Feedback> findByOwnerId(Long ownerId);
}

package com.skillsmatrixapplication.persistence.repository;

import com.skillsmatrixapplication.persistence.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByEmployeeId(Long employeeId);
}

package com.skillsmatrixapplication.persistence.repository;

import com.skillsmatrixapplication.persistence.entity.CareerPath;
import com.skillsmatrixapplication.persistence.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareerPathRepository extends JpaRepository<CareerPath, Long> {
    @Query("SELECT e FROM Employee e WHERE e.careerPath.id = :id")
    ResponseEntity<List<Employee>> getCareerPathEmployees(@Param("id") Long id);
}

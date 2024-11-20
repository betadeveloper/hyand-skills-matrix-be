package com.skillsmatrixapplication.persistence.repository;

import com.skillsmatrixapplication.persistence.entity.CareerPath;
import com.skillsmatrixapplication.persistence.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);

    @Query("SELECT e FROM Employee e JOIN e.owners o WHERE o.id = :ownerId")
    List<Employee> findEmployeesManagedByOwner(@Param("ownerId") Long ownerId);

    @Query("SELECT cp FROM CareerPath cp JOIN cp.employees e WHERE e.email = :email")
    CareerPath getCurrentEmployeeCareerPath(@Param("email") String email);
}
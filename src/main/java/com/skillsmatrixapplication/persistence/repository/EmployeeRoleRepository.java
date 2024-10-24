package com.skillsmatrixapplication.persistence.repository;

import com.skillsmatrixapplication.persistence.entity.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRoleRepository extends JpaRepository<EmployeeRole, Long> {
    EmployeeRole findByEmployeeId(Long employeeId);
}

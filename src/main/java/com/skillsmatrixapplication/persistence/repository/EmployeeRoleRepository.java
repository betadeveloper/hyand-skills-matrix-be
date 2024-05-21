package com.skillsmatrixapplication.persistence.repository;

import com.skillsmatrixapplication.persistence.entity.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRoleRepository extends JpaRepository<EmployeeRole, Long> {
}

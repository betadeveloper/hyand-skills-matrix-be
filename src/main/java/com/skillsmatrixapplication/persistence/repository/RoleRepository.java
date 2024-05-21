package com.skillsmatrixapplication.persistence.repository;

import com.skillsmatrixapplication.persistence.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}

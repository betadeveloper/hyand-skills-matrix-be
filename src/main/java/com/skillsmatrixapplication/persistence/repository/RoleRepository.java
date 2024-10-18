package com.skillsmatrixapplication.persistence.repository;

import com.skillsmatrixapplication.enums.RoleEnum;
import com.skillsmatrixapplication.persistence.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(RoleEnum role);
}

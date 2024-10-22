package com.skillsmatrixapplication.persistence.repository;

import com.skillsmatrixapplication.persistence.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByCareerPathId(Long id);
}

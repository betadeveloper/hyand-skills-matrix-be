package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.persistence.entity.Skill;
import com.skillsmatrixapplication.persistence.repository.SkillRepository;
import com.skillsmatrixapplication.service.SkillsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SkillsServiceIT {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillsService skillsService;

    @BeforeEach
    void setUp() {
        skillRepository.deleteAll();
    }

    @Test
    void testGetSkillsByCareerPathId() {
        Long careerPathId = 1L;

        Skill skill = new Skill();
        skill.setName("Java");
        skill.setCareerPathId(careerPathId);
        skillRepository.save(skill);

        List<Skill> skills = skillsService.getSkillsByCareerPathId(careerPathId);

        assertEquals(1, skills.size());
        assertEquals("Java", skills.get(0).getName());
    }

    @Test
    void testCreateSkill() {
        Long careerPathId = 2L;
        Skill skill = new Skill();
        skill.setName("Python");
        skill.setCareerPathId(careerPathId);

        Skill createdSkill = skillsService.createSkill(careerPathId, skill);

        assertNotNull(createdSkill.getId());
        assertEquals("Python", createdSkill.getName());
    }

    @Test
    void testUpdateSkill() {
        Skill skill = new Skill();
        skill.setName("SQL");
        skill = skillRepository.save(skill);

        Skill updatedSkillDetails = new Skill();
        updatedSkillDetails.setName("Updated SQL");

        Skill updatedSkill = skillsService.updateSkill(skill.getId(), updatedSkillDetails);

        assertEquals("Updated SQL", updatedSkill.getName());
    }

    @Test
    void testUpdateSkill_NotFound() {
        Long nonExistentSkillId = 999L;

        RuntimeException exception = assertThrows(RuntimeException.class, () -> skillsService.updateSkill(nonExistentSkillId, new Skill()));
        assertEquals("No value present", exception.getMessage());
    }

    @Test
    void testDeleteSkill() {
        Skill skill = new Skill();
        skill.setName("HTML");
        skill = skillRepository.save(skill);

        skillsService.deleteSkill(skill.getId());

        assertFalse(skillRepository.existsById(skill.getId()));
    }
}

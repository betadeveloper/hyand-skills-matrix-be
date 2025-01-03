package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.persistence.entity.Skill;
import com.skillsmatrixapplication.persistence.repository.SkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SkillsServiceTest {

    private SkillRepository skillRepository;
    private SkillsService skillsService;

    @BeforeEach
    void setUp() {
        skillRepository = Mockito.mock(SkillRepository.class);
        skillsService = new SkillsService(skillRepository, null);
    }

    @Test
    void testGetSkillsByCareerPathId() {
        Long careerPathId = 1L;
        Skill skill = new Skill();
        skill.setCareerPathId(careerPathId);
        when(skillRepository.findByCareerPathId(careerPathId)).thenReturn(List.of(skill));

        List<Skill> skills = skillsService.getSkillsByCareerPathId(careerPathId);

        assertNotNull(skills);
        assertEquals(1, skills.size());
        assertEquals(careerPathId, skills.get(0).getCareerPathId());
        verify(skillRepository, times(1)).findByCareerPathId(careerPathId);
    }

    @Test
    void testCreateSkill() {
        Long careerPathId = 1L;
        Skill skill = new Skill();
        skill.setName("Test Skill");
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);

        Skill createdSkill = skillsService.createSkill(careerPathId, skill);

        assertNotNull(createdSkill);
        assertEquals("Test Skill", createdSkill.getName());
        verify(skillRepository, times(1)).save(skill);
    }

    @Test
    void testUpdateSkill() {
        Long skillId = 1L;
        Skill existingSkill = new Skill();
        existingSkill.setId(skillId);

        Skill updatedDetails = new Skill();
        updatedDetails.setName("Updated Skill");

        when(skillRepository.findById(skillId)).thenReturn(Optional.of(existingSkill));
        when(skillRepository.save(existingSkill)).thenReturn(existingSkill);

        Skill updatedSkill = skillsService.updateSkill(skillId, updatedDetails);

        assertEquals("Updated Skill", existingSkill.getName());
        verify(skillRepository, times(1)).findById(skillId);
        verify(skillRepository, times(1)).save(existingSkill);
    }

    @Test
    void testUpdateSkill_NotFound() {
        Long skillId = 1L;
        when(skillRepository.findById(skillId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> skillsService.updateSkill(skillId, new Skill()));
        verify(skillRepository, times(1)).findById(skillId);
    }

    @Test
    void testDeleteSkill() {
        Long skillId = 1L;
        doNothing().when(skillRepository).deleteById(skillId);

        skillsService.deleteSkill(skillId);

        verify(skillRepository, times(1)).deleteById(skillId);
    }
}

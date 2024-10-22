package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.exception.ResourceNotFoundException;
import com.skillsmatrixapplication.persistence.entity.Skill;
import com.skillsmatrixapplication.persistence.entity.CareerPath;
import com.skillsmatrixapplication.persistence.repository.SkillRepository;
import com.skillsmatrixapplication.persistence.repository.CareerPathRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillsService {

    private final SkillRepository skillRepository;
    private final CareerPathRepository careerPathRepository;

    public SkillsService(SkillRepository skillRepository, CareerPathRepository careerPathRepository) {
        this.skillRepository = skillRepository;
        this.careerPathRepository = careerPathRepository;
    }

    public List<Skill> getSkillsByCareerPathId(Long careerPathId) {
        return skillRepository.findByCareerPathId(careerPathId);
    }

    public Skill createSkill(Long careerPathId, Skill skill) {
        CareerPath careerPath = careerPathRepository.findById(careerPathId)
                .orElseThrow(() -> new ResourceNotFoundException("CareerPath not found with id " + careerPathId));

        skill.setCareerPath(careerPath);
        System.out.println("Creating skill: " + skill);

        return skillRepository.save(skill);
    }

    public Skill updateSkill(Long id, Skill newSkillDetails) {
        Skill existingSkill = skillRepository.findById(id).orElseThrow();
        existingSkill.setName(newSkillDetails.getName());
        existingSkill.setDescription(newSkillDetails.getDescription());
        existingSkill.setWeight(newSkillDetails.getWeight());
        existingSkill.setProficiency(newSkillDetails.getProficiency());
        return skillRepository.save(existingSkill);
    }

    public void deleteSkill(Long id) {
        skillRepository.deleteById(id);
    }
}
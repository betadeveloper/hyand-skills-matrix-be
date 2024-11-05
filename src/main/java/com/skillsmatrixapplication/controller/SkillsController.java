package com.skillsmatrixapplication.controller;

import com.skillsmatrixapplication.service.SkillsService;
import com.skillsmatrixapplication.persistence.entity.Skill;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SkillsController {

    private final SkillsService skillsService;

    public SkillsController(SkillsService skillsService) {
        this.skillsService = skillsService;
    }

    @GetMapping("/skills/{careerPathId}")
    public ResponseEntity<List<Skill>> getSkillsByCareerPathId(@PathVariable Long careerPathId) {
        List<Skill> skills = skillsService.getSkillsByCareerPathId(careerPathId);
        return ResponseEntity.ok(skills);
    }

    @PostMapping("/skills/{careerPathId}")
    public ResponseEntity<Skill> createSkill(@PathVariable Long careerPathId, @RequestBody Skill skill) {
        Skill createdSkill = skillsService.createSkill(careerPathId, skill);
        return ResponseEntity.ok(createdSkill);
    }

    @PutMapping("/skills/{id}")
    public ResponseEntity<Skill> updateSkill(@PathVariable Long id, @RequestBody Skill newSkillDetails) {
        Skill updatedSkill = skillsService.updateSkill(id, newSkillDetails);
        return ResponseEntity.ok(updatedSkill);
    }

    @DeleteMapping("/skills/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        skillsService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }
}

package com.skillsmatrixapplication.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "skill")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Double weight;

    @Column
    private Double proficiency;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "career_path_id")
    @JsonBackReference(value = "careerPath-skill")
    private CareerPath careerPath;

    @OneToMany(mappedBy = "skill", fetch = FetchType.EAGER)
    @JsonManagedReference(value = "skill-employeeSkill")
    private List<EmployeeSkill> employeeSkills;
}

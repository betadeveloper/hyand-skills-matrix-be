package com.skillsmatrixapplication.persistence.entity;

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
    private CareerPath careerPath;

    @OneToMany(mappedBy = "skill", fetch = FetchType.EAGER)
    private List<EmployeeSkill> employeeSkills;
}
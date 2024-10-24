package com.skillsmatrixapplication.persistence.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "career_path")
public class CareerPath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Double score;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Skill> skills;

    @OneToMany(fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Employee> employees;
}

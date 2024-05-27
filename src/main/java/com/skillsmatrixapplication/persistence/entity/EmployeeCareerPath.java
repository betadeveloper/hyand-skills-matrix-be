//package com.skillsmatrixapplication.persistence.entity;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//@Entity
//public class EmployeeCareerPath {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "career_path_id")
//    private CareerPath careerPath;
//
//    @Column
//    private Integer careerPathScore;
//
//    @ManyToOne
//    @JoinColumn(name = "employee_id")
//    private Employee employee;
//}

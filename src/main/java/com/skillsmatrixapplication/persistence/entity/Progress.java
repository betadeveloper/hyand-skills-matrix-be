package com.skillsmatrixapplication.persistence.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long goalId;

    @Column
    private LocalDate date;

    @Column
    private String progressMade;

    @Column
    private String notes;
}
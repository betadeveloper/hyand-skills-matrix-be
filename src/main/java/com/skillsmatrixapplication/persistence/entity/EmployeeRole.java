package com.skillsmatrixapplication.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public EmployeeRole(Employee employee, Role role) {
        this.employee = employee;
        this.role = role;
    }
}

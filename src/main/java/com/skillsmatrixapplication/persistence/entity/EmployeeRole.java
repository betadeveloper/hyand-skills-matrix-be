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

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "employee_id", nullable = false)
    @NonNull
    private Employee employee;


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    @NonNull
    private Role role;

    public EmployeeRole(final Employee employee, final Role role) {
        this.employee = employee;
        this.role = role;
    }
}

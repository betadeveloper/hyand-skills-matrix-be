package com.skillsmatrixapplication.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    @NonNull
    @JsonBackReference
    private Employee employee;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    @NonNull
    @JsonBackReference
    private Role role;

    public EmployeeRole(final Employee employee, final Role role) {
        this.employee = employee;
        this.role = role;
    }
}

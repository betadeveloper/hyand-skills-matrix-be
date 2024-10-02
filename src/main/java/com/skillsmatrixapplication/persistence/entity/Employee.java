package com.skillsmatrixapplication.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.skillsmatrixapplication.model.enums.CareerLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Table(name = "employees")
@Getter
@Setter
@Entity
public class Employee implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(unique = true, length = 100, nullable = false)
    private String email;

    @Column
    private String position;

    @Column
    @Enumerated(EnumType.STRING)
    private CareerLevel careerLevel;

    @Column
    private String department;

    @Lob
    @Column
    private byte[] profilePicture;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
    @JsonManagedReference(value = "employee-employeeRole")
    private List<EmployeeRole> employeeRoles = new ArrayList<>();

    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Feedback> feedbackEntries = new ArrayList<>();

    public List<Role> getRoles() {
        return employeeRoles
                .stream()
                .map(EmployeeRole::getRole)
                .toList();
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employee_owners",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "owner_id")
    )
    private Set<Employee> owners = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "career_path_id")
    @JsonBackReference(value = "careerPath-employee")
    private CareerPath careerPath;

    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER)
    @JsonManagedReference(value = "employee-employeeSkill")
    private List<EmployeeSkill> employeeSkills;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

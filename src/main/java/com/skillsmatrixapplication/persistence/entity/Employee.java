package com.skillsmatrixapplication.persistence.entity;

import com.fasterxml.jackson.annotation.*;
import com.skillsmatrixapplication.enums.RoleEnum;
import com.skillsmatrixapplication.model.enums.CareerLevel;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

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
    private List<EmployeeRole> employeeRoles = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employee_owners",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "owner_id")
    )
    private Set<Employee> owners = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_path_id")
    @JsonBackReference
    private CareerPath careerPath;

    @OneToMany(fetch = FetchType.EAGER)
    private List<EmployeeSkill> employeeSkills;

    @Column
    private String currentRole;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return employeeRoles.stream()
                .map(employeeRole -> new SimpleGrantedAuthority(employeeRole.getRole().getRole().name()))
                .toList();
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

    public List<RoleEnum> getRoles() {
        return employeeRoles.stream()
                .map(employeeRole -> employeeRole.getRole().getRole())
                .toList();
    }

    public void addRole(Role role) {
        EmployeeRole employeeRole = new EmployeeRole(this, role);
        this.employeeRoles.add(employeeRole);
    }
}

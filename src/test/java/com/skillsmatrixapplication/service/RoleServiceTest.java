package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.persistence.entity.EmployeeRole;
import com.skillsmatrixapplication.persistence.entity.Role;
import com.skillsmatrixapplication.persistence.repository.EmployeeRepository;
import com.skillsmatrixapplication.persistence.repository.EmployeeRoleRepository;
import com.skillsmatrixapplication.persistence.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EmployeeRoleRepository employeeRoleRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    void testCreateRole() {
        Role role = new Role();
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        Role result = roleService.createRole(role);

        assertEquals(role, result);
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void testAssignRoleToEmployee() {
        Long employeeId = 1L;
        Long roleId = 1L;
        Employee employee = new Employee();
        Role role = new Role();
        EmployeeRole employeeRole = new EmployeeRole(employee, role);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(employeeRoleRepository.save(any(EmployeeRole.class))).thenReturn(employeeRole);

        EmployeeRole result = roleService.assignRoleToEmployee(employeeId, roleId);

        assertEquals(employeeRole, result);
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(roleRepository, times(1)).findById(roleId);
        verify(employeeRoleRepository, times(1)).save(any(EmployeeRole.class));
    }
}
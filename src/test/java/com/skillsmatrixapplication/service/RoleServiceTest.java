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

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
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

    @Test
    void testGetAllRoles() {
        Role role1 = new Role();
        role1.setId(1L);
        Role role2 = new Role();
        role2.setId(2L);

        when(roleRepository.findAll()).thenReturn(List.of(role1, role2));

        List<Role> result = roleService.getAllRoles();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testDeleteRoleNotFound() {
        Long roleId = 1L;

        when(roleRepository.existsById(roleId)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            roleService.deleteRole(roleId);
        });

        assertEquals("Role not found", exception.getMessage());
        verify(roleRepository, times(1)).existsById(roleId);
        verify(roleRepository, times(0)).deleteById(roleId);
    }

    @Test
    void testAssignNonExistentRoleToEmployee() {
        Long employeeId = 1L;
        Long roleId = 1L;
        Employee employee = new Employee();
        employee.setId(employeeId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            roleService.assignRoleToEmployee(employeeId, roleId);
        });

        assertEquals("Role not found", exception.getMessage());
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(roleRepository, times(1)).findById(roleId);
        verify(employeeRoleRepository, times(0)).save(any(EmployeeRole.class));
    }
}
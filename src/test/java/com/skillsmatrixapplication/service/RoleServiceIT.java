package com.skillsmatrixapplication.service;

import com.skillsmatrixapplication.enums.RoleEnum;
import com.skillsmatrixapplication.persistence.entity.Employee;
import com.skillsmatrixapplication.persistence.entity.EmployeeRole;
import com.skillsmatrixapplication.persistence.entity.Role;
import com.skillsmatrixapplication.persistence.repository.EmployeeRepository;
import com.skillsmatrixapplication.persistence.repository.EmployeeRoleRepository;
import com.skillsmatrixapplication.persistence.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class RoleServiceIT {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeRoleRepository employeeRoleRepository;

    @BeforeEach
    void setup() {
        employeeRoleRepository.deleteAll();
        roleRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    void testCreateRole() {
        Role role = new Role();
        role.setRole(RoleEnum.ROLE_EMPLOYEE);
        Role createdRole = roleService.createRole(role);

        assertNotNull(createdRole.getId());
        assertEquals(RoleEnum.ROLE_EMPLOYEE, createdRole.getRole());
    }

    @Test
    void testAssignRoleToEmployee() {
        Employee employee = new Employee();
        employee.setEmail("john@gmail.com");
        employee.setFirstName("John");
        employee = employeeRepository.save(employee);

        Role role = new Role();
        role.setRole(RoleEnum.ROLE_OWNER);
        role = roleRepository.save(role);

        EmployeeRole assignedRole = roleService.assignRoleToEmployee(employee.getId(), role.getId());

        assertNotNull(assignedRole.getId());
        assertEquals(employee.getId(), assignedRole.getEmployee().getId());
        assertEquals(role.getId(), assignedRole.getRole().getId());
    }

    @Test
    void testGetAllRoles() {
        Role role1 = new Role();
        role1.setRole(RoleEnum.ROLE_ADMIN);
        roleRepository.save(role1);

        Role role2 = new Role();
        role2.setRole(RoleEnum.ROLE_EMPLOYEE);
        roleRepository.save(role2);

        List<Role> roles = roleService.getAllRoles();

        assertEquals(2, roles.size());
        assertTrue(roles.stream().anyMatch(r -> r.getRole().equals(RoleEnum.ROLE_ADMIN)));
        assertTrue(roles.stream().anyMatch(r -> r.getRole().equals(RoleEnum.ROLE_EMPLOYEE)));
    }

    @Test
    void testGetEmployeeRole() {
        Employee employee = new Employee();
        employee.setEmail("jane@gmail.com");
        employee.setFirstName("Jane");
        employee = employeeRepository.save(employee);

        Role role = new Role();
        role.setRole(RoleEnum.ROLE_OWNER);
        role = roleRepository.save(role);

        EmployeeRole employeeRole = new EmployeeRole(employee, role);
        employeeRoleRepository.save(employeeRole);

        EmployeeRole fetchedEmployeeRole = roleService.getEmployeeRole(employee.getId());

        assertNotNull(fetchedEmployeeRole);
        assertEquals(employee.getId(), fetchedEmployeeRole.getEmployee().getId());
        assertEquals(role.getId(), fetchedEmployeeRole.getRole().getId());
    }

    @Test
    void testDeleteRole() {
        Role role = new Role();
        role.setRole(RoleEnum.ROLE_EMPLOYEE);
        role = roleRepository.save(role);

        roleService.deleteRole(role.getId());

        assertFalse(roleRepository.existsById(role.getId()));
    }

    @Test
    void testDeleteRole_NotFound() {
        Long nonExistentRoleId = 999L;
        RuntimeException exception = assertThrows(RuntimeException.class, () -> roleService.deleteRole(nonExistentRoleId));
        assertEquals("Role not found", exception.getMessage());
    }

    @Test
    void testAssignRoleToEmployee_NotFoundEmployee() {
        Role role = new Role();
        role.setRole(RoleEnum.ROLE_EMPLOYEE);
        role = roleRepository.save(role);

        Role finalRole = role;
        RuntimeException exception = assertThrows(RuntimeException.class, () -> roleService.assignRoleToEmployee(999L, finalRole.getId()));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testAssignRoleToEmployee_NotFoundRole() {
        Employee employee = new Employee();
        employee.setEmail("emma@gmail.com");
        employee.setFirstName("Emma");
        employee = employeeRepository.save(employee);

        Employee finalEmployee = employee;
        RuntimeException exception = assertThrows(RuntimeException.class, () -> roleService.assignRoleToEmployee(finalEmployee.getId(), 999L));
        assertEquals("Role not found", exception.getMessage());
    }
}

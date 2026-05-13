package com.ems.employee;

import com.ems.department.Department;
import com.ems.department.DepartmentRepository;
import com.ems.employee.dto.EmployeeRequest;
import com.ems.employee.dto.EmployeeResponse;
import com.ems.employee.dto.EmployeeSummary;
import com.ems.user.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public EmployeeMapper(DepartmentRepository departmentRepository, UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    public EmployeeResponse toResponse(Employee employee, boolean includeSalary) {
        String deptName = null;
        Long deptId = employee.getDepartmentId();
        if (deptId != null) {
            deptName = departmentRepository.findById(deptId)
                    .map(Department::getName)
                    .orElse(null);
        }

        String role = null;
        Long userId = employee.getUserId();
        if (userId != null) {
            role = userRepository.findById(userId)
                    .map(u -> u.getRole().name())
                    .orElse(null);
        }

        return EmployeeResponse.builder()
                .id(employee.getId())
                .userId(employee.getUserId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .departmentId(employee.getDepartmentId())
                .departmentName(deptName)
                .position(employee.getPosition())
                .salary(includeSalary ? employee.getSalary() : null)
                .hireDate(employee.getHireDate())
                .status(employee.getStatus())
                .role(role)
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }

    public EmployeeSummary toSummary(Employee employee) {
        String deptName = null;
        Long deptId = employee.getDepartmentId();
        if (deptId != null) {
            deptName = departmentRepository.findById(deptId)
                    .map(Department::getName)
                    .orElse(null);
        }

        
        return EmployeeSummary.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .position(employee.getPosition())
                .departmentName(deptName)
                .status(employee.getStatus())
                .build();
    }

    public Employee toEntity(EmployeeRequest request) {
        return Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .departmentId(request.getDepartmentId())
                .position(request.getPosition())
                .salary(request.getSalary())
                .hireDate(request.getHireDate())
                .status(request.getStatus() != null ? request.getStatus() : "ACTIVE")
                .isDeleted(false)
                .build();
    }

    public void updateEntity(Employee employee, EmployeeRequest request) {
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDepartmentId(request.getDepartmentId());
        employee.setPosition(request.getPosition());
        if (request.getSalary() != null) {
            employee.setSalary(request.getSalary());
        }
        if (request.getHireDate() != null) {
            employee.setHireDate(request.getHireDate());
        }
        if (request.getStatus() != null) {
            employee.setStatus(request.getStatus());
        }
    }
}

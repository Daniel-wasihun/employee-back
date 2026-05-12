package com.ems.employee;

import com.ems.common.dto.PageResponse;
import com.ems.common.exception.ResourceNotFoundException;
import com.ems.employee.dto.EmployeeRequest;
import com.ems.employee.dto.EmployeeResponse;
import com.ems.user.Role;
import com.ems.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public PageResponse<EmployeeResponse> getAllEmployees(int page, int size, String sortBy, String direction, String search, User currentUser) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        Page<Employee> employees;

        if (search != null && !search.isBlank()) {
            employees = employeeRepository.searchEmployees(search, pageable);
        } else {
            employees = employeeRepository.findByIsDeletedFalse(pageable);
        }

        var content = employees.getContent().stream()
                .map(e -> employeeMapper.toResponse(e, isAdmin))
                .toList();

        return PageResponse.<EmployeeResponse>builder()
                .content(content)
                .page(employees.getNumber())
                .size(employees.getSize())
                .totalElements(employees.getTotalElements())
                .totalPages(employees.getTotalPages())
                .last(employees.isLast())
                .build();
    }

    public EmployeeResponse getEmployeeById(Long id, User currentUser) {
        Employee employee = employeeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        return employeeMapper.toResponse(employee, isAdmin);
    }

    public EmployeeResponse getMyProfile(User currentUser) {
        Employee employee = employeeRepository.findByUserIdAndIsDeletedFalse(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee profile not found for current user"));

        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        return employeeMapper.toResponse(employee, isAdmin);
    }

    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        Employee employee = employeeMapper.toEntity(request);
        employee = employeeRepository.save(employee);
        return employeeMapper.toResponse(employee, true);
    }

    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request, User currentUser) {
        Employee employee = employeeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        employeeMapper.updateEntity(employee, request);
        employee = employeeRepository.save(employee);

        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        return employeeMapper.toResponse(employee, isAdmin);
    }

    @Transactional
    public void softDeleteEmployee(Long id) {
        Employee employee = employeeRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        employee.setIsDeleted(true);
        employee.setStatus("INACTIVE");
        employeeRepository.save(employee);
    }
}

package com.ems.dashboard;

import com.ems.dashboard.dto.StatsResponse;
import com.ems.department.Department;
import com.ems.department.DepartmentRepository;
import com.ems.employee.EmployeeMapper;
import com.ems.employee.EmployeeRepository;
import com.ems.employee.dto.EmployeeSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    public StatsResponse getStats() {
        long total = employeeRepository.countActive();
        long active = employeeRepository.countByStatus("ACTIVE");
        long inactive = employeeRepository.countByStatus("INACTIVE");

        // Department breakdown
        Map<String, Long> byDepartment = new LinkedHashMap<>();
        List<Department> departments = departmentRepository.findAll();
        for (Department dept : departments) {
            long count = employeeRepository.countByDepartmentId(dept.getId());
            byDepartment.put(dept.getName(), count);
        }

        // Recent hires (last 5)
        List<EmployeeSummary> recentHires = employeeRepository
                .findRecentHires(PageRequest.of(0, 5))
                .stream()
                .map(employeeMapper::toSummary)
                .toList();

        return StatsResponse.builder()
                .totalEmployees(total)
                .activeEmployees(active)
                .inactiveEmployees(inactive)
                .byDepartment(byDepartment)
                .recentHires(recentHires)
                .build();
    }
}

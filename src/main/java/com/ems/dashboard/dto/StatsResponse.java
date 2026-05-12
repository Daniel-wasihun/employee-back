package com.ems.dashboard.dto;

import com.ems.employee.dto.EmployeeSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsResponse {

    private long totalEmployees;
    private long activeEmployees;
    private long inactiveEmployees;
    private Map<String, Long> byDepartment;
    private List<EmployeeSummary> recentHires;
}

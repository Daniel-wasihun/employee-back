package com.ems.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSummary {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String position;
    private String departmentName;
    private String status;
}

package com.ems.config;

import com.ems.department.Department;
import com.ems.department.DepartmentRepository;
import com.ems.employee.Employee;
import com.ems.employee.EmployeeRepository;
import com.ems.user.Role;
import com.ems.user.User;
import com.ems.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("Initializing sample data...");

            // Seed Admin User
            User admin = User.builder()
                    .email("admin@ems.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .isActive(true)
                    .build();
            admin = userRepository.save(admin);

            // Seed Manager User
            User manager = User.builder()
                    .email("manager@ems.com")
                    .password(passwordEncoder.encode("manager123"))
                    .role(Role.MANAGER)
                    .isActive(true)
                    .build();
            manager = userRepository.save(manager);

            // Seed Departments
            Department engineering = Department.builder()
                    .name("Engineering")
                    .description("Software and Infrastructure development")
                    .build();
            Department hr = Department.builder()
                    .name("Human Resources")
                    .description("Recruitment and employee relations")
                    .build();
            Department marketing = Department.builder()
                    .name("Marketing")
                    .description("Sales and branding")
                    .build();
            Department finance = Department.builder()
                    .name("Finance")
                    .description("Accounting and financial planning")
                    .build();

            departmentRepository.saveAll(List.of(engineering, hr, marketing, finance));

            // Seed Employees
            Employee emp1 = Employee.builder()
                    .userId(admin.getId()) // Link to Admin User
                    .firstName("Daniel")
                    .lastName("Wasihun")
                    .email("admin@ems.com")
                    .phone("0911223344")
                    .departmentId(engineering.getId())
                    .position("Lead Developer")
                    .salary(new BigDecimal("150000.00"))
                    .hireDate(LocalDate.of(2023, 1, 15))
                    .status("ACTIVE")
                    .build();

            Employee emp2 = Employee.builder()
                    .userId(manager.getId()) // Link to Manager User
                    .firstName("Sarah")
                    .lastName("Johnson")
                    .email("manager@ems.com")
                    .phone("0922334455")
                    .departmentId(hr.getId())
                    .position("HR Manager")
                    .salary(new BigDecimal("120000.00"))
                    .hireDate(LocalDate.of(2023, 3, 10))
                    .status("ACTIVE")
                    .build();

            Employee emp3 = Employee.builder()
                    .firstName("Michael")
                    .lastName("Smith")
                    .email("michael.s@ems.com")
                    .phone("0933445566")
                    .departmentId(engineering.getId())
                    .position("Backend Developer")
                    .salary(new BigDecimal("135000.00"))
                    .hireDate(LocalDate.of(2023, 6, 22))
                    .status("ACTIVE")
                    .build();
            
            Employee emp4 = Employee.builder()
                    .firstName("Emma")
                    .lastName("Davis")
                    .email("emma.d@ems.com")
                    .phone("0944556677")
                    .departmentId(marketing.getId())
                    .position("Marketing Specialist")
                    .salary(new BigDecimal("95000.00"))
                    .hireDate(LocalDate.of(2023, 8, 5))
                    .status("ACTIVE")
                    .build();

            employeeRepository.saveAll(List.of(emp1, emp2, emp3, emp4));

            log.info("Sample data initialization completed successfully.");
        }
    }
}

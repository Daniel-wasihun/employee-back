-- V2: Seed data
-- Admin password: Admin@123 (BCrypt hash)
INSERT INTO users (email, password, role) VALUES
    ('admin@ems.com', '$2a$12$LJ3m4ys3uz2YDjcOhBhBu.7JNBqMFOsmRCPsqkzKAyRAyOBHruCGS', 'ADMIN'),
    ('manager@ems.com', '$2a$12$LJ3m4ys3uz2YDjcOhBhBu.7JNBqMFOsmRCPsqkzKAyRAyOBHruCGS', 'MANAGER'),
    ('employee@ems.com', '$2a$12$LJ3m4ys3uz2YDjcOhBhBu.7JNBqMFOsmRCPsqkzKAyRAyOBHruCGS', 'EMPLOYEE');

INSERT INTO departments (name, description) VALUES
    ('Engineering', 'Software development and infrastructure'),
    ('Human Resources', 'People operations and recruitment'),
    ('Finance', 'Financial planning and accounting'),
    ('Marketing', 'Brand management and growth');

INSERT INTO employees (user_id, first_name, last_name, email, phone, department_id, position, salary, hire_date, status) VALUES
    (1, 'System', 'Administrator', 'admin@ems.com', '+1234567890', 1, 'CTO', 150000.00, '2024-01-15', 'ACTIVE'),
    (2, 'John', 'Manager', 'manager@ems.com', '+1234567891', 1, 'Engineering Manager', 120000.00, '2024-02-01', 'ACTIVE'),
    (3, 'Jane', 'Employee', 'employee@ems.com', '+1234567892', 1, 'Software Engineer', 95000.00, '2024-03-01', 'ACTIVE');

-- Set manager for Engineering department
UPDATE departments SET manager_id = 2 WHERE name = 'Engineering';

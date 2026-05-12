package com.ems.auth;

import com.ems.auth.dto.LoginRequest;
import com.ems.auth.dto.LoginResponse;
import com.ems.auth.dto.RegisterRequest;
import com.ems.common.exception.ResourceNotFoundException;
import com.ems.common.exception.UnauthorizedException;
import com.ems.employee.Employee;
import com.ems.employee.EmployeeRepository;
import com.ems.user.Role;
import com.ems.user.User;
import com.ems.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .role(user.getRole().name())
                .email(user.getEmail())
                .userId(user.getId())
                .build();
    }

    @Transactional
    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + request.getEmail());
        }

        Role role = Role.EMPLOYEE;
        if (request.getRole() != null && !request.getRole().isBlank()) {
            try {
                role = Role.valueOf(request.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role: " + request.getRole());
            }
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .isActive(true)
                .build();
        user = userRepository.save(user);

        // Create associated employee record
        Employee employee = Employee.builder()
                .userId(user.getId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .hireDate(LocalDate.now())
                .status("ACTIVE")
                .isDeleted(false)
                .build();
        employeeRepository.save(employee);

        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .role(user.getRole().name())
                .email(user.getEmail())
                .userId(user.getId())
                .build();
    }

    public LoginResponse refresh(String token) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(token);

        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new UnauthorizedException("User not found for refresh token"));

        String accessToken = jwtService.generateAccessToken(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .role(user.getRole().name())
                .email(user.getEmail())
                .userId(user.getId())
                .build();
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenService.revokeToken(refreshToken);
    }
}

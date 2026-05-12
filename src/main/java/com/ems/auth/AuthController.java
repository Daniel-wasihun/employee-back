package com.ems.auth;

import com.ems.auth.dto.LoginRequest;
import com.ems.auth.dto.LoginResponse;
import com.ems.auth.dto.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "JWT authentication endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return createResponseWithCookies(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user (ADMIN only in production)")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        LoginResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(createCookies(response))
                .body(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token using refresh token")
    public ResponseEntity<LoginResponse> refresh(@RequestBody Map<String, String> request, 
                                               @CookieValue(name = "ems_refresh_token", required = false) String cookieRefreshToken) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null || refreshToken.isBlank()) {
            refreshToken = cookieRefreshToken;
        }
        
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        
        LoginResponse response = authService.refresh(refreshToken);
        return createResponseWithCookies(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout and revoke refresh token")
    public ResponseEntity<Void> logout(@RequestBody(required = false) Map<String, String> request,
                                     @CookieValue(name = "ems_refresh_token", required = false) String cookieRefreshToken) {
        String refreshToken = null;
        if (request != null) {
            refreshToken = request.get("refreshToken");
        }
        if (refreshToken == null) {
            refreshToken = cookieRefreshToken;
        }
        
        if (refreshToken != null) {
            authService.logout(refreshToken);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie("ems_access_token"))
                .header(HttpHeaders.SET_COOKIE, deleteCookie("ems_refresh_token"))
                .build();
    }

    private ResponseEntity<LoginResponse> createResponseWithCookies(LoginResponse response) {
        return ResponseEntity.ok()
                .headers(createCookies(response))
                .body(response);
    }

    private HttpHeaders createCookies(LoginResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, createCookie("ems_access_token", response.getAccessToken(), 900)); // 15m
        headers.add(HttpHeaders.SET_COOKIE, createCookie("ems_refresh_token", response.getRefreshToken(), 604800)); // 7d
        return headers;
    }

    private String createCookie(String name, String value, long maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(false) // Set to true in production with HTTPS
                .path("/")
                .maxAge(maxAge)
                .sameSite("Lax")
                .build()
                .toString();
    }

    private String deleteCookie(String name) {
        return ResponseCookie.from(name, "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build()
                .toString();
    }
}

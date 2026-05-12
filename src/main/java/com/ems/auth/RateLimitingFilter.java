package com.ems.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Map<String, RequestCounter> limiters = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS_PER_MINUTE = 20;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String path = request.getRequestURI();
        
        // Only rate limit auth endpoints
        if (path.startsWith("/api/v1/auth/")) {
            String clientIp = getClientIp(request);
            RequestCounter counter = limiters.computeIfAbsent(clientIp, k -> new RequestCounter());

            if (counter.isLimitExceeded()) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Too many requests. Please try again later.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    private static class RequestCounter {
        private final AtomicInteger count = new AtomicInteger(0);
        private long lastResetTime = System.currentTimeMillis();

        public synchronized boolean isLimitExceeded() {
            long now = System.currentTimeMillis();
            if (now - lastResetTime > TimeUnit.MINUTES.toMillis(1)) {
                count.set(0);
                lastResetTime = now;
            }
            return count.incrementAndGet() > MAX_REQUESTS_PER_MINUTE;
        }
    }
}

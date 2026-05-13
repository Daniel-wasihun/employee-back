package com.ems.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/metadata")
@Tag(name = "Metadata", description = "System metadata and lookups")
public class MetadataController {

    @GetMapping("/roles")
    @Operation(summary = "Get all available system roles")
    public ResponseEntity<List<Map<String, String>>> getRoles() {
        List<Map<String, String>> roles = Arrays.stream(Role.values())
                .map(role -> Map.of(
                        "value", role.name(),
                        "label", formatLabel(role.name())
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }

    private String formatLabel(String name) {
        return switch (name) {
            case "ADMIN" -> "Administrator";
            case "MANAGER" -> "Department Manager";
            case "EMPLOYEE" -> "Staff Member";
            default -> name;
        };
    }
}

package com.mockforge.management;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Health and diagnostics endpoints for MockForge.
 * Exposed under /_mockforge for management purposes.
 */
@RestController
@RequestMapping("/_mockforge")
public class HealthController {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${mockforge.home}")
    private String mockForgeHome;

    /**
     * Basic health check endpoint.
     * Used by Docker health checks and monitoring.
     * 
     * @return Health status
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now().toString()
        ));
    }

    /**
     * Readiness check endpoint.
     * Indicates if the application is ready to serve requests.
     * 
     * @return Readiness status
     */
    @GetMapping("/ready")
    public ResponseEntity<Map<String, Object>> ready() {
        return ResponseEntity.ok(Map.of(
            "status", "READY",
            "timestamp", LocalDateTime.now().toString()
        ));
    }

    /**
     * Basic diagnostics endpoint (protected in production).
     * Shows application info and configuration.
     * 
     * @return Diagnostic information
     */
    @GetMapping("/diagnostics")
    public ResponseEntity<Map<String, Object>> diagnostics() {
        return ResponseEntity.ok(Map.of(
            "application", applicationName,
            "version", "0.1.0-SNAPSHOT",
            "javaVersion", System.getProperty("java.version"),
            "mockForgeHome", mockForgeHome,
            "timestamp", LocalDateTime.now().toString()
        ));
    }
}

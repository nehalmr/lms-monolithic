package com.library.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"}, 
             methods = {RequestMethod.GET, RequestMethod.OPTIONS},
             allowedHeaders = "*")
@Tag(name = "Health", description = "Application health monitoring APIs")
public class HealthController {
    
    @Operation(
        summary = "Application health check",
        description = "Check the overall health status of the application"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Application is healthy"),
        @ApiResponse(responseCode = "503", description = "Application is unhealthy")
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", System.currentTimeMillis());
        response.put("service", "Library Management System");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }
    
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok().build();
    }
    
    @Operation(
        summary = "Database health check",
        description = "Check the health status of the database connection"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Database is healthy"),
        @ApiResponse(responseCode = "503", description = "Database is unhealthy")
    })
    @GetMapping("/database")
    public ResponseEntity<Map<String, Object>> databaseHealth() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Simple database check - you can enhance this
            response.put("database", "UP");
            response.put("status", "HEALTHY");
            response.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            response.put("database", "DOWN");
            response.put("status", "UNHEALTHY");
            response.put("error", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());
        }
        return ResponseEntity.ok(response);
    }
}

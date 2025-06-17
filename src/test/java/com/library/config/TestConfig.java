package com.library.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@TestConfiguration
@ActiveProfiles("test")
public class TestConfig {
    
    @Bean
    @Primary
    public Clock testClock() {
        // Fixed clock for predictable testing
        return Clock.fixed(Instant.parse("2024-01-15T10:00:00Z"), ZoneId.systemDefault());
    }
}

package com.mockforge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * MockForge - Local-first API Mocking & Backend Simulation Platform
 * 
 * Main application entry point for the Spring Boot backend.
 * 
 * @author Subham Raj
 * @version 0.1.0-SNAPSHOT
 */
@SpringBootApplication
@EnableCaching
public class MockForgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockForgeApplication.class, args);
    }
}

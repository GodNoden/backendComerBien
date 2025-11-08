package com.comerbien.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/api") // AGREGAR este endpoint
    public Map<String, String> apiHome() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Recipe App API is running!");
        response.put("status", "OK");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return response;
    }

    @GetMapping("/api/health") // CAMBIAR a /api/health
    public Map<String, String> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "recipe-backend");
        return response;
    }
}

package com.swynex.backend.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, String> root() {
        return Map.of(
                "service", "SWYNEX Deployable Backend Service",
                "docs", "/swagger-ui.html",
                "health", "/actuator/health");
    }
}

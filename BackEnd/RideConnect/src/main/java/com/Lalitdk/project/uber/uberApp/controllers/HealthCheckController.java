package com.Lalitdk.project.uber.uberApp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/")
    public ResponseEntity<String> healthcheckController(){
        return ResponseEntity.ok("Hello it's fine");
    }
}

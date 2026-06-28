package com.ftn.sbnz.controller;

import com.ftn.sbnz.dto.RegisterRequestDTO;
import com.ftn.sbnz.leservice.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO dto) {
        userService.register(dto);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/test")
    public String test() {
        return "OK";
    }
}
package com.ftn.sbnz.controller;

import com.ftn.sbnz.dto.RegisterRequestDTO;
import com.ftn.sbnz.dto.SuccessDTO;
import com.ftn.sbnz.dto.UserDTO;
import com.ftn.sbnz.dto.ErrorDTO;
import com.ftn.sbnz.leservice.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ftn.sbnz.dto.LoginRequestDTO;
import com.ftn.sbnz.dasmodel.User;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO dto) {

        try {
            userService.register(dto);
            return ResponseEntity.ok(new SuccessDTO("User registered successfully"));
        }
        catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorDTO(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {

        try {
            UserDTO user = userService.login(dto);
            return ResponseEntity.ok(user);
        }
        catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorDTO(e.getMessage()));
        }
    }

    @GetMapping("/test")
    public String test() {
        return "OK";
    }
}
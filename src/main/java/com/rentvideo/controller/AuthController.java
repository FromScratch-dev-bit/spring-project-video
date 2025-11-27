package com.rentvideo.controller;

import com.rentvideo.dto.request.LoginRequest;
import com.rentvideo.dto.request.RegisterRequest;
import com.rentvideo.dto.response.AuthResponse;
import com.rentvideo.dto.response.UserResponse;
import com.rentvideo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = userService.registerUser(request);
        AuthResponse response = new AuthResponse(
                "User registered successfully",
                user
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        // With Basic Auth, the actual authentication is handled by Spring Security
        // This endpoint just confirms successful login
        UserResponse user = userService.getCurrentUser();
        AuthResponse response = new AuthResponse(
                "Login successful",
                user
        );
        return ResponseEntity.ok(response);
    }
}

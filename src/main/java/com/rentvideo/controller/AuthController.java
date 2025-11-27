package com.rentvideo.controller;

import com.rentvideo.dto.request.LoginRequest;
import com.rentvideo.dto.request.RegisterRequest;
import com.rentvideo.dto.response.AuthResponse;
import com.rentvideo.dto.response.JwtAuthResponse;
import com.rentvideo.dto.response.UserResponse;
import com.rentvideo.security.JwtUtil;
import com.rentvideo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = userService.registerUser(request);
        AuthResponse response = new AuthResponse(
                "User registered successfully. Please login to get your access token.",
                user
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@Valid @RequestBody LoginRequest request) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        
        // Generate JWT token
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);
        
        // Get user details
        UserResponse user = userService.getUserResponseByUsername(userDetails.getUsername());
        
        // Return response with token
        JwtAuthResponse response = new JwtAuthResponse(
                token,
                "Bearer",
                user,
                "Login successful"
        );
        
        return ResponseEntity.ok(response);
    }
}

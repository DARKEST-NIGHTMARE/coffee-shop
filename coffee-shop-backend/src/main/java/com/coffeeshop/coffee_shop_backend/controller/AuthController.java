package com.coffeeshop.coffee_shop_backend.controller;

import com.coffeeshop.coffee_shop_backend.dto.AuthResponse;
import com.coffeeshop.coffee_shop_backend.dto.LoginRequest;
import com.coffeeshop.coffee_shop_backend.dto.RegisterRequest;
import com.coffeeshop.coffee_shop_backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}
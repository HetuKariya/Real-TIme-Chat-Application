package com.chat.app.controller;

import com.chat.app.model.AuthRequest;
import com.chat.app.model.AuthResponse;
import com.chat.app.service.JwtService;
import com.chat.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthController(UserService userService, JwtService jwtService,
                          AuthenticationManager authManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest req) {
        userService.register(req.getUsername(), req.getPassword());
        String token = jwtService.generateToken(req.getUsername());
        return ResponseEntity.ok(new AuthResponse(token, req.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        String token = jwtService.generateToken(req.getUsername());
        return ResponseEntity.ok(new AuthResponse(token, req.getUsername()));
    }
}
package com.foro.demo.web;

import com.foro.demo.dto.LoginRequest;
import com.foro.demo.dto.TokenResponse;
import com.foro.demo.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwt;

    public AuthController(AuthenticationManager authManager, JwtService jwt) {
        this.authManager = authManager;
        this.jwt = jwt;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest body) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword())
        );

        var principal = (UserDetails) auth.getPrincipal();
        String token = jwt.generate(principal.getUsername(), Map.of("role", principal.getAuthorities().iterator().next().getAuthority()));

        return ResponseEntity.ok(new TokenResponse(token));
    }
}
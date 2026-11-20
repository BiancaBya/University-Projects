package org.example;

import lombok.RequiredArgsConstructor;
import org.example.Domain.Admin;
import org.example.jwt.JwtUtil;
import org.example.jwt.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    public AuthController(){
        authService = new AuthService();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        try {
            Admin admin = authService.login(email, password);
            String token = JwtUtil.generateToken(admin.getId().toString());
            return ResponseEntity.ok(new TokenResponse(token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}




package com.ragbot.rag_knowledge_bot.controller;

import com.ragbot.rag_knowledge_bot.dto.AuthResponse;
import com.ragbot.rag_knowledge_bot.dto.LoginRequest;
import com.ragbot.rag_knowledge_bot.dto.RegisterRequest;
import com.ragbot.rag_knowledge_bot.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
        @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
        @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}

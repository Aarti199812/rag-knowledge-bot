package com.ragbot.rag_knowledge_bot.service;

import com.ragbot.rag_knowledge_bot.dto.AuthResponse;
import com.ragbot.rag_knowledge_bot.dto.LoginRequest;
import com.ragbot.rag_knowledge_bot.dto.RegisterRequest;
import com.ragbot.rag_knowledge_bot.entity.User;
import com.ragbot.rag_knowledge_bot.repository.UserRepository;
import com.ragbot.rag_knowledge_bot.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole()  != null ? request.getRole() : "EMPLOYEE");

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return new AuthResponse(token, user.getName(), user.getEmail(), user.getRole());
    }
    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("user not found"));

    if(! passwordEncoder.matches(request.getPassword(),user.getPassword())){
        throw new RuntimeException("invalid password");
    }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return new AuthResponse(token, user.getName(), user.getEmail(), user.getRole());
    }
}

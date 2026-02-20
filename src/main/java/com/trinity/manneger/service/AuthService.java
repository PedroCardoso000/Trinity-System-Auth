package com.trinity.manneger.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trinity.manneger.domain.Role;
import com.trinity.manneger.dto.AuthResponse;
import com.trinity.manneger.dto.LoginRequest;
import com.trinity.manneger.dto.RegisterRequest;
import com.trinity.manneger.entity.User;
import com.trinity.manneger.jwt.JwtTokenProvider;
import com.trinity.manneger.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
            .name(request.getName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.valueOf(request.getRole()))
            .build();

        userRepository.save(user);
        
        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse authenticate(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponse(token);
    }
}

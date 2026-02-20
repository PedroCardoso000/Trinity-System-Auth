package com.trinity.manneger.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trinity.manneger.domain.Role;
import com.trinity.manneger.domain.dto.AcademicCreatedEvent;
import com.trinity.manneger.domain.dto.AuthResponse;
import com.trinity.manneger.domain.dto.LoginRequest;
import com.trinity.manneger.domain.dto.RegisterRequest;
import com.trinity.manneger.entity.Academic;
import com.trinity.manneger.entity.User;
import com.trinity.manneger.jwt.JwtTokenProvider;
import com.trinity.manneger.rabbitmq.EventPublisher;
import com.trinity.manneger.repository.AcademicRepository;
import com.trinity.manneger.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AcademicRepository academicRepository;
    private final EventPublisher eventPublisher;

    public AuthResponse registerStudent(RegisterRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);

        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse registerAdm(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .active(true)
                .build();

        userRepository.save(user);

        Academic academic = Academic.builder()
                .email(request.getEmail())
                .name(request.getName())
                .Iduser(user.getId().toString())
                .build();

        academicRepository.save(academic);

        eventPublisher.publish(
                "academic.created",
                AcademicCreatedEvent.builder()
                        .academicId(academic.getId())
                        .name(academic.getName())
                        .build());

        String token = jwtTokenProvider.generateToken(user, academic.getId());
        return new AuthResponse(token);
    }

    /**
     * Authenticate a user
     * 
     * @param request
     * @return
     */
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

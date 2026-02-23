package com.trinity.manneger.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trinity.manneger.domain.Role;
import com.trinity.manneger.domain.dto.AcademicCreatedEvent;
import com.trinity.manneger.domain.dto.AuthResponse;
import com.trinity.manneger.domain.dto.LoginRequest;
import com.trinity.manneger.domain.dto.RegisterRequest;
import com.trinity.manneger.domain.dto.RegisterRequestAdm;
import com.trinity.manneger.entity.Academic;
import com.trinity.manneger.entity.User;
import com.trinity.manneger.jwt.JwtTokenProvider;
import com.trinity.manneger.rabbitmq.EventPublisher;
import com.trinity.manneger.repository.AcademicRepository;
import com.trinity.manneger.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AcademicRepository academicRepository;
    @Autowired
    private EventPublisher eventPublisher;

    public AuthResponse registerStudent(RegisterRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);

        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse registerAdm(RegisterRequestAdm request) {

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

        user = userRepository.save(user);

        Academic academic = Academic.builder()
                .email(request.getEmail())
                .name(request.getNameAcademia())
                .Iduser(user.getId().toString())
                .build();

        academic = academicRepository.save(academic);

        user.setIdAcademic(academic.getId());
        userRepository.save(user);

        AcademicCreatedEvent event = new AcademicCreatedEvent();
        event.setAcademicId(academic.getId());
        event.setName(academic.getName());

        eventPublisher.publish(
                "academic.created",
                event);

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

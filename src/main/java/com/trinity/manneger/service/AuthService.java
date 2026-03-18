package com.trinity.manneger.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trinity.manneger.domain.Role;
import com.trinity.manneger.domain.dto.AcademicCreatedEvent;
import com.trinity.manneger.domain.dto.AuthResponseAdm;
import com.trinity.manneger.domain.dto.AuthResponseStudent;
import com.trinity.manneger.domain.dto.AuthResponseTeacher;
import com.trinity.manneger.domain.dto.LoginRequest;
import com.trinity.manneger.domain.dto.RegisterRequestStudent;
import com.trinity.manneger.domain.dto.RegisterRequestTeacher;
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

        // REGISTER STUDENT
        public AuthResponseStudent registerStudent(RegisterRequestStudent request) {

                User user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new RuntimeException("Student not found"));

                if (!user.getRole().equals(Role.STUDENT)) {
                        throw new RuntimeException("User is not a student");
                }

                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setActive(true);

                userRepository.save(user);

                String token = jwtTokenProvider.generateToken(user);
                AuthResponseStudent response = new AuthResponseStudent(token, user.getEmail(),
                                user.getIdAcademic().toString(),
                                Role.STUDENT);
                return response;
        }

        // CREATED TEACHER
        public AuthResponseTeacher registerTeacher(RegisterRequestTeacher request) {

                User user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new RuntimeException("Teacher not found"));

                if (!user.getRole().equals(Role.TEACHER)) {
                        throw new RuntimeException("User is not a teacher");
                }

                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setActive(true);

                userRepository.save(user);

                String token = jwtTokenProvider.generateToken(user);
                AuthResponseTeacher response = new AuthResponseTeacher(token, user.getEmail(),
                                user.getIdAcademic().toString(),
                                Role.TEACHER);
                return response;
        }

        // CREATED ADMIN
        public AuthResponseAdm registerAdm(RegisterRequestAdm request) {

                if (userRepository.existsByEmail(request.getEmail())) {
                        throw new RuntimeException("Email already registered");
                }

                User user = new User();
                user.setName(request.getName());
                user.setEmail(request.getEmail());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setRole(Role.ADMIN);
                user.setActive(true);

                user = userRepository.save(user);

                Academic academic = new Academic();
                academic.setEmail(request.getEmail());
                academic.setName(request.getNameAcademia());
                academic.setIduser(user.getId().toString());

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
                AuthResponseAdm response = new AuthResponseAdm(token, user.getEmail(), academic.getId().toString(),
                                academic.getName(), user.getName(), Role.ADMIN);
                return response;
        }

        /**
         * Authenticate a user
         * 
         * @param request
         * @return
         */
        public Object authenticate(LoginRequest request) {

                User user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

                if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        throw new RuntimeException("Invalid credentials");
                }

                String token = jwtTokenProvider.generateToken(user);

                if (user.getRole() == Role.ADMIN) {
                        Academic academic = academicRepository.findById(user.getIdAcademic()).get();
                        AuthResponseAdm response = new AuthResponseAdm(token, user.getEmail(),
                                        user.getIdAcademic().toString(),
                                        academic.getName(), user.getName(), Role.ADMIN);
                        return response;
                }

                AuthResponseStudent response = new AuthResponseStudent(token, user.getEmail(),
                                user.getIdAcademic().toString(),
                                Role.STUDENT);
                return response;
        }
}

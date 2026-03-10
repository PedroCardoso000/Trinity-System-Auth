package com.trinity.manneger.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.trinity.manneger.domain.dto.AuthResponseAdm;
import com.trinity.manneger.domain.dto.AuthResponseStudent;
import com.trinity.manneger.domain.dto.AuthResponseTeacher;
import com.trinity.manneger.domain.dto.LoginRequest;
import com.trinity.manneger.domain.dto.RegisterRequestStudent;
import com.trinity.manneger.domain.dto.RegisterRequestTeacher;
import com.trinity.manneger.domain.dto.RegisterRequestAdm;
import com.trinity.manneger.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Register a new teacher
     * 
     * @param request
     * @return
     */
    @PostMapping("/register-teacher")
    public ResponseEntity<AuthResponseTeacher> registerTeacher(@RequestBody RegisterRequestTeacher request) {
        return ResponseEntity.ok(authService.registerTeacher(request));
    }

    /**
     * Confirm register student
     * 
     * @param request
     * @return
     */
    @PostMapping("/register-student")
    public ResponseEntity<AuthResponseStudent> registerStudent(@RequestBody RegisterRequestStudent request) {
        return ResponseEntity.ok(authService.registerStudent(request));
    }

    /**
     * Register a new user
     * 
     * @param request
     * @return
     */
    @PostMapping("/register-adm")
    public ResponseEntity<AuthResponseAdm> register(@RequestBody RegisterRequestAdm request) {
        return ResponseEntity.ok(authService.registerAdm(request));
    }

    /**
     * Login a user
     * 
     * @param request
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
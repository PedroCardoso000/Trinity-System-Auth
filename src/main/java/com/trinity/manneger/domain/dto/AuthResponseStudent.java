package com.trinity.manneger.domain.dto;

import com.trinity.manneger.domain.Role;

public record AuthResponseStudent(
        String token, String email, String academic, Role role) {
}

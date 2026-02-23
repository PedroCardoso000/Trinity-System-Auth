package com.trinity.manneger.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestAdm {
        private String nameAcademia;
        private String name;
        private String email;
        private String password;
        private String role;
        private Long academic;
}

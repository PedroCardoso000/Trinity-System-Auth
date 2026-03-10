package com.trinity.manneger.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreatedEvent {
    private Long userId;
    private String email;
}

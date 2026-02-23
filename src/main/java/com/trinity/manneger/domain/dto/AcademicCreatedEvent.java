package com.trinity.manneger.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AcademicCreatedEvent {
    private Long academicId;
    private String name;
}

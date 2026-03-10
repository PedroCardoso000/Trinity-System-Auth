package com.trinity.manneger.domain.dto;

import lombok.Data;

@Data
public class TeacherUpdatedEvent {

    private Long teacherId;
    private String email;
    private String name;
    private Boolean active;
}

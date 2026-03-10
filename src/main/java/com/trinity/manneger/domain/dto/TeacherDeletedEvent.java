package com.trinity.manneger.domain.dto;

import lombok.Data;

@Data
public class TeacherDeletedEvent {

    private Long teacherId;
    private String email;
}

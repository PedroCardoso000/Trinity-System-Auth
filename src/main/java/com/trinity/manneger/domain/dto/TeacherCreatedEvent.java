package com.trinity.manneger.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherCreatedEvent {

    private Long teacherId;
    private String name;
    private String email;
    private Long academicId;
    private List<Long> branchId;
}

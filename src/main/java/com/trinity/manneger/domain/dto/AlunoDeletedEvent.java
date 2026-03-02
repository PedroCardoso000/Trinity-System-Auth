package com.trinity.manneger.domain.dto;

import lombok.Data;

@Data
public class AlunoDeletedEvent {

    private Long alunoId;
    private String email;
}

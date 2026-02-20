package com.trinity.manneger.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlunoCreatedEvent {

    private Long alunoId;
    private String email;
    private String nome;
}

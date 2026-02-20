package com.trinity.manneger.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "academics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Academic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    
    private String Iduser;
}

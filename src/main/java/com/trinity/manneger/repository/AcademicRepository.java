package com.trinity.manneger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trinity.manneger.entity.Academic;

public interface AcademicRepository extends JpaRepository<Academic, Long> {
    
}

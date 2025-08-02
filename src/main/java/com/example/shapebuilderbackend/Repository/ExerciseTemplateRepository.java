package com.example.shapebuilderbackend.Repository;

import com.example.shapebuilderbackend.Model.ExerciseTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExerciseTemplateRepository extends JpaRepository<ExerciseTemplate,Long> {
    Optional<ExerciseTemplate> findByName(String name);
}


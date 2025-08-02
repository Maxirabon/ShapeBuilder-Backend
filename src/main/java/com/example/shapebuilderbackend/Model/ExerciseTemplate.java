package com.example.shapebuilderbackend.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "exercise_template")
public class ExerciseTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public ExerciseTemplate(String name) {
        this.name = name;
    }

    public ExerciseTemplate() {

    }
}
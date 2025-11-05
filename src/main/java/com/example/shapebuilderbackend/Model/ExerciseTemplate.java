package com.example.shapebuilderbackend.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "exerciseTemplate", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    private List<Exercise> exercises = new ArrayList<>();
}
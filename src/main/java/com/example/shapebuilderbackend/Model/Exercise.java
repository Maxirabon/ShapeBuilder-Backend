package com.example.shapebuilderbackend.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int sets;
    private int repetitions;
    private double weight;

    @ManyToOne(optional = false)
    @JoinColumn(name = "calendar_id")
    @ToString.Exclude
    private Calendar calendar;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exercise_template_id")
    @ToString.Exclude
    private ExerciseTemplate exerciseTemplate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Exercise exercise = (Exercise) o;

        return id != null && id.equals(exercise.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}

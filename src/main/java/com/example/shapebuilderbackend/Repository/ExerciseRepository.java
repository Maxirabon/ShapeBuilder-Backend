package com.example.shapebuilderbackend.Repository;

import com.example.shapebuilderbackend.Model.Calendar;
import com.example.shapebuilderbackend.Model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise,Long> {
    //Optional<Exercise> findByCalendarAndName(Calendar calendar, String name);
}

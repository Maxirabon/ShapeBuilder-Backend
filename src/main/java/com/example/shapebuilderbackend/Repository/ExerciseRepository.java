package com.example.shapebuilderbackend.Repository;

import com.example.shapebuilderbackend.Model.Calendar;
import com.example.shapebuilderbackend.Model.Exercise;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise,Long> {
    //Optional<Exercise> findByCalendarAndName(Calendar calendar, String name);
    @Modifying
    @Transactional
    @Query("DELETE FROM Exercise e WHERE e.exerciseTemplate.id = :templateId")
    void deleteAllByExerciseTemplateId(@Param("templateId") Long templateId);
}

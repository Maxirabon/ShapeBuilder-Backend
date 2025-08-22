package com.example.shapebuilderbackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoExerciseSummary {
    private Long id;
    private String exerciseName;
    private int sets;
    private int repetitions;
    private double weight;
}
package com.example.shapebuilderbackend.Dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AddExerciseRequest {
    @NotNull
    private LocalDate day;

    @NotNull
    private Long exerciseTemplateId;

    @NotNull
    private Long userId;

    @Min(1)
    private int sets;

    @Min(1)
    private int repetitions;

    @Min(0)
    private double weight;
}

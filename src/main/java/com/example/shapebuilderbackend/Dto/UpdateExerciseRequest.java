package com.example.shapebuilderbackend.Dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateExerciseRequest {

    @NotNull
    private Long id;

    @Min(1)
    private int sets;

    @Min(1)
    private int repetitions;

    @Min(0)
    private double weight;
}

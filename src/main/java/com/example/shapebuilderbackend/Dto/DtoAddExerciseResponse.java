package com.example.shapebuilderbackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoAddExerciseResponse {
    private Long id;
    private Long exerciseTemplateId;
    private String name;
    private int sets;
    private int repetitions;
    private double weight;
}

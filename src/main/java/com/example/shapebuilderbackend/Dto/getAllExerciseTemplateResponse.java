package com.example.shapebuilderbackend.Dto;

import lombok.Data;

@Data
public class getAllExerciseTemplateResponse {
    private Long id;
    private String name;

    public getAllExerciseTemplateResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

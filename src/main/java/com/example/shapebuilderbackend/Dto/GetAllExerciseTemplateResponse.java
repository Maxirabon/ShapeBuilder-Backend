package com.example.shapebuilderbackend.Dto;

import lombok.Data;

@Data
public class GetAllExerciseTemplateResponse {
    private Long id;
    private String name;

    public GetAllExerciseTemplateResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

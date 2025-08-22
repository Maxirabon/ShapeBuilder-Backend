package com.example.shapebuilderbackend.Dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserProductRequest {
    private Long id;

    @NotBlank
    private String name;

    @Min(0)
    private int protein;

    @Min(0)
    private int carbs;

    @Min(0)
    private int fat;

    @Min(0)
    private int calories;
}

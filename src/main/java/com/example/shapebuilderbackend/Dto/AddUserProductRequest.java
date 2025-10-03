package com.example.shapebuilderbackend.Dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserProductRequest {
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
    private boolean isCustom;
}

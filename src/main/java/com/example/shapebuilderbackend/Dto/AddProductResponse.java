package com.example.shapebuilderbackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddProductResponse {
    private Long id;
    private String name;
    private int protein;
    private int fat;
    private int carbs;
    private int calories;
    private boolean isCustom;
}

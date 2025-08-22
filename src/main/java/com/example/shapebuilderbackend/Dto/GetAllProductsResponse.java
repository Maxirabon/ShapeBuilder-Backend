package com.example.shapebuilderbackend.Dto;

import lombok.Data;

@Data
public class GetAllProductsResponse {
    private Long id;
    private String name;
    private int protein;
    private int fat;
    private int carbs;
    private int calories;
    private boolean isCustom;

    public GetAllProductsResponse(Long id, String name, int protein, int fat, int carbs, int calories,  boolean isCustom) {
        this.id = id;
        this.name = name;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.calories = calories;
        this.isCustom = isCustom;
    }
}

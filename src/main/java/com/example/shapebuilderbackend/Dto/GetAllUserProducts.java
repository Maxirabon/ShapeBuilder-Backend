package com.example.shapebuilderbackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetAllUserProducts {
    private Long id;
    private String name;
    private int protein;
    private int fat;
    private int carbs;
    private int calories;

    public GetAllUserProducts(Long id, String name, int protein, int fat, int carbs, int calories) {
        this.id = id;
        this.name = name;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.calories = calories;
    }

}

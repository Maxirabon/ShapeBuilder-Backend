package com.example.shapebuilderbackend.Dto;

import lombok.Data;

import java.util.List;

@Data
public class DtoGetDayMeals {
    private Long id;
    private String description;
    List<DtoMealProduct> mealProducts;

    public DtoGetDayMeals(Long id, String description, List<DtoMealProduct> mealProducts) {
        this.id = id;
        this.description = description;
        this.mealProducts = mealProducts;
    }
}

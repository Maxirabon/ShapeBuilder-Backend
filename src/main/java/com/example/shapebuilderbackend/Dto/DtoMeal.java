package com.example.shapebuilderbackend.Dto;

import com.example.shapebuilderbackend.Model.Meal;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class DtoMeal {
    private Long id;
    private String description;
    private List<DtoMealProduct> mealProducts;

    public DtoMeal(Meal meal) {
        this.id = meal.getId();
        this.description = meal.getDescription();
        this.mealProducts = meal.getMealProducts()
                .stream()
                .map(DtoMealProduct::new)
                .collect(Collectors.toList());
    }
}

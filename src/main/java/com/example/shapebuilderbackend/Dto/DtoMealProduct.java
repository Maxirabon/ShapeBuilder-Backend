package com.example.shapebuilderbackend.Dto;

import com.example.shapebuilderbackend.Model.MealProduct;
import lombok.Data;

@Data
public class DtoMealProduct {
    private Long id;
    private Long productId;
    private String name;
    private double calories;
    private double protein;
    private double carbs;
    private double fat;
    private double amount;

    public DtoMealProduct(MealProduct mp) {
        this.id = mp.getId();
        this.productId = mp.getProduct().getId();
        this.name = mp.getProduct().getName();
        this.amount = mp.getAmount();

        double ratio = mp.getAmount() / 100.0;
        this.calories = mp.getProduct().getCalories() * ratio;
        this.protein = mp.getProduct().getProtein() * ratio;
        this.carbs = mp.getProduct().getCarbs() * ratio;
        this.fat = mp.getProduct().getFat() * ratio;
    }

    public DtoMealProduct(Long id, Long productId,  String name, double calories, double protein, double carbs, double fat, double amount) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.amount = amount;
    }
}

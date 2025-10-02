package com.example.shapebuilderbackend.Dto;

import com.example.shapebuilderbackend.Model.MealProduct;
import lombok.Data;

@Data
public class DtoMealProduct {
    private Long id;
    private String name;
    private double calories;
    private double protein;
    private double carbs;
    private double fat;
    private double amount;

    public DtoMealProduct(MealProduct mp) {
        this.id = mp.getId();
        this.name = mp.getProduct().getName();
        this.calories = mp.getProduct().getCalories();
        this.protein = mp.getProduct().getProtein();
        this.carbs = mp.getProduct().getCarbs();
        this.fat = mp.getProduct().getFat();
        this.amount = mp.getAmount();
    }

    public DtoMealProduct(Long id, String name, double calories, double protein, double carbs, double fat, double amount) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.amount = amount;
    }
}

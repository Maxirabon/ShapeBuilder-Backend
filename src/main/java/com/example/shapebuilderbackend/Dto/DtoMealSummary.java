package com.example.shapebuilderbackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DtoMealSummary {
    private String description;
    private List<DtoProductSummary> products;
    private double totalProtein;
    private double totalFat;
    private double totalCarbs;
    private double totalCalories;
}

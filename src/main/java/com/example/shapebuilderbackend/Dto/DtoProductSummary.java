package com.example.shapebuilderbackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoProductSummary {
    private String name;
    private double amount;
    private double protein;
    private double fat;
    private double carbs;
    private double calories;
}

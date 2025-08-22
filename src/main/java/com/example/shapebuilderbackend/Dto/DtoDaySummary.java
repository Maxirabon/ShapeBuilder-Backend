package com.example.shapebuilderbackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class DtoDaySummary {
    private LocalDate date;
    private List<DtoMealSummary> meals;
    private double totalProtein;
    private double totalFat;
    private double totalCarbs;
    private double totalCalories;
}

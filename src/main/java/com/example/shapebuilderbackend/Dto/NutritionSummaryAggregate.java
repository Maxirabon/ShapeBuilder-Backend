package com.example.shapebuilderbackend.Dto;

import java.util.List;

public record NutritionSummaryAggregate(
        List<DtoDaySummary> daySummaries,
        double totalCalories,
        double avgCalories,
        double totalProtein,
        double avgProtein,
        double totalCarbs,
        double avgCarbs,
        double totalFat,
        double avgFat
) {}
package com.example.shapebuilderbackend.Dto;

import java.util.List;

public record NutritionSummaryAggregate(
        List<DtoDaySummary> daySummaries,
        double avgCalories,
        double avgProtein,
        double avgCarbs,
        double avgFat
) {}
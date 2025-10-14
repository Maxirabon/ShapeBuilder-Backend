package com.example.shapebuilderbackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoWeekSummary {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<DtoDaySummary> days;
    private double averageCalories;
    private double averageProtein;
    private double averageCarbs;
    private double averageFat;
    private List<DtoChartPointFood> chartData;
}

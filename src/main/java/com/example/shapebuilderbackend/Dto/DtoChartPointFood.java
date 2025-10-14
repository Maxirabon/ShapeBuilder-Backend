package com.example.shapebuilderbackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoChartPointFood {
    private LocalDate date;
    private double calories;
    private double protein;
    private double carbs;
    private double fat;
}

package com.example.shapebuilderbackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoPeriodExerciseSummary {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<DtoDayExerciseSummary> daySummaries;
    private double avgVolume;
    private double totalVolume;
    private List<DtoChartPointExercise> chartData;
}

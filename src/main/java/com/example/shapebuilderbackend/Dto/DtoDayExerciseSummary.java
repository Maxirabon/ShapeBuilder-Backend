package com.example.shapebuilderbackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class DtoDayExerciseSummary {
    private LocalDate date;
    private List<DtoExerciseSummary> exercises;

    private double totalVolume;
    private double avgVolume;
}

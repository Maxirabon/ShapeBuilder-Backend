package com.example.shapebuilderbackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetAllUserProducts {
    private String name;
    private int protein;
    private int fat;
    private int carbs;
    private int calories;

}

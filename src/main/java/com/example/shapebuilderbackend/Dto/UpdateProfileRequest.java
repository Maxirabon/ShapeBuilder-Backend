package com.example.shapebuilderbackend.Dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private int age;
    private double weight;
    private int height;
}

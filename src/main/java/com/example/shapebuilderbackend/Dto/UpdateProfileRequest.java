package com.example.shapebuilderbackend.Dto;

import com.example.shapebuilderbackend.Model.Activity.Activity;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    private int age;
    private double weight;
    private int height;
    private Activity activity;
}

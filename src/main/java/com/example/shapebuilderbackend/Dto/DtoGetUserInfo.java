package com.example.shapebuilderbackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoGetUserInfo {
    private String firstName;
    private String lastName;
    private String email;
    private char gender;
    private int age;
    private int height;
    private double weight;
    private String activity;
}

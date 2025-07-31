package com.example.shapebuilderbackend.Dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private char gender;
    private int age;
    private double weight;
    private int height;
}

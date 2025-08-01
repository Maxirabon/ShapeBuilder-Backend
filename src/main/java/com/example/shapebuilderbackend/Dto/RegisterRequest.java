package com.example.shapebuilderbackend.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;

    @NotBlank
    @Email(message = "Nieprawidłowy format adresu e-mail")
    private String email;
    private String password;
    private char gender;
    private int age;
    private double weight;
    private int height;
}

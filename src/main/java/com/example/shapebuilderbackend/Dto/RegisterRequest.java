package com.example.shapebuilderbackend.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
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
    @Min(value = 1, message = "Wiek musi być większy niż 0")
    private int age;

    @Min(value = 1, message = "Waga musi być większa niż 0")
    private double weight;

    @Min(value = 1, message = "Wzrost musi być większy niż 0")
    private int height;
    private String activity;
}

package com.example.shapebuilderbackend.Dto;

import com.example.shapebuilderbackend.Model.Activity.Activity;
import com.example.shapebuilderbackend.Model.Role.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class GetAllUsersResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private char gender;
    private int age;
    private double weight;
    private int height;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Activity activity;

    public GetAllUsersResponse(Long id, String firstName, String lastName, char gender, int age, double weight, int height, String email, String password, Role role, Activity activity) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.email = email;
        this.password = password;
        this.role = role;
        this.activity = activity;
    }
}

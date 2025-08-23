package com.example.shapebuilderbackend.Dto;

import com.example.shapebuilderbackend.Model.Role.Role;
import lombok.Data;

@Data
public class ChangeUserRole {
    private Long id;
    private Role role;
}

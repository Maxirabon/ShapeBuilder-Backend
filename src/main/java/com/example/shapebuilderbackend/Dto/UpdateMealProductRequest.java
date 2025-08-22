package com.example.shapebuilderbackend.Dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateMealProductRequest {
    private Long id;
    private Long product_id;
    @Min(value = 0, message = "Ilość produktu nie może być mniejsza niż 0")
    private double amount;
}

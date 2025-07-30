package com.example.shapebuilderbackend.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "meal_product", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"meal_id", "product_id"})
})
public class MealProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "meal_id")
    private Meal meal;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private double amount;
}

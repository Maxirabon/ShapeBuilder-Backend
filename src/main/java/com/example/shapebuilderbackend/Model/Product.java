package com.example.shapebuilderbackend.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int protein;
    private int fat;
    private int carbs;
    private int calories;

    private boolean isCustom;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "product")
    private List<MealProduct> usedInMeals = new ArrayList<>();
}

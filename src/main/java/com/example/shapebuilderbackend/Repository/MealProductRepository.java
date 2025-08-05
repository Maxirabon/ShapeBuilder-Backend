package com.example.shapebuilderbackend.Repository;

import com.example.shapebuilderbackend.Model.MealProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealProductRepository extends JpaRepository<MealProduct, Long> {
}

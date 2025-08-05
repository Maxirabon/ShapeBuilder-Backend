package com.example.shapebuilderbackend.Repository;

import com.example.shapebuilderbackend.Model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
}

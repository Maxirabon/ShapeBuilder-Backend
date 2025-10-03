package com.example.shapebuilderbackend.Repository;

import com.example.shapebuilderbackend.Model.MealProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealProductRepository extends JpaRepository<MealProduct, Long> {
    List<MealProduct> findByMealId(Long id);

    boolean existsByMealIdAndProductId(Long id, Long id1);
}

package com.example.shapebuilderbackend.Repository;

import com.example.shapebuilderbackend.Model.MealProduct;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MealProductRepository extends JpaRepository<MealProduct, Long> {
    List<MealProduct> findByMealId(Long id);
    boolean existsByMealIdAndProductId(Long id, Long id1);
    @Modifying
    @Transactional
    @Query("DELETE FROM MealProduct mp WHERE mp.product.id = :productId")
    void deleteAllByProductId(@Param("productId") Long productId);
}

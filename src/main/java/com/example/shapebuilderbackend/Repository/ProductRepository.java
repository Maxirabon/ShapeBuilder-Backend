package com.example.shapebuilderbackend.Repository;

import com.example.shapebuilderbackend.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

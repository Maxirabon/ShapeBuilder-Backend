package com.example.shapebuilderbackend.Service;

import com.example.shapebuilderbackend.Dto.*;
import com.example.shapebuilderbackend.Exception.ConflictException;
import com.example.shapebuilderbackend.Exception.NotFoundException;
import com.example.shapebuilderbackend.Model.Meal;
import com.example.shapebuilderbackend.Model.MealProduct;
import com.example.shapebuilderbackend.Model.Product;
import com.example.shapebuilderbackend.Repository.MealProductRepository;
import com.example.shapebuilderbackend.Repository.MealRepository;
import com.example.shapebuilderbackend.Repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MealProductService {

    @Autowired
    private MealProductRepository mealProductRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private ProductRepository productRepository;


    public DtoMeal addMealProduct(AddMealProductRequest addMealProductRequest) {
        Meal meal = mealRepository.findById(addMealProductRequest.getMeal_id())
                .orElseThrow(() -> new NotFoundException("Meal not found"));

        Product product = productRepository.findById(addMealProductRequest.getProduct_id())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        boolean exists = mealProductRepository.existsByMealIdAndProductId(meal.getId(), product.getId());
        if (exists) {
            throw new ConflictException("Produkt juz znajduje sie w posilku. Usun go lub zmodyfikuj!");
        }

        MealProduct mealProduct = new MealProduct();
        mealProduct.setMeal(meal);
        mealProduct.setProduct(product);
        mealProduct.setAmount(addMealProductRequest.getAmount());
        mealProductRepository.save(mealProduct);

        meal.setMealProducts(mealProductRepository.findByMealId(meal.getId()));
        return new DtoMeal(meal);
    }

    public MealProduct updateMealProduct(UpdateMealProductRequest req) {
        Product product = productRepository.findById(req.getProduct_id())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        MealProduct mealProduct = mealProductRepository.findById(req.getId())
                .orElseThrow(() -> new NotFoundException("MealProduct not found"));

        mealProduct.setProduct(product);
        mealProduct.setAmount(req.getAmount());

        return mealProductRepository.save(mealProduct);
    }
    @Transactional
    public void deleteMealProduct(DeleteMealProductRequest req) {
        MealProduct mp = mealProductRepository.findById(req.getId())
                .orElseThrow(() -> new NotFoundException("MealProduct not found"));

        Meal meal = mp.getMeal();
        if (meal != null) {
            meal.getMealProducts().remove(mp);
        }
    }
}

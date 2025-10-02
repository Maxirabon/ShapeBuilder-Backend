package com.example.shapebuilderbackend.Service;

import com.example.shapebuilderbackend.Dto.AddMealProductRequest;
import com.example.shapebuilderbackend.Dto.DeleteMealProductRequest;
import com.example.shapebuilderbackend.Dto.DtoMeal;
import com.example.shapebuilderbackend.Dto.UpdateMealProductRequest;
import com.example.shapebuilderbackend.Exception.NotFoundException;
import com.example.shapebuilderbackend.Model.Meal;
import com.example.shapebuilderbackend.Model.MealProduct;
import com.example.shapebuilderbackend.Model.Product;
import com.example.shapebuilderbackend.Repository.MealProductRepository;
import com.example.shapebuilderbackend.Repository.MealRepository;
import com.example.shapebuilderbackend.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

        MealProduct mealProduct = new MealProduct();
        mealProduct.setMeal(meal);
        mealProduct.setProduct(product);
        mealProduct.setAmount(addMealProductRequest.getAmount());
        mealProductRepository.save(mealProduct);
        meal.setMealProducts(mealProductRepository.findByMealId(meal.getId()));

        return new DtoMeal(meal);
    }

    public void updateMealProduct(UpdateMealProductRequest updateMealProductRequest) {
        Product product = productRepository.findById(updateMealProductRequest.getProduct_id())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        MealProduct mealProduct = mealProductRepository.findById(updateMealProductRequest.getId())
                .orElseThrow(() -> new NotFoundException("MealProduct not found"));

        mealProduct.setProduct(product);
        mealProduct.setAmount(updateMealProductRequest.getAmount());
        mealProductRepository.save(mealProduct);
    }

    public void deleteMealProduct(DeleteMealProductRequest deleteMealProductRequest) {
        MealProduct mealProduct = mealProductRepository.findById(deleteMealProductRequest.getId())
                .orElseThrow(() -> new NotFoundException("MealProduct not found"));
        mealProductRepository.delete(mealProduct);
    }
}

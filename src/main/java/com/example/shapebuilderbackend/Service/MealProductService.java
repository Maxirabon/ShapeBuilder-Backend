package com.example.shapebuilderbackend.Service;

import com.example.shapebuilderbackend.Dto.AddMealProductRequest;
import com.example.shapebuilderbackend.Exception.NotFoundException;
import com.example.shapebuilderbackend.Model.Meal;
import com.example.shapebuilderbackend.Model.MealProduct;
import com.example.shapebuilderbackend.Model.Product;
import com.example.shapebuilderbackend.Repository.MealProductRepository;
import com.example.shapebuilderbackend.Repository.MealRepository;
import com.example.shapebuilderbackend.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MealProductService {

    @Autowired
    private MealProductRepository mealProductRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private ProductRepository productRepository;


    public void addMealProduct(AddMealProductRequest addMealProductRequest) {
        Meal meal = mealRepository.findById(addMealProductRequest.getMeal_id())
                .orElseThrow(() -> new NotFoundException("Meal not found"));

        Product product = productRepository.findById(addMealProductRequest.getProduct_id())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        MealProduct mealProduct = new MealProduct();
        mealProduct.setMeal(meal);
        mealProduct.setProduct(product);
        mealProduct.setAmount(addMealProductRequest.getAmount());
        mealProductRepository.save(mealProduct);
    }

}

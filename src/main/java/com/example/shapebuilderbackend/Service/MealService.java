package com.example.shapebuilderbackend.Service;

import com.example.shapebuilderbackend.Dto.DtoMealSummary;
import com.example.shapebuilderbackend.Dto.DtoProductSummary;
import com.example.shapebuilderbackend.Model.Meal;
import com.example.shapebuilderbackend.Model.MealProduct;
import com.example.shapebuilderbackend.Repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

@Service
public class MealService {

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private ProductService productService;

    public DtoMealSummary calculateMealSummary(Meal meal) {
        List<DtoProductSummary> productSummaries = new ArrayList<>();
        double protein = 0, fat = 0, carbs = 0, calories = 0;

        for (MealProduct mp : meal.getMealProducts()) {
            DtoProductSummary ps = productService.calculateProductSummary(mp);
            productSummaries.add(ps);

            protein += ps.getProtein();
            fat += ps.getFat();
            carbs += ps.getCarbs();
            calories += ps.getCalories();
        }

        return new DtoMealSummary(
                meal.getDescription(),
                productSummaries,
                round(protein),
                round(fat),
                round(carbs),
                round(calories)
        );
    }
}

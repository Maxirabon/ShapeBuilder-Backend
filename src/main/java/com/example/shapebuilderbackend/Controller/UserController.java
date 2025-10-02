package com.example.shapebuilderbackend.Controller;

import com.example.shapebuilderbackend.Dto.*;
import com.example.shapebuilderbackend.Exception.NotFoundException;
import com.example.shapebuilderbackend.Model.Meal;
import com.example.shapebuilderbackend.Model.MealProduct;
import com.example.shapebuilderbackend.Repository.MealProductRepository;
import com.example.shapebuilderbackend.Repository.MealRepository;
import com.example.shapebuilderbackend.Service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private ExerciseTemplateService exerciseTemplateService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AppService appService;

    @Autowired
    private MealProductService mealProductService;

    @Autowired
    private MealProductRepository mealProductRepository;

    @Autowired
    private MealRepository mealRepository;
    @Autowired
    private MealService mealService;
    @Autowired
    private CalendarService calendarService;

    @PutMapping("/updateProfile")
    public ResponseEntity<?> updateUserProfile(@RequestBody UpdateProfileRequest updateProfileRequest) {
        userService.updateProfile(updateProfileRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changeUserPassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAllExerciseTemplates")
    public ResponseEntity<?> getAllExerciseTemplates() {
        return ResponseEntity.ok(exerciseTemplateService.getAllExerciseTemplates());
    }

    @PostMapping("/addExercise")
    public ResponseEntity<?> addUserExerciseToDay(@Valid @RequestBody AddExerciseRequest addExerciseRequest) {
        exerciseService.addExercise(addExerciseRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/updateExercise")
    public ResponseEntity<?> updateUserExercise(@Valid @RequestBody UpdateExerciseRequest updateExerciseRequest) {
        exerciseService.updateExercise(updateExerciseRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteExercise")
    public ResponseEntity<?> deleteUserExercise(@RequestBody DeleteExerciseRequest deleteExerciseRequest) {
        exerciseService.deleteExercise(deleteExerciseRequest);
        return ResponseEntity.ok().body("Ćwiczenie zostało usunięte.");
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/getCaloricRequisition")
    public ResponseEntity<?> getCaloricRequisition() { return ResponseEntity.ok(appService.calculateCaloricRequisition());}

    @PostMapping("/addMealProduct")
    public ResponseEntity<DtoMeal> addMealProduct(@Valid @RequestBody AddMealProductRequest request) {
        DtoMeal updatedMeal = mealProductService.addMealProduct(request);
        return ResponseEntity.ok(updatedMeal);
    }

    @PutMapping("/updateMealProduct")
    public ResponseEntity<?> updateMealProduct(@Valid @RequestBody UpdateMealProductRequest updateMealProductRequest) {
        mealProductService.updateMealProduct(updateMealProductRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteMealProduct")
    public ResponseEntity<?> deleteMealProduct(@RequestBody DeleteMealProductRequest deleteMealProductRequest) {
        mealProductService.deleteMealProduct(deleteMealProductRequest);
        return ResponseEntity.ok().body("Posiłek został usunięty.");
    }

    @GetMapping("/getProductSummary/{id}")
   public DtoProductSummary getProductSummary(@PathVariable Long id) {
        MealProduct mealProduct = mealProductRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono produktu o id: " + id));
        return productService.calculateProductSummary(mealProduct);
    }

    @GetMapping("/getMealSummary/{id}")
    public DtoMealSummary getMealSummary(@PathVariable Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono posiłku o id: " + id));
        return mealService.calculateMealSummary(meal);
    }

    @GetMapping("/getDaySummary/{id}")
    public DtoDaySummary getDaySummary(@PathVariable Long id) {
        return calendarService.getDaySummary(id);
    }

    @GetMapping("/getDayExerciseSummary/{id}")
    public DtoDayExerciseSummary getDayExerciseSummary(@PathVariable Long id) {
        return calendarService.getDayExerciseSummary(id);
    }

    @PostMapping("/addUserProduct")
    public ResponseEntity<?> addUserProduct(@Valid @RequestBody AddUserProductRequest addUserProductRequest) {
        userService.addUserProduct(addUserProductRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getUserProducts")
    public List<GetAllUserProducts> getUserProducts() {
        return userService.getAllUserProducts();
    }

    @PutMapping("/updateUserProduct")
    public ResponseEntity<?> updateUserProduct(@Valid @RequestBody UpdateUserProductRequest updateUserProductRequest) {
        userService.updateUserProduct(updateUserProductRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteUserProduct")
    public ResponseEntity<?> deleteUserProduct(@RequestBody DeleteUserProductRequest deleteUserProductRequest) {
        userService.deleteUserProduct(deleteUserProductRequest);
        return ResponseEntity.ok().body("Posiłek został usunięty.");
    }

    @GetMapping("/getUserDays")
    public ResponseEntity<?> getUserDays() {
        List<GetAllUserDays> days = userService.getAllUserDays();
        return ResponseEntity.ok(days);
    }


}

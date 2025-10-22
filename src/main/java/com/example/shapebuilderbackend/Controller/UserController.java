package com.example.shapebuilderbackend.Controller;

import com.example.shapebuilderbackend.Dto.*;
import com.example.shapebuilderbackend.Exception.BadRequestException;
import com.example.shapebuilderbackend.Exception.NotFoundException;
import com.example.shapebuilderbackend.Model.Exercise;
import com.example.shapebuilderbackend.Model.Meal;
import com.example.shapebuilderbackend.Model.MealProduct;
import com.example.shapebuilderbackend.Repository.MealProductRepository;
import com.example.shapebuilderbackend.Repository.MealRepository;
import com.example.shapebuilderbackend.Service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/getUserInfo")
    public ResponseEntity<DtoGetUserInfo> getUserInfo(){
        DtoGetUserInfo info = userService.getUserInfo();
        return ResponseEntity.ok(info);
    }

    @PutMapping("/updateProfile")
    public ResponseEntity<?> updateUserProfile(@RequestBody UpdateProfileRequest updateProfileRequest) {
        userService.updateProfile(updateProfileRequest);
        return ResponseEntity.ok(Map.of("message", "Profil zaktualizowany"));
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changeUserPassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            userService.changePassword(changePasswordRequest);
            return ResponseEntity.ok(Map.of("message", "Hasło zostało zmienione."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/getAllExerciseTemplates")
    public ResponseEntity<?> getAllExerciseTemplates() {
        return ResponseEntity.ok(exerciseTemplateService.getAllExerciseTemplates());
    }

    @PostMapping("/addExercise")
    public ResponseEntity<DtoAddExerciseResponse> addUserExerciseToDay(@Valid @RequestBody AddExerciseRequest addExerciseRequest) {
        DtoAddExerciseResponse newExercise = exerciseService.addExercise(addExerciseRequest);
        return ResponseEntity.ok(newExercise);
    }

    @PutMapping("/updateExercise")
    public ResponseEntity<Exercise> updateUserExercise(@Valid @RequestBody UpdateExerciseRequest updateExerciseRequest) {
        Exercise updated = exerciseService.updateExercise(updateExerciseRequest);
        return ResponseEntity.ok(updated);
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
    public ResponseEntity<DtoMealProduct> updateMealProduct(@Valid @RequestBody UpdateMealProductRequest updateMealProductRequest) {
        MealProduct updated = mealProductService.updateMealProduct(updateMealProductRequest);
        double ratio = updated.getAmount() / 100.0;

        DtoMealProduct dto = new DtoMealProduct(
                updated.getId(),
                updated.getProduct().getId(),
                updated.getProduct().getName(),
                updated.getProduct().getCalories() * ratio,
                updated.getProduct().getProtein() * ratio,
                updated.getProduct().getCarbs() * ratio,
                updated.getProduct().getFat() * ratio,
                updated.getAmount()
        );
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/deleteMealProduct")
    public ResponseEntity<Map<String, Object>> deleteMealProduct(@RequestParam Long id) {
        mealProductService.deleteMealProduct(new DeleteMealProductRequest(id));

        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("message", "Posiłek został usunięty");
        return ResponseEntity.ok(response);
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

    @GetMapping("/getWeekSummary/{userId}")
    public DtoWeekSummary getWeekSummary(@PathVariable Long userId, @RequestParam(required = false) String startOfWeek, @RequestParam(required = false) String endOfWeek) {
        LocalDate start = (startOfWeek != null) ? LocalDate.parse(startOfWeek) : null;
        LocalDate end = (endOfWeek != null) ? LocalDate.parse(endOfWeek) : null;
        return calendarService.getWeekSummary(userId, start, end);
    }

    @GetMapping("/getMonthSummary/{userId}/{year}/{month}")
    public DtoMonthSummary getMonthSummary(@PathVariable Long userId, @PathVariable(required = false) Integer year, @PathVariable(required = false) Integer month) {
        if (year < 2025 || year > LocalDate.now().getYear()) {
            throw new BadRequestException("Niepoprawny rok");
        }
        if (month < 1 || month > 12) {
            throw new BadRequestException("Niepoprawny miesiąc");
        }
        return calendarService.getMonthSummary(userId, year, month);
    }

    @GetMapping("/getDayExerciseSummary/{id}")
    public DtoDayExerciseSummary getDayExerciseSummary(@PathVariable Long id) {
        return calendarService.getDayExerciseSummary(id);
    }

    @GetMapping("/getWeekExerciseSummary/{userId}")
    public ResponseEntity<DtoPeriodExerciseSummary> getWeekExerciseSummary(@PathVariable Long userId, @RequestParam(required = false) String startOfWeek, @RequestParam(required = false) String endOfWeek) {
        LocalDate start = (startOfWeek != null) ? LocalDate.parse(startOfWeek) : null;
        LocalDate end = (endOfWeek != null) ? LocalDate.parse(endOfWeek) : null;

        DtoPeriodExerciseSummary summary = calendarService.getWeekExerciseSummary(userId, start, end);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/getMonthExerciseSummary/{userId}/{year}/{month}")
    public ResponseEntity<DtoPeriodExerciseSummary> getMonthExerciseSummary(@PathVariable Long userId, @PathVariable int year, @PathVariable int month) {
        if (year < 2025 || year > LocalDate.now().getYear()) {
            throw new BadRequestException("Niepoprawny rok");
        }
        if (month < 1 || month > 12) {
            throw new BadRequestException("Niepoprawny miesiąc");
        }
        DtoPeriodExerciseSummary summary = calendarService.getMonthExerciseSummary(userId, year, month);
        return ResponseEntity.ok(summary);
    }

    @PostMapping("/addUserProduct")
    public ResponseEntity<AddUserProductRequest> addUserProduct(@Valid @RequestBody AddUserProductRequest addUserProductRequest) {
        AddUserProductRequest newUserProduct = userService.addUserProduct(addUserProductRequest);
        return ResponseEntity.ok(newUserProduct);
    }

    @GetMapping("/getUserProducts")
    public List<GetAllUserProducts> getUserProducts() {
        return userService.getAllUserProducts();
    }

    @PutMapping("/updateUserProduct")
    public ResponseEntity<UpdateUserProductRequest> updateUserProduct(@Valid @RequestBody UpdateUserProductRequest updateUserProductRequest) {
        UpdateUserProductRequest updated = userService.updateUserProduct(updateUserProductRequest);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/deleteUserProduct/{id}")
    public ResponseEntity<?> deleteUserProduct(@PathVariable Long id) {
        userService.deleteUserProduct(id);
        return ResponseEntity.ok().body("Produkt został usunięty.");
    }

    @GetMapping("/getUserDays")
    public ResponseEntity<?> getUserDays() {
        List<GetAllUserDays> days = userService.getAllUserDays();
        return ResponseEntity.ok(days);
    }


}

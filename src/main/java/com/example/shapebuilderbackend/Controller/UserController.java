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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User", description = "Operacje dostępne dla zalogowanego użytkownika")
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
    @Operation(summary = "Pobierz informacje o użytkowniku", description = "Zwraca dane profilu zalogowanego użytkownika")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dane użytkownika pobrane pomyślnie"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp - brak tokena")
    })
    public ResponseEntity<DtoGetUserInfo> getUserInfo(){
        DtoGetUserInfo info = userService.getUserInfo();
        return ResponseEntity.ok(info);
    }

    @PutMapping("/updateProfile")
    @Operation(summary = "Aktualizacja profilu użytkownika", description = "Aktualizuje dane takie jak wiek, wzrost, waga i aktywność")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profil zaktualizowany"),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane w żądaniu"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp")
    })
    public ResponseEntity<?> updateUserProfile(@RequestBody UpdateProfileRequest updateProfileRequest) {
        userService.updateProfile(updateProfileRequest);
        return ResponseEntity.ok(Map.of("message", "Profil zaktualizowany"));
    }

    @GetMapping("/weight-history")
    @Operation(summary = "Pobranie historii wagi użytkownika", description = "Zwraca listę wszystkich zmian wagi użytkownika wraz z datami ich zapisania. " + "Historia jest uporządkowana malejąco, od najnowszych do najstarszych wpisów.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Zwrócono historię wagi użytkownika"),
            @ApiResponse(responseCode = "401", description = "Użytkownik niezalogowany lub token nieważny")
    })
    public ResponseEntity<List<DtoWeightHistory>> getWeightHistory() {
        return ResponseEntity.ok(userService.getWeightHistory());
    }

    @PutMapping("/changePassword")
    @Operation(summary = "Zmiana hasła użytkownika", description = "Pozwala użytkownikowi zmienić hasło")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hasło zostało zmienione"),
            @ApiResponse(responseCode = "400", description = "Niepoprawne stare hasło lub nowe hasło jest takie samo"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp")
    })
    public ResponseEntity<?> changeUserPassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            userService.changePassword(changePasswordRequest);
            return ResponseEntity.ok(Map.of("message", "Hasło zostało zmienione."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/getAllExerciseTemplates")
    @Operation(summary = "Pobierz wszystkie szablony ćwiczeń", description = "Zwraca listę wszystkich dostępnych szablonów ćwiczeń")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista szablonów ćwiczeń"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp")
    })
    public ResponseEntity<?> getAllExerciseTemplates() {
        return ResponseEntity.ok(exerciseTemplateService.getAllExerciseTemplates());
    }

    @PostMapping("/addExercise")
    @Operation(summary = "Dodaj ćwiczenie do dnia użytkownika", description = "Dodaje nowe ćwiczenie do określonego dnia użytkownika")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ćwiczenie dodane"),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane w żądaniu"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono użytkownika/dnia/szablonu"),
            @ApiResponse(responseCode = "409", description = "Ćwiczenie już istnieje w tym dniu")
    })
    public ResponseEntity<DtoAddExerciseResponse> addUserExerciseToDay(@Valid @RequestBody AddExerciseRequest addExerciseRequest) {
        DtoAddExerciseResponse newExercise = exerciseService.addExercise(addExerciseRequest);
        return ResponseEntity.ok(newExercise);
    }

    @PutMapping("/updateExercise")
    @Operation(summary = "Aktualizacja ćwiczenia użytkownika", description = "Aktualizuje dane ćwiczenia użytkownika")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ćwiczenie zaktualizowane"),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono ćwiczenia")
    })
    public ResponseEntity<Exercise> updateUserExercise(@Valid @RequestBody UpdateExerciseRequest updateExerciseRequest) {
        Exercise updated = exerciseService.updateExercise(updateExerciseRequest);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/deleteExercise")
    @Operation(summary = "Usuń ćwiczenie użytkownika", description = "Usuwa ćwiczenie z określonego dnia użytkownika")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ćwiczenie usunięte"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono ćwiczenia")
    })
    public ResponseEntity<?> deleteUserExercise(@RequestBody DeleteExerciseRequest deleteExerciseRequest) {
        exerciseService.deleteExercise(deleteExerciseRequest);
        return ResponseEntity.ok().body("Ćwiczenie zostało usunięte.");
    }

    @GetMapping("/getAllProducts")
    @Operation(summary = "Pobierz wszystkie produkty", description = "Zwraca listę wszystkich produktów użytkownika")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista produktów"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp")
    })
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/getCaloricRequisition")
    @Operation(summary = "Oblicz zapotrzebowanie kaloryczne", description = "Zwraca zalecane dzienne zapotrzebowanie kaloryczne użytkownika")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Obliczono zapotrzebowanie"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp")
    })
    public ResponseEntity<?> getCaloricRequisition() { return ResponseEntity.ok(appService.calculateCaloricRequisition());}

    @PostMapping("/addMealProduct")
    @Operation(summary = "Dodaj produkt do posiłku", description = "Dodaje produkt do konkretnego posiłku użytkownika")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produkt dodany do posiłku"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono posiłku lub produktu"),
            @ApiResponse(responseCode = "409", description = "Produkt już istnieje w posiłku")
    })
    public ResponseEntity<DtoMeal> addMealProduct(@Valid @RequestBody AddMealProductRequest request) {
        DtoMeal updatedMeal = mealProductService.addMealProduct(request);
        return ResponseEntity.ok(updatedMeal);
    }

    @PutMapping("/updateMealProduct")
    @Operation(summary = "Aktualizacja produktu w posiłku", description = "Aktualizuje ilość lub produkt w posiłku użytkownika")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produkt w posiłku zaktualizowany"),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane w żądaniu"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono produktu lub posiłku")
    })
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
    @Operation(summary = "Usuń produkt z posiłku", description = "Usuwa wybrany produkt z posiłku użytkownika")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produkt usunięty z posiłku"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono produktu w posiłku")
    })
    public ResponseEntity<Map<String, Object>> deleteMealProduct(@RequestParam Long id) {
        mealProductService.deleteMealProduct(new DeleteMealProductRequest(id));

        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("message", "Posiłek został usunięty");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getProductSummary/{id}")
    @Operation(summary = "Podsumowanie produktu", description = "Zwraca podsumowanie wartości odżywczych wybranego produktu")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Podsumowanie produktu"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono produktu")
    })
   public DtoProductSummary getProductSummary(@PathVariable Long id) {
        MealProduct mealProduct = mealProductRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono produktu o id: " + id));
        return productService.calculateProductSummary(mealProduct);
    }

    @GetMapping("/getMealSummary/{id}")
    @Operation(summary = "Podsumowanie posiłku", description = "Zwraca podsumowanie wartości odżywczych posiłku")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Podsumowanie posiłku"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono posiłku")
    })
    public DtoMealSummary getMealSummary(@PathVariable Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono posiłku o id: " + id));
        return mealService.calculateMealSummary(meal);
    }

    @GetMapping("/getDaySummary/{id}")
    @Operation(summary = "Podsumowanie żywieniowe dnia użytkownika", description = "Zwraca podsumowanie dnia użytkownika wraz z posiłkami")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Podsumowanie dnia"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono dnia")
    })
    public DtoDaySummary getDaySummary(@PathVariable Long id) {
        return calendarService.getDaySummary(id);
    }

    @GetMapping("/getWeekSummary/{userId}")
    @Operation(summary = "Podsumowanie żywieniowe tygodnia użytkownika", description = "Zwraca podsumowanie żywieniowe tygodnia dla użytkownika, z możliwością podania zakresu dat")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Podsumowanie tygodnia"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp")
    })
    public DtoWeekSummary getWeekSummary(@PathVariable Long userId, @RequestParam(required = false) String startOfWeek, @RequestParam(required = false) String endOfWeek) {
        LocalDate start = (startOfWeek != null) ? LocalDate.parse(startOfWeek) : null;
        LocalDate end = (endOfWeek != null) ? LocalDate.parse(endOfWeek) : null;
        return calendarService.getWeekSummary(userId, start, end);
    }

    @GetMapping("/getMonthSummary/{userId}/{year}/{month}")
    @Operation(summary = "Podsumowanie żywieniowe miesiąca użytkownika", description = "Zwraca pełne żywieniowe podsumowanie miesiąca dla użytkownika")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Podsumowanie miesiąca"),
            @ApiResponse(responseCode = "400", description = "Niepoprawny rok lub miesiąc"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp")
    })
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
    @Operation(summary = "Podsumowanie ćwiczeń dnia", description = "Zwraca podsumowanie wszystkich ćwiczeń użytkownika w danym dniu")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Podsumowanie ćwiczeń dnia"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp")
    })
    public DtoDayExerciseSummary getDayExerciseSummary(@PathVariable Long id) {
        return calendarService.getDayExerciseSummary(id);
    }

    @GetMapping("/getWeekExerciseSummary/{userId}")
    @Operation(summary = "Podsumowanie ćwiczeń tygodnia", description = "Zwraca podsumowanie ćwiczeń użytkownika w danym tygodniu")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Podsumowanie ćwiczeń tygodnia"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp")
    })
    public ResponseEntity<DtoPeriodExerciseSummary> getWeekExerciseSummary(@PathVariable Long userId, @RequestParam(required = false) String startOfWeek, @RequestParam(required = false) String endOfWeek) {
        LocalDate start = (startOfWeek != null) ? LocalDate.parse(startOfWeek) : null;
        LocalDate end = (endOfWeek != null) ? LocalDate.parse(endOfWeek) : null;

        DtoPeriodExerciseSummary summary = calendarService.getWeekExerciseSummary(userId, start, end);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/getMonthExerciseSummary/{userId}/{year}/{month}")
    @Operation(summary = "Podsumowanie ćwiczeń miesiąca", description = "Zwraca podsumowanie ćwiczeń użytkownika w danym miesiącu")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Podsumowanie ćwiczeń miesiąca"),
            @ApiResponse(responseCode = "400", description = "Niepoprawny rok lub miesiąc"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp")
    })
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
    @Operation(summary = "Dodaj własny produkt użytkownika", description = "Pozwala użytkownikowi dodać własny produkt")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produkt dodany"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp")
    })
    public ResponseEntity<AddUserProductRequest> addUserProduct(@Valid @RequestBody AddUserProductRequest addUserProductRequest) {
        AddUserProductRequest newUserProduct = userService.addUserProduct(addUserProductRequest);
        return ResponseEntity.ok(newUserProduct);
    }

    @GetMapping("/getUserProducts")
    @Operation(summary = "Pobierz wszystkie produkty użytkownika", description = "Zwraca listę wszystkich produktów utworzonych przez użytkownika")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista produktów"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp")
    })
    public List<GetAllUserProducts> getUserProducts() {
        return userService.getAllUserProducts();
    }

    @PutMapping("/updateUserProduct")
    @Operation(summary = "Aktualizacja własnego produktu", description = "Pozwala użytkownikowi zaktualizować własny produkt")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produkt zaktualizowany"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono produktu"),
            @ApiResponse(responseCode = "403", description = "Brak dostępu do produktu")
    })
    public ResponseEntity<UpdateUserProductRequest> updateUserProduct(@Valid @RequestBody UpdateUserProductRequest updateUserProductRequest) {
        UpdateUserProductRequest updated = userService.updateUserProduct(updateUserProductRequest);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/deleteUserProduct/{id}")
    @Operation(summary = "Usuń własny produkt", description = "Pozwala użytkownikowi usunąć własny produkt")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produkt usunięty"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono produktu"),
            @ApiResponse(responseCode = "403", description = "Brak dostępu do produktu")
    })
    public ResponseEntity<?> deleteUserProduct(@PathVariable Long id) {
        userService.deleteUserProduct(id);
        return ResponseEntity.ok().body("Produkt został usunięty.");
    }

    @GetMapping("/getUserDays")
    @Operation(summary = "Pobierz wszystkie dni użytkownika", description = "Zwraca wszystkie dni użytkownika z posiłkami i ćwiczeniami")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista dni użytkownika"),
            @ApiResponse(responseCode = "401", description = "Nieautoryzowany dostęp")
    })
    public ResponseEntity<?> getUserDays() {
        List<GetAllUserDays> days = userService.getAllUserDays();
        return ResponseEntity.ok(days);
    }


}

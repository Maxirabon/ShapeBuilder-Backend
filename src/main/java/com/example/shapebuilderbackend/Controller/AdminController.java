package com.example.shapebuilderbackend.Controller;

import com.example.shapebuilderbackend.Dto.*;
import com.example.shapebuilderbackend.Repository.ExerciseTemplateRepository;
import com.example.shapebuilderbackend.Service.ExerciseTemplateService;
import com.example.shapebuilderbackend.Service.ProductService;
import com.example.shapebuilderbackend.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Endpointy administracyjne")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ExerciseTemplateService exerciseTemplateService;

    @GetMapping("/getAllUsersProfile")
    @Operation(summary = "Pobierz wszystkich użytkowników", description = "Zwraca listę wszystkich profili użytkowników")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista użytkowników zwrócona pomyślnie"),
            @ApiResponse(responseCode = "403", description = "Brak uprawnień do wyświetlenia profili użytkowników")
    })
    public List<GetAllUsersResponse> getAllUsersProfile() {
        return ResponseEntity.ok(userService.getAllUsers()).getBody();
    }

    @PutMapping("/changeUserRole")
    @Operation(summary = "Zmień rolę użytkownika", description = "Aktualizuje rolę użytkownika na podstawie przesłanych danych")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rola użytkownika została zmieniona"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono użytkownika o podanym ID"),
            @ApiResponse(responseCode = "403", description = "Brak uprawnień do zmiany roli użytkownika")
    })
    public ResponseEntity<?>  changeUserRole(@RequestBody ChangeUserRole changeUserRole) {
        userService.changeUseRole(changeUserRole);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteUser")
    @Operation(summary = "Usuń użytkownika", description = "Usuwa użytkownika na podstawie przesłanych danych")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Użytkownik został usunięty"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono użytkownika o podanym ID"),
            @ApiResponse(responseCode = "403", description = "Brak uprawnień do usunięcia użytkownika")
    })
    public ResponseEntity<?> deleteUser(@RequestBody DeleteUserRequest deleteUserRequest) {
        userService.deleteUser(deleteUserRequest);
        return ResponseEntity.ok().body("Uzytkownik zostal usuniety.");
    }

    @PostMapping("/addProduct")
    @Operation(summary = "Dodaj nowy produkt", description = "Pozwala administratorowi dodać nowy produkt do bazy danych")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produkt został dodany pomyślnie"),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane produktu"),
            @ApiResponse(responseCode = "403", description = "Brak uprawnień do dodania produktu")
    })
    public ResponseEntity<AddProductResponse> addProduct(@RequestBody AddProductRequest addProductRequest) {
        return ResponseEntity.ok(productService.addProduct(addProductRequest));
    }

    @PutMapping("/updateProduct")
    @Operation(summary = "Zaktualizuj produkt", description = "Pozwala administratorowi zaktualizować dane istniejącego produktu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produkt został zaktualizowany pomyślnie"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono produktu o podanym ID"),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane produktu"),
            @ApiResponse(responseCode = "403", description = "Brak uprawnień do modyfikacji produktu")
    })
    public ResponseEntity<DtoUpdateProduct> updateProduct(@RequestBody DtoUpdateProduct updateProduct) {
        return ResponseEntity.ok(productService.updateProduct(updateProduct));
    }

    @DeleteMapping("/deleteProduct/{id}")
    @Operation(summary = "Usuń produkt", description = "Usuwa produkt z bazy danych na podstawie jego identyfikatora")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produkt został usunięty pomyślnie"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono produktu o podanym ID"),
            @ApiResponse(responseCode = "403", description = "Brak uprawnień do usunięcia produktu")
    })
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Produkt został usunięty");
    }

    @PostMapping("/addExerciseTemplate")
    @Operation(summary = "Dodaj nowy szablon ćwiczenia", description = "Pozwala administratorowi dodać nowy szablon ćwiczenia do bazy danych")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Szablon ćwiczenia został dodany pomyślnie"),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane szablonu ćwiczenia"),
            @ApiResponse(responseCode = "403", description = "Brak uprawnień do dodania szablonu ćwiczenia")
    })
    public ResponseEntity<AddExerciseTemplateResponse> addExerciseTemplate(@RequestBody AddExerciseTemplateRequest request) {
        return ResponseEntity.ok(exerciseTemplateService.addExerciseTemplate(request));
    }

    @PutMapping("/updateExerciseTemplate")
    @Operation(summary = "Zaktualizuj szablon ćwiczenia", description = "Pozwala administratorowi zaktualizować nazwę istniejącego szablonu ćwiczenia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Szablon ćwiczenia został zaktualizowany pomyślnie"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono szablonu ćwiczenia o podanym ID"),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane szablonu ćwiczenia"),
            @ApiResponse(responseCode = "403", description = "Brak uprawnień do modyfikacji szablonu ćwiczenia")
    })
    public ResponseEntity<AddExerciseTemplateResponse> updateExerciseTemplate(@RequestBody UpdateExerciseTemplateRequest request) {
        return ResponseEntity.ok(exerciseTemplateService.updateExerciseTemplate(request));
    }

    @DeleteMapping("/deleteExerciseTemplate/{id}")
    @Operation(summary = "Usuń szablon ćwiczenia", description = "Usuwa szablon ćwiczenia z bazy danych na podstawie jego identyfikatora")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Szablon ćwiczenia został usunięty pomyślnie"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono szablonu ćwiczenia o podanym ID"),
            @ApiResponse(responseCode = "403", description = "Brak uprawnień do usunięcia szablonu ćwiczenia")
    })
    public ResponseEntity<?> deleteExerciseTemplate(@PathVariable Long id) {
        exerciseTemplateService.deleteExerciseTemplate(id);
        return ResponseEntity.ok("Szablon ćwiczenia został usunięty.");
    }
}

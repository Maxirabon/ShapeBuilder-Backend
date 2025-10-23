package com.example.shapebuilderbackend.Controller;

import com.example.shapebuilderbackend.Dto.LoginRequest;
import com.example.shapebuilderbackend.Dto.RegisterRequest;
import com.example.shapebuilderbackend.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Auth", description = "Rejestracja i logowanie użytkowników")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Rejestracja użytkownika", description = "Tworzy nowe konto użytkownika na podstawie przesłanych danych")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Konto zostało utworzone pomyślnie"),
            @ApiResponse(responseCode = "409", description = "Użytkownik o podanym emailu już istnieje"),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane rejestracyjne")
    })
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        userService.createUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED) .body(Map.of("message", "Konto utworzone pomyślnie"));
    }

    @PostMapping("/login")
    @Operation(summary = "Logowanie użytkownika", description = "Zwraca token JWT po poprawnym zalogowaniu")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logowanie udane"),
            @ApiResponse(responseCode = "404", description = "Użytkownik o podanym emailu nie istnieje"),
            @ApiResponse(responseCode = "401", description = "Nieprawidłowe hasło")
    })
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        String token = userService.loginUser(loginRequest);
        return ResponseEntity.ok(Map.of("token", token));
    }
}

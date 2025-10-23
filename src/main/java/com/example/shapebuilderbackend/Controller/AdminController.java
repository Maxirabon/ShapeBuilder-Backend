package com.example.shapebuilderbackend.Controller;

import com.example.shapebuilderbackend.Dto.ChangeUserRole;
import com.example.shapebuilderbackend.Dto.DeleteUserRequest;
import com.example.shapebuilderbackend.Dto.GetAllUsersResponse;
import com.example.shapebuilderbackend.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
}

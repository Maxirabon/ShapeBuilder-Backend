package com.example.shapebuilderbackend.Controller;

import com.example.shapebuilderbackend.Dto.ChangeUserRole;
import com.example.shapebuilderbackend.Dto.DeleteUserRequest;
import com.example.shapebuilderbackend.Dto.GetAllUsersResponse;
import com.example.shapebuilderbackend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/getAllUsersProfile")
    public List<GetAllUsersResponse> getAllUsersProfile() {
        return ResponseEntity.ok(userService.getAllUsers()).getBody();
    }

    @PutMapping("/changeUserRole")
    public ResponseEntity<?>  changeUserRole(@RequestBody ChangeUserRole changeUserRole) {
        userService.changeUseRole(changeUserRole);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestBody DeleteUserRequest deleteUserRequest) {
        userService.deleteUser(deleteUserRequest);
        return ResponseEntity.ok().body("Uzytkownik zostal usuniety.");
    }


}

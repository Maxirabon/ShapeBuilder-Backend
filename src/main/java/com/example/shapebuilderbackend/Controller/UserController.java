package com.example.shapebuilderbackend.Controller;

import com.example.shapebuilderbackend.Dto.AddExerciseRequest;
import com.example.shapebuilderbackend.Dto.ChangePasswordRequest;
import com.example.shapebuilderbackend.Dto.UpdateProfileRequest;
import com.example.shapebuilderbackend.Service.ExerciseService;
import com.example.shapebuilderbackend.Service.ExerciseTemplateService;
import com.example.shapebuilderbackend.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ExerciseService exerciseService;

    @Autowired
    ExerciseTemplateService exerciseTemplateService;

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

}

package com.example.shapebuilderbackend.Controller;

import com.example.shapebuilderbackend.Dto.GetAllUsersResponse;
import com.example.shapebuilderbackend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}

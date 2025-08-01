package com.example.shapebuilderbackend.Service;

import com.example.shapebuilderbackend.Dto.ChangePasswordRequest;
import com.example.shapebuilderbackend.Dto.LoginRequest;
import com.example.shapebuilderbackend.Dto.RegisterRequest;
import com.example.shapebuilderbackend.Dto.UpdateProfileRequest;
import com.example.shapebuilderbackend.Exception.ConflictException;
import com.example.shapebuilderbackend.Exception.NotFoundException;
import com.example.shapebuilderbackend.Exception.UnauthorizedException;
import com.example.shapebuilderbackend.Model.Role.Role;
import com.example.shapebuilderbackend.Model.User;
import com.example.shapebuilderbackend.Repository.UserRepository;
import com.example.shapebuilderbackend.Security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public UserService(UserRepository userRepository,  PasswordEncoder passwordEncoder,  JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void createUser(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException("Użytkownik o podanym emailu już istnieje");
        }
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setRole(Role.valueOf("ROLE_USER"));
        user.setGender(registerRequest.getGender());
        user.setAge(registerRequest.getAge());
        user.setWeight(registerRequest.getWeight());
        user.setHeight(registerRequest.getHeight());

        userRepository.save(user);
    }

    public String loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("Użytkownik o podanym emailu nie istnieje"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }
        return jwtService.generateToken(user);
    }

    public void updateProfile(UpdateProfileRequest updateProfileRequest) {
        User user = getCurrentUser();
        user.setAge(updateProfileRequest.getAge());
        user.setHeight(updateProfileRequest.getHeight());
        user.setWeight(updateProfileRequest.getWeight());
        userRepository.save(user);
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = getCurrentUser();
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }
}

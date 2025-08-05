package com.example.shapebuilderbackend.Service;

import com.example.shapebuilderbackend.Dto.ChangePasswordRequest;
import com.example.shapebuilderbackend.Dto.LoginRequest;
import com.example.shapebuilderbackend.Dto.RegisterRequest;
import com.example.shapebuilderbackend.Dto.UpdateProfileRequest;
import com.example.shapebuilderbackend.Exception.ConflictException;
import com.example.shapebuilderbackend.Exception.NotFoundException;
import com.example.shapebuilderbackend.Exception.UnauthorizedException;
import com.example.shapebuilderbackend.Model.Activity.Activity;
import com.example.shapebuilderbackend.Model.Role.Role;
import com.example.shapebuilderbackend.Model.User;
import com.example.shapebuilderbackend.Repository.UserRepository;
import com.example.shapebuilderbackend.Security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CalendarService calendarService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, CalendarService calendarService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.calendarService = calendarService;
    }

    public void createUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException("Użytkownik o podanym emailu już istnieje");
        }

        Activity activity;
        try {
            activity = Activity.valueOf(registerRequest.getActivity().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Nieprawidłowa wartość aktywności: " + registerRequest.getActivity());
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
        user.setActivity(activity);
        calendarService.generateDaysForUser(user);

        userRepository.save(user);
    }

    public String loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("Użytkownik o podanym emailu nie istnieje"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Nieprawidłowe hasło");
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

    public double getUserPAL(){
        User user = getCurrentUser();
        String activityLevel = String.valueOf(user.getActivity());
        return switch (activityLevel) {
            case "BRAK" -> 1.2;
            case "MALA" -> 1.375;
            case "SREDNIA" -> 1.55;
            case "DUZA" -> 1.725;
            case "BARDZO_DUZA" -> 1.9;
            default -> 1.2;
        };
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }
}

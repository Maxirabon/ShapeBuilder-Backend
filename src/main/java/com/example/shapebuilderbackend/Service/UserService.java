package com.example.shapebuilderbackend.Service;

import com.example.shapebuilderbackend.Dto.*;
import com.example.shapebuilderbackend.Exception.ConflictException;
import com.example.shapebuilderbackend.Exception.ForbiddenException;
import com.example.shapebuilderbackend.Exception.NotFoundException;
import com.example.shapebuilderbackend.Exception.UnauthorizedException;
import com.example.shapebuilderbackend.Model.Activity.Activity;
import com.example.shapebuilderbackend.Model.Product;
import com.example.shapebuilderbackend.Model.Role.Role;
import com.example.shapebuilderbackend.Model.User;
import com.example.shapebuilderbackend.Repository.ProductRepository;
import com.example.shapebuilderbackend.Repository.UserRepository;
import com.example.shapebuilderbackend.Security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private ProductRepository productRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, CalendarService calendarService, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.calendarService = calendarService;
        this.productRepository = productRepository;
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
        user.setActivity(updateProfileRequest.getActivity());
        userRepository.save(user);
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = getCurrentUser();
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

    public double getUserPAL() {
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

    public AddUserProductRequest addUserProduct(AddUserProductRequest addUserProductRequest) {
        User user = getCurrentUser();
        Product product = new Product();
        product.setName(addUserProductRequest.getName());
        product.setProtein(addUserProductRequest.getProtein());
        product.setCarbs(addUserProductRequest.getCarbs());
        product.setFat(addUserProductRequest.getFat());
        product.setCalories(addUserProductRequest.getCalories());
        product.setUser(user);
        product.setCustom(true);

        Product saved = productRepository.save(product);

        return new AddUserProductRequest(
                saved.getId(),
                saved.getName(),
                saved.getProtein(),
                saved.getCarbs(),
                saved.getFat(),
                saved.getCalories(),
                saved.isCustom()
        );
    }

    public List<GetAllUserProducts> getAllUserProducts() {
        User user = getCurrentUser();
        List<Product> products = productRepository.findAllByUserId(user.getId());

        return products.stream()
                .map(p -> new GetAllUserProducts(
                        p.getId(),
                        p.getName(),
                        p.getProtein(),
                        p.getFat(),
                        p.getCarbs(),
                        p.getCalories()
                ))
                .collect(Collectors.toList());
    }

    public UpdateUserProductRequest updateUserProduct(UpdateUserProductRequest updateUserProductRequest) {
        User user = getCurrentUser();

        Product product = productRepository.findById(updateUserProductRequest.getId())
                .orElseThrow(() -> new NotFoundException("Nie znaleziono produktu"));

        if (!product.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Nie masz dostępu do tego produktu");
        }

        product.setName(updateUserProductRequest.getName());
        product.setProtein(updateUserProductRequest.getProtein());
        product.setCarbs(updateUserProductRequest.getCarbs());
        product.setFat(updateUserProductRequest.getFat());
        product.setCalories(updateUserProductRequest.getCalories());
        productRepository.save(product);

        return new UpdateUserProductRequest(
                product.getId(),
                product.getName(),
                product.getProtein(),
                product.getCarbs(),
                product.getFat(),
                product.getCalories()
        );

    }

    public void deleteUserProduct(Long productId) {
        Long userId = getCurrentUser().getId();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono produktu"));

        if (!product.getUser().getId().equals(userId)) {
            throw new SecurityException("Nie masz dostępu do tego produktu");
        }

        productRepository.delete(product);
    }

    public List<GetAllUsersResponse> getAllUsers() {
        User user = getCurrentUser();
        if(user.getRole().equals(Role.ROLE_ADMIN)) {
            return userRepository.findAll().stream()
                    .filter(u -> !u.getId().equals(user.getId()))
                    .map(u -> new GetAllUsersResponse(u.getId(), u.getFirstName(), u.getLastName(), u.getGender(), u.getAge(), u.getWeight(), u.getHeight(), u.getEmail(), u.getPassword(), u.getRole(), u.getActivity()))
                    .collect(toList());
        }
        else{
            throw new ForbiddenException("Nie masz uprawnien by wyświetlic profile innych użytkownikow");
        }
    }

    public void changeUseRole(ChangeUserRole changeUserRole) {
        User admin = getCurrentUser();
        User user =  userRepository.findById(changeUserRole.getId())
                .orElseThrow(() -> new NotFoundException("Nie znaleziono uzytkownika"));
        if(admin.getRole().equals(Role.ROLE_ADMIN)) {
            user.setRole(changeUserRole.getRole());
            userRepository.save(user);
        }else{
            throw new ForbiddenException("Nie masz uprawnien by zmieniac role innych uzytkownikow");
        }
    }

    public void deleteUser(DeleteUserRequest deleteUserRequest) {
        User admin = getCurrentUser();
        User user =  userRepository.findById(deleteUserRequest.getId())
                .orElseThrow(() -> new NotFoundException("Nie znaleziono uzytkownika"));

        if(admin.getRole().equals(Role.ROLE_ADMIN)) {
            userRepository.delete(user);
        }else{
            throw new ForbiddenException("Nie masz uprawnien by usuwac innych uzytkownikow");
        }
    }

    public List<GetAllUserDays> getAllUserDays() {
        User user = getCurrentUser();
        return user.getDays().stream()
                .map(e -> new GetAllUserDays(
                        e.getId(),
                        e.getDay(),
                        e.getModification_date(),
                        e.getMeals().stream()
                                .map(meal -> new DtoGetDayMeals(
                                        meal.getId(),
                                        meal.getDescription(),
                                        meal.getMealProducts().stream()
                                                .map(p -> {
                                                    double ratio = p.getAmount() / 100.0;
                                                    return new DtoMealProduct(
                                                            p.getId(),
                                                            p.getProduct().getId(),
                                                            p.getProduct().getName(),
                                                            p.getProduct().getCalories() * ratio,
                                                            p.getProduct().getProtein() * ratio,
                                                            p.getProduct().getCarbs() * ratio,
                                                            p.getProduct().getFat() * ratio,
                                                            p.getAmount()
                                                    );
                                                })
                                                .toList()
                                ))
                                .toList(),
                        e.getExercises().stream()
                                .map(ex -> new DtoGetDayExercises(
                                        ex.getId(),
                                        ex.getExerciseTemplate().getId(),
                                        ex.getExerciseTemplate().getName(),
                                        ex.getSets(),
                                        ex.getRepetitions(),
                                        ex.getWeight()
                                ))
                                .toList()
                ))
                .toList();
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }
}

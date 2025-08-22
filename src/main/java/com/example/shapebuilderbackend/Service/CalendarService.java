package com.example.shapebuilderbackend.Service;

import com.example.shapebuilderbackend.Dto.DtoDayExerciseSummary;
import com.example.shapebuilderbackend.Dto.DtoDaySummary;
import com.example.shapebuilderbackend.Dto.DtoExerciseSummary;
import com.example.shapebuilderbackend.Dto.DtoMealSummary;
import com.example.shapebuilderbackend.Exception.NotFoundException;
import com.example.shapebuilderbackend.Model.Calendar;
import com.example.shapebuilderbackend.Model.Exercise;
import com.example.shapebuilderbackend.Model.Meal;
import com.example.shapebuilderbackend.Model.User;
import com.example.shapebuilderbackend.Repository.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalendarService {

    @Autowired
    private MealService mealService;

    @Autowired
    private CalendarRepository calendarRepository;

    public void generateDaysForUser(User user) {
        List<Calendar> newDays = new ArrayList<>();
        LocalDate today = LocalDate.now();

        if (user.getDays().isEmpty()) {
            LocalDate endOfYear = LocalDate.of(today.getYear(), 12, 31);
            generateDays(newDays, user, today, endOfYear);
        }

        user.getDays().addAll(newDays);
    }

    public void generateDays(List<Calendar> newDays, User user, LocalDate today, LocalDate endOfYear) {
        for (LocalDate date = today; !date.isAfter(endOfYear); date = date.plusDays(1)) {
            Calendar day = new Calendar();
            day.setDay(date);
            day.setModification_date(LocalDateTime.now());
            day.setUser(user);
            List<Meal> meals = createDefaultMealsForDay(day);
            day.setMeals(meals);

            newDays.add(day);
        }
    }

    private List<Meal> createDefaultMealsForDay(Calendar day) {
        List<String> mealDescriptions = List.of("Śniadanie", "Obiad", "Przekąska", "Kolacja");

        return mealDescriptions.stream().map(desc -> {
            Meal meal = new Meal();
            meal.setDescription(desc);
            meal.setCalendar(day);
            return meal;
        }).toList();
    }

    public DtoDaySummary calculateDaySummary(Calendar calendar) {
        List<DtoMealSummary> mealSummaries = new ArrayList<>();
        double protein = 0, fat = 0, carbs = 0, calories = 0;

        for (Meal meal : calendar.getMeals()) {
            DtoMealSummary ms = mealService.calculateMealSummary(meal);
            mealSummaries.add(ms);

            protein += ms.getTotalProtein();
            fat += ms.getTotalFat();
            carbs += ms.getTotalCarbs();
            calories += ms.getTotalCalories();
        }

        return new DtoDaySummary(
                calendar.getDay(),
                mealSummaries,
                Math.round(protein),
                Math.round(fat),
                Math.round(carbs),
                Math.round(calories)
        );
    }

    public DtoDaySummary getDaySummary(Long calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono dnia o id: " + calendarId));
        return calculateDaySummary(calendar);
    }

    public DtoDayExerciseSummary getDayExerciseSummary(Long calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono dnia o id: " + calendarId));
        List<DtoExerciseSummary> exercises = calendar.getExercises().stream()
                .map(e -> new DtoExerciseSummary(
                        e.getId(),
                        e.getExerciseTemplate().getName(),
                        e.getSets(),
                        e.getRepetitions(),
                        e.getWeight()
                ))
                .toList();

        return new DtoDayExerciseSummary(calendar.getDay(), exercises);
    }
}

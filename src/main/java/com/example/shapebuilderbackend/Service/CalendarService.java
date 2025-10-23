package com.example.shapebuilderbackend.Service;

import com.example.shapebuilderbackend.Dto.*;
import com.example.shapebuilderbackend.Exception.NotFoundException;
import com.example.shapebuilderbackend.Model.Calendar;
import com.example.shapebuilderbackend.Model.Meal;
import com.example.shapebuilderbackend.Model.User;
import com.example.shapebuilderbackend.Repository.CalendarRepository;
import com.example.shapebuilderbackend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CalendarService {

    @Autowired
    private MealService mealService;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private UserRepository userRepository;

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
                protein,
                fat,
                carbs,
                calories
        );
    }

    public DtoDaySummary getDaySummary(Long calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono dnia o id: " + calendarId));
        DtoDaySummary daySummary = calculateDaySummary(calendar);
        List<DtoChartPointFood> chartData = List.of(new DtoChartPointFood(
                daySummary.getDate(),
                daySummary.getTotalCalories(),
                daySummary.getTotalProtein(),
                daySummary.getTotalCarbs(),
                daySummary.getTotalFat()
        ));
        return daySummary;
    }

    public DtoWeekSummary getWeekSummary(Long userId, LocalDate startOfWeek, LocalDate endOfWeek) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono użytkownika o podanym id"));
        LocalDate start;
        LocalDate end;
        if (startOfWeek != null && endOfWeek != null) {
            start = startOfWeek;
            end = endOfWeek;
        } else {
            LocalDate today = LocalDate.now();
            start = today.with(DayOfWeek.MONDAY);
            end = today.with(DayOfWeek.SUNDAY);
        }
        List<Calendar> calendars = calendarRepository.findByUserIdAndDayBetween(user.getId(), start, end);
        NutritionSummaryAggregate summary = calculateNutritionSummary(calendars);
        List<DtoChartPointFood> chartData = convertToChartData(summary.daySummaries());

        return new DtoWeekSummary(
                start,
                end,
                summary.daySummaries(),
                summary.totalCalories(),
                summary.avgCalories(),
                summary.avgProtein(),
                summary.avgCarbs(),
                summary.avgFat(),
                chartData
        );
    }

    public DtoMonthSummary getMonthSummary(Long userId, Integer year, Integer month) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono użytkownika o podanym id"));
        LocalDate startOfMonth;
        LocalDate endOfMonth;
        if (year != null && month != null) {
            startOfMonth = LocalDate.of(year, month, 1);
        } else {
            LocalDate today = LocalDate.now();
            startOfMonth = LocalDate.of(today.getYear(), today.getMonthValue(), 1);
        }
        endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
        List<Calendar> calendars = calendarRepository.findByUserIdAndDayBetween(user.getId(), startOfMonth, endOfMonth);
        NutritionSummaryAggregate summary = calculateNutritionSummary(calendars);
        List<DtoChartPointFood> chartData = convertToChartData(summary.daySummaries());

        return new DtoMonthSummary(
                startOfMonth.getYear(),
                startOfMonth.getMonthValue(),
                summary.daySummaries(),
                summary.totalCalories(),
                summary.avgCalories(),
                summary.avgProtein(),
                summary.avgCarbs(),
                summary.avgFat(),
                chartData
        );
    }

    private NutritionSummaryAggregate calculateNutritionSummary(List<Calendar> calendars) {
        List<DtoDaySummary> daySummaries = calendars.stream()
                .map(this::calculateDaySummary)
                .toList();
        double totalCalories = daySummaries.stream().mapToDouble(DtoDaySummary::getTotalCalories).sum();
        double avgCalories = daySummaries.isEmpty() ? 0 : totalCalories / daySummaries.size();

        double totalProtein = daySummaries.stream().mapToDouble(DtoDaySummary::getTotalProtein).sum();
        double avgProtein = daySummaries.isEmpty() ? 0 : totalProtein / daySummaries.size();

        double totalCarbs = daySummaries.stream().mapToDouble(DtoDaySummary::getTotalCarbs).sum();
        double avgCarbs = daySummaries.isEmpty() ? 0 : totalCarbs / daySummaries.size();

        double totalFat = daySummaries.stream().mapToDouble(DtoDaySummary::getTotalFat).sum();
        double avgFat = daySummaries.isEmpty() ? 0 : totalFat / daySummaries.size();

        return new NutritionSummaryAggregate(daySummaries, totalCalories, avgCalories, totalProtein, avgProtein, totalCarbs, avgCarbs, totalFat, avgFat);
    }

    private List<DtoChartPointFood> convertToChartData(List<DtoDaySummary> days) {
        return days.stream()
                .sorted(Comparator.comparing(DtoDaySummary::getDate))
                .map(d -> new DtoChartPointFood(
                        d.getDate(),
                        d.getTotalCalories(),
                        d.getTotalProtein(),
                        d.getTotalCarbs(),
                        d.getTotalFat()
                ))
                .toList();
    }

    public DtoDayExerciseSummary getDayExerciseSummary(Long calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono dnia o id: " + calendarId));

        return calculateDayExerciseSummary(calendar);
    }

    public DtoPeriodExerciseSummary getWeekExerciseSummary(Long userId, LocalDate startOfWeek, LocalDate endOfWeek) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono użytkownika o podanym id"));
        LocalDate start;
        LocalDate end;
        if (startOfWeek != null && endOfWeek != null) {
            start = startOfWeek;
            end = endOfWeek;
        } else {
            LocalDate today = LocalDate.now();
            start = today.with(DayOfWeek.MONDAY);
            end = today.with(DayOfWeek.SUNDAY);
        }
        List<DtoDayExerciseSummary> daySummaries = getExerciseSummaryForPeriod(user.getId(), start, end);
        double totalVolume = daySummaries.stream().mapToDouble(DtoDayExerciseSummary::getTotalVolume).sum();
        double avgVolume = daySummaries.isEmpty() ? 0 : totalVolume / daySummaries.size();
        List<DtoChartPointExercise> chartData = daySummaries.stream()
                .map(d -> new DtoChartPointExercise(d.getDate(), d.getTotalVolume(), d.getAvgVolume()))
                .toList();

        return new DtoPeriodExerciseSummary(start, end, daySummaries, avgVolume, totalVolume, chartData);
    }

    public DtoPeriodExerciseSummary getMonthExerciseSummary(Long userId, Integer year, Integer month) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono użytkownika o podanym id"));
        LocalDate startOfMonth;
        LocalDate endOfMonth;
        if (year != null && month != null) {
            startOfMonth = LocalDate.of(year, month, 1);
        } else {
            LocalDate today = LocalDate.now();
            startOfMonth = LocalDate.of(today.getYear(), today.getMonthValue(), 1);
        }
        endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
        List<DtoDayExerciseSummary> daySummaries = getExerciseSummaryForPeriod(user.getId(), startOfMonth, endOfMonth);
        double totalVolume = daySummaries.stream().mapToDouble(DtoDayExerciseSummary::getTotalVolume).sum();
        double avgVolume = daySummaries.isEmpty() ? 0 : totalVolume / daySummaries.size();

        List<DtoChartPointExercise> chartData = daySummaries.stream()
                .map(d -> new DtoChartPointExercise(d.getDate(), d.getTotalVolume(), d.getAvgVolume()))
                .toList();

        return new DtoPeriodExerciseSummary(startOfMonth, endOfMonth, daySummaries, avgVolume, totalVolume, chartData);
    }

    private DtoDayExerciseSummary calculateDayExerciseSummary(Calendar calendar) {
        List<DtoExerciseSummary> exercises = calendar.getExercises().stream()
                .map(e -> new DtoExerciseSummary(
                        e.getId(),
                        e.getExerciseTemplate().getName(),
                        e.getSets(),
                        e.getRepetitions(),
                        e.getWeight()
                ))
                .toList();
        double totalVolume = exercises.stream()
                .mapToDouble(e -> e.getSets() * e.getRepetitions() * e.getWeight())
                .sum();
        double avgWeight = exercises.stream()
                .mapToDouble(DtoExerciseSummary::getWeight)
                .average()
                .orElse(0);

        return new DtoDayExerciseSummary(calendar.getDay(), exercises, totalVolume, avgWeight);
    }

    private List<DtoDayExerciseSummary> getExerciseSummaryForPeriod(Long userId, LocalDate start, LocalDate end) {
        List<Calendar> calendars = calendarRepository.findByUserIdAndDayBetween(userId, start, end);

        return calendars.stream()
                .filter(c -> !c.getExercises().isEmpty())
                .map(this::calculateDayExerciseSummary)
                .sorted(Comparator.comparing(DtoDayExerciseSummary::getDate))
                .toList();
    }
}


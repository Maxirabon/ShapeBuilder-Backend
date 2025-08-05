package com.example.shapebuilderbackend.Service;

import com.example.shapebuilderbackend.Model.Calendar;
import com.example.shapebuilderbackend.Model.User;
import com.example.shapebuilderbackend.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalendarService calendarService;

    public AppService(UserRepository userRepository, CalendarService calendarService) {
        this.userRepository = userRepository;
        this.calendarService = calendarService;
    }

    @Scheduled(cron = "0 0 0 1 1 *") // (cron = "0 0 0 1 1 *")1 stycznia o północy każdego roku
    @Transactional
    public void generateNextYearCalendarForAllUsers() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            Set<Integer> years = user.getDays().stream()
                    .map(day -> day.getDay().getYear())
                    .collect(Collectors.toSet());

            int nextYear = years.stream()
                    .mapToInt(Integer::intValue)
                    .max()
                    .orElse(LocalDate.now().getYear()) + 1;

            if (!years.contains(nextYear)) {
                List<Calendar> days = new ArrayList<>();
                LocalDate start = LocalDate.of(nextYear, 1, 1);
                LocalDate end = LocalDate.of(nextYear, 12, 31);
                calendarService.generateDays(days, user, start, end);
                user.getDays().addAll(days);
                userRepository.save(user);
            }
        }
    }
}

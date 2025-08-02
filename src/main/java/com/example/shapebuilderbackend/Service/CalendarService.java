package com.example.shapebuilderbackend.Service;

import com.example.shapebuilderbackend.Model.Calendar;
import com.example.shapebuilderbackend.Model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalendarService {

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
            newDays.add(day);
        }
    }
}

package com.example.shapebuilderbackend.Repository;

import com.example.shapebuilderbackend.Model.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar,Long> {

    Optional<Calendar> findCalendarByDay(LocalDate day);

}

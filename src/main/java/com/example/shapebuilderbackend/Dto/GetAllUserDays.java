package com.example.shapebuilderbackend.Dto;

import com.example.shapebuilderbackend.Model.Calendar;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class GetAllUserDays {
    Long dayId;
    LocalDate day;
    LocalDateTime modification_date;
}

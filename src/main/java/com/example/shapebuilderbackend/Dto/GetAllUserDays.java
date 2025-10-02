package com.example.shapebuilderbackend.Dto;

import com.example.shapebuilderbackend.Model.Meal;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class GetAllUserDays {
    private Long dayId;
    private LocalDate day;
    private LocalDateTime modificationDate;
    private List<DtoGetDayMeals> meals;

}

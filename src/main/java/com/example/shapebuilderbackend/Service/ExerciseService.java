package com.example.shapebuilderbackend.Service;

import com.example.shapebuilderbackend.Dto.AddExerciseRequest;
import com.example.shapebuilderbackend.Exception.ConflictException;
import com.example.shapebuilderbackend.Exception.NotFoundException;
import com.example.shapebuilderbackend.Model.Calendar;
import com.example.shapebuilderbackend.Model.Exercise;
import com.example.shapebuilderbackend.Model.ExerciseTemplate;
import com.example.shapebuilderbackend.Repository.CalendarRepository;
import com.example.shapebuilderbackend.Repository.ExerciseTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExerciseService {

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private ExerciseTemplateRepository exerciseTemplateRepository;

    public ExerciseService(CalendarRepository calendarRepository, ExerciseTemplateRepository exerciseTemplateRepository) {
        this.calendarRepository = calendarRepository;
        this.exerciseTemplateRepository = exerciseTemplateRepository;
    }

    public void addExercise(AddExerciseRequest addExerciseRequest) {
        Calendar day = calendarRepository.findCalendarByDay(addExerciseRequest.getDay())
                .orElseThrow(() -> new NotFoundException("Nieprawidłowy dzień"));

        ExerciseTemplate template = exerciseTemplateRepository.findById(addExerciseRequest.getExerciseTemplateId())
                .orElseThrow(() -> new NotFoundException("Nieprawidłowy szablon ćwiczenia"));

        boolean exists = day.getExercises().stream()
                .anyMatch(e -> e.getExerciseTemplate().getId().equals(template.getId()));

        if (exists) {
            throw new ConflictException("Ćwiczenie tego typu już istnieje dla tego dnia.");
        }

        Exercise exercise = new Exercise();
        exercise.setExerciseTemplate(template);
        exercise.setSets(addExerciseRequest.getSets());
        exercise.setRepetitions(addExerciseRequest.getRepetitions());
        exercise.setWeight(addExerciseRequest.getWeight());
        exercise.setCalendar(day);

        day.getExercises().add(exercise);
        calendarRepository.save(day);
    }
}

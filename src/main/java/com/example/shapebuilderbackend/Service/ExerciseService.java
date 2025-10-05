package com.example.shapebuilderbackend.Service;

import com.example.shapebuilderbackend.Dto.AddExerciseRequest;
import com.example.shapebuilderbackend.Dto.DeleteExerciseRequest;
import com.example.shapebuilderbackend.Dto.DtoAddExerciseResponse;
import com.example.shapebuilderbackend.Dto.UpdateExerciseRequest;
import com.example.shapebuilderbackend.Exception.ConflictException;
import com.example.shapebuilderbackend.Exception.NotFoundException;
import com.example.shapebuilderbackend.Model.Calendar;
import com.example.shapebuilderbackend.Model.Exercise;
import com.example.shapebuilderbackend.Model.ExerciseTemplate;
import com.example.shapebuilderbackend.Repository.CalendarRepository;
import com.example.shapebuilderbackend.Repository.ExerciseRepository;
import com.example.shapebuilderbackend.Repository.ExerciseTemplateRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ExerciseService {

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseTemplateRepository exerciseTemplateRepository;

    public ExerciseService(CalendarRepository calendarRepository, ExerciseTemplateRepository exerciseTemplateRepository, ExerciseRepository exerciseRepository) {
        this.calendarRepository = calendarRepository;
        this.exerciseTemplateRepository = exerciseTemplateRepository;
        this.exerciseRepository = exerciseRepository;
    }

    public DtoAddExerciseResponse addExercise(AddExerciseRequest addExerciseRequest) {
        Calendar day = calendarRepository.findCalendarByDay(addExerciseRequest.getDay())
                .orElseThrow(() -> new NotFoundException("Nieprawidłowy dzień"));

        ExerciseTemplate template = exerciseTemplateRepository.findById(addExerciseRequest.getExerciseTemplateId())
                .orElseThrow(() -> new NotFoundException("Nieprawidłowy szablon ćwiczenia"));

        boolean exists = day.getExercises().stream()
                .anyMatch(e -> e.getExerciseTemplate().getId().equals(template.getId()));

        if (exists) {
            throw new ConflictException("Ćwiczenie już znajduje się tego dnia. Usuń je lub zmodyfikuj!");
        }

        Exercise exercise = new Exercise();
        exercise.setExerciseTemplate(template);
        exercise.setSets(addExerciseRequest.getSets());
        exercise.setRepetitions(addExerciseRequest.getRepetitions());
        exercise.setWeight(addExerciseRequest.getWeight());
        exercise.setCalendar(day);

        Exercise savedExercise = exerciseRepository.save(exercise);

        day.getExercises().add(savedExercise);
        calendarRepository.save(day);

        DtoAddExerciseResponse dto = new DtoAddExerciseResponse();
        dto.setId(savedExercise.getId());
        dto.setExerciseTemplateId(template.getId());
        dto.setName(template.getName());
        dto.setSets(savedExercise.getSets());
        dto.setRepetitions(savedExercise.getRepetitions());
        dto.setWeight(savedExercise.getWeight());

        return dto;
    }

    public Exercise updateExercise(UpdateExerciseRequest updateExerciseRequest) {
        Exercise updatedExercise = exerciseRepository.findById(updateExerciseRequest.getId())
                .orElseThrow(() -> new NotFoundException("Nie ma ćwiczenia o takim ID"));

        updatedExercise.setSets(updateExerciseRequest.getSets());
        updatedExercise.setRepetitions(updateExerciseRequest.getRepetitions());
        updatedExercise.setWeight(updateExerciseRequest.getWeight());

        return exerciseRepository.save(updatedExercise);
    }

    public void deleteExercise(DeleteExerciseRequest deleteExerciseRequest) {
        Exercise exercise = exerciseRepository.findById(deleteExerciseRequest.getId())
                .orElseThrow(() -> new NotFoundException("Nie ma ćwiczenia o takim ID"));

        Long calendarId = exercise.getCalendar().getId();
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono kalendarza"));

        calendar.getExercises().removeIf(e -> e.getId().equals(exercise.getId()));
        calendarRepository.save(calendar);
    }
}

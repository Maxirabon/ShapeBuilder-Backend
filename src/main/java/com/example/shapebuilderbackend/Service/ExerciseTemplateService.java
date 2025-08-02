package com.example.shapebuilderbackend.Service;

import com.example.shapebuilderbackend.Dto.getAllExerciseTemplateResponse;
import com.example.shapebuilderbackend.Model.ExerciseTemplate;
import com.example.shapebuilderbackend.Repository.ExerciseTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseTemplateService {

    @Autowired
    private ExerciseTemplateRepository exerciseTemplateRepository;

    public List<getAllExerciseTemplateResponse> getAllExerciseTemplates() {
        return exerciseTemplateRepository.findAll().stream()
                .map(et -> new getAllExerciseTemplateResponse(et.getId(), et.getName()))
                .collect(Collectors.toList());
    }
}

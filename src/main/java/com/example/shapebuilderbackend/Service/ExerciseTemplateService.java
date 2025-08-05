package com.example.shapebuilderbackend.Service;

import com.example.shapebuilderbackend.Dto.GetAllExerciseTemplateResponse;
import com.example.shapebuilderbackend.Repository.ExerciseTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseTemplateService {

    @Autowired
    private ExerciseTemplateRepository exerciseTemplateRepository;

    public List<GetAllExerciseTemplateResponse> getAllExerciseTemplates() {
        return exerciseTemplateRepository.findAll().stream()
                .map(et -> new GetAllExerciseTemplateResponse(et.getId(), et.getName()))
                .collect(Collectors.toList());
    }
}

package com.example.shapebuilderbackend.Service;

import com.example.shapebuilderbackend.Dto.AddExerciseTemplateRequest;
import com.example.shapebuilderbackend.Dto.AddExerciseTemplateResponse;
import com.example.shapebuilderbackend.Dto.GetAllExerciseTemplateResponse;
import com.example.shapebuilderbackend.Dto.UpdateExerciseTemplateRequest;
import com.example.shapebuilderbackend.Exception.NotFoundException;
import com.example.shapebuilderbackend.Model.ExerciseTemplate;
import com.example.shapebuilderbackend.Repository.ExerciseRepository;
import com.example.shapebuilderbackend.Repository.ExerciseTemplateRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseTemplateService {

    @Autowired
    private ExerciseTemplateRepository exerciseTemplateRepository;
    @Autowired
    private ExerciseRepository exerciseRepository;

    public List<GetAllExerciseTemplateResponse> getAllExerciseTemplates() {
        return exerciseTemplateRepository.findAll().stream()
                .map(et -> new GetAllExerciseTemplateResponse(et.getId(), et.getName()))
                .collect(Collectors.toList());
    }

    public AddExerciseTemplateResponse addExerciseTemplate(AddExerciseTemplateRequest request) {
        ExerciseTemplate exerciseTemplate = new ExerciseTemplate();
        exerciseTemplate.setName(request.getName());
        ExerciseTemplate saved = exerciseTemplateRepository.save(exerciseTemplate);

        return new AddExerciseTemplateResponse(saved.getId(), saved.getName());
    }

    public AddExerciseTemplateResponse updateExerciseTemplate(UpdateExerciseTemplateRequest request) {
        ExerciseTemplate existing = exerciseTemplateRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Nie znaleziono szablonu ćwiczenia o id: " + request.getId()));
        existing.setName(request.getName());
        ExerciseTemplate saved = exerciseTemplateRepository.save(existing);

        return new AddExerciseTemplateResponse(saved.getId(), saved.getName());
    }

    @Transactional
    public void deleteExerciseTemplate(Long id) {
        ExerciseTemplate existing = exerciseTemplateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nie znaleziono szablonu ćwiczenia o id: " + id));
        exerciseRepository.deleteAllByExerciseTemplateId(id);
        exerciseTemplateRepository.delete(existing);
    }

}

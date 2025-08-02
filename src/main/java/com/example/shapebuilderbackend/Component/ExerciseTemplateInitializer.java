package com.example.shapebuilderbackend.Component;

import com.example.shapebuilderbackend.Model.ExerciseTemplate;
import com.example.shapebuilderbackend.Repository.ExerciseTemplateRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class ExerciseTemplateInitializer implements CommandLineRunner {

    private final ExerciseTemplateRepository repository;

    public ExerciseTemplateInitializer(ExerciseTemplateRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() > 0) {
            return;
        }

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("data.sql")) {
            if (is == null) {
                throw new FileNotFoundException("Nie znaleziono pliku data.sql w resources");
            }

            List<String> exerciseNames = new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .filter(line -> !line.isBlank())
                    .toList();

            List<ExerciseTemplate> templates = exerciseNames.stream()
                    .map(ExerciseTemplate::new)
                    .toList();

            repository.saveAll(templates);
        }
    }
}
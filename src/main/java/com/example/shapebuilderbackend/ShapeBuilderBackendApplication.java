package com.example.shapebuilderbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShapeBuilderBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShapeBuilderBackendApplication.class, args);
    }

}

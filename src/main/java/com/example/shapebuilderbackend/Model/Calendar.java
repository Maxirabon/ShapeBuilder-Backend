package com.example.shapebuilderbackend.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "calendar")
public class Calendar {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate day;
    private LocalDateTime modification_date;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Exercise> exercises = new ArrayList<>();

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Meal> meals = new ArrayList<>();
}

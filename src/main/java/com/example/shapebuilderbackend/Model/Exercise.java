package com.example.shapebuilderbackend.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int sets;
    private int repetitions;
    private double weight;

    @ManyToOne(optional = false)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

}

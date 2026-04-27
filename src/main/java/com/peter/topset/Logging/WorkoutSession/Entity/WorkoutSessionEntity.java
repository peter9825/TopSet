package com.peter.topset.Logging.WorkoutSession.Entity;

import com.peter.topset.Logging.Exercises.Entity.ExerciseEntity;
import com.peter.topset.Users.Entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class WorkoutSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    @Column(nullable = false)
    private String name;

    //one workout session -> many exercises
    @OneToMany(mappedBy = "workoutSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseEntity> exercises = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private UserEntity users;

    public WorkoutSessionEntity(){}

    public WorkoutSessionEntity(LocalDate date, String name){
        this.date = date;
        this.name = name;
    }

    public void addExercise(ExerciseEntity exercise){
        exercises.add(exercise);
        exercise.setWorkoutSession(this);
    }

    public void removeExercise(ExerciseEntity exercise){
        exercises.remove(exercise);
        exercise.setWorkoutSession(null);
    }

}

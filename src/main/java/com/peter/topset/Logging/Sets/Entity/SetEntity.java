package com.peter.topset.Logging.Sets.Entity;

import com.peter.topset.Logging.Exercises.Entity.ExerciseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "set_log")
public class SetEntity {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

//many sets -> one exercise
@ManyToOne(fetch = FetchType.LAZY,optional = false)
@JoinColumn(name = "exercise_log_id",nullable = false)
private ExerciseEntity exercise;

private int setNumber;

private double weight;

private int reps;


public SetEntity(){}

public SetEntity(int setNumber, double weight, int reps){

        this.setNumber = setNumber;
        this.weight = weight;
        this.reps = reps;
}

}

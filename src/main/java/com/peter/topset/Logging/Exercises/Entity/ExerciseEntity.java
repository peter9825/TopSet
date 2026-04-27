package com.peter.topset.Logging.Exercises.Entity;

import com.peter.topset.Logging.Sets.Entity.SetEntity;
import com.peter.topset.Logging.WorkoutSession.Entity.WorkoutSessionEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "exercise_log")
public class ExerciseEntity {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

//fk -> pk
//many exercises, one workout session
@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "workout_session_id", nullable = false)
private WorkoutSessionEntity workoutSession;

@Column(nullable = false)
private String exerciseName;

private boolean pr;

//one exercise, many sets
@OneToMany(mappedBy = "exercise",cascade = CascadeType.ALL, orphanRemoval = true)
private List<SetEntity> sets = new ArrayList<>();

public ExerciseEntity(){}

public ExerciseEntity(String exerciseName, boolean pr){
    this.exerciseName = exerciseName;
    this.pr = pr;
}
    public void addSet(SetEntity set) {
        sets.add(set);
        set.setExercise(this);
    }

    public void removeSet(SetEntity set) {
        sets.remove(set);
        set.setExercise(null);
    }
}

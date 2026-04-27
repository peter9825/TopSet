package com.peter.topset.Logging.Exercises.Repository;

import com.peter.topset.Logging.Exercises.Entity.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository <ExerciseEntity,Long> {

}

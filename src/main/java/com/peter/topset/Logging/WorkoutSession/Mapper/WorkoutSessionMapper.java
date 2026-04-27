package com.peter.topset.Logging.WorkoutSession.Mapper;

import com.peter.topset.Logging.Exercises.Dtos.ExerciseRequestDto;
import com.peter.topset.Logging.Exercises.Dtos.ExerciseResponseDto;
import com.peter.topset.Logging.Exercises.Entity.ExerciseEntity;
import com.peter.topset.Logging.Sets.Entity.SetEntity;
import com.peter.topset.Logging.WorkoutSession.Dtos.WorkoutSessionRequestDto;
import com.peter.topset.Logging.WorkoutSession.Dtos.WorkoutSessionResponseDto;
import com.peter.topset.Logging.WorkoutSession.Entity.WorkoutSessionEntity;
import com.peter.topset.Logging.Sets.Dtos.SetRequestDto;
import com.peter.topset.Logging.Sets.Dtos.SetResponseDto;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class WorkoutSessionMapper {

    /*==================*/
    //Dto -> Entity
    /*==================*/


public WorkoutSessionEntity toEntity (WorkoutSessionRequestDto dto){

    WorkoutSessionEntity session = new WorkoutSessionEntity(dto.getDate(), dto.getName());

    if (dto.getExercises() != null){
       dto.getExercises()
               .stream()
               .map(this::toEntity)
               .forEach(session::addExercise);
    }
    return session;
}

private ExerciseEntity toEntity (ExerciseRequestDto dto){

    ExerciseEntity exercise = new ExerciseEntity(dto.getExerciseName(), dto.isPr());

    if (dto.getSets() != null){

        dto.getSets()
                .stream()
                .map(this::toEntity)
                .forEach(exercise::addSet);
    }
    return exercise;
}


 private SetEntity toEntity(SetRequestDto dto){
    return new SetEntity(dto.getSetNumber(),dto.getWeight(),dto.getReps());
}

    /*==================*/
    //Entity -> Dto
    /*==================*/

    public WorkoutSessionResponseDto toDto (WorkoutSessionEntity e) {
        List<ExerciseResponseDto> exercises =
                e.getExercises() == null ? Collections.emptyList() :
                        e.getExercises()
                        .stream()
                        .map(this::toDto)
                        .collect(Collectors.toList());

        return new WorkoutSessionResponseDto(e.getId(),e.getDate(),e.getName(),exercises);
    }


    private ExerciseResponseDto toDto (ExerciseEntity e) {

        List<SetResponseDto> sets =
                e.getSets() == null ? Collections.emptyList() :
                        e.getSets()
                        .stream()
                        .map(this::toDto)
                        .collect(Collectors.toList());

        return new ExerciseResponseDto(e.getId(), e.getExerciseName(), sets, e.isPr());

    }

    private SetResponseDto toDto (SetEntity e) {
        return new SetResponseDto(e.getId(), e.getSetNumber(), e.getWeight(), e.getReps());
    }

}


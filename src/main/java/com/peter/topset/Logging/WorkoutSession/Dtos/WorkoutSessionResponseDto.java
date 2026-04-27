package com.peter.topset.Logging.WorkoutSession.Dtos;

import com.peter.topset.Logging.Exercises.Dtos.ExerciseResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class WorkoutSessionResponseDto {

    private Long id;
    private LocalDate date;
    private String name;
    private List<ExerciseResponseDto> exercises;


    public WorkoutSessionResponseDto(Long id, LocalDate date, String name, List<ExerciseResponseDto> exercises){
        this.id = id;
        this.date = date;
        this.name = name;
        this.exercises = exercises;

    }

}

package com.peter.topset.Logging.WorkoutSession.Dtos;
import com.peter.topset.Logging.Exercises.Dtos.ExerciseRequestDto;
import jakarta.validation.constraints.*;
import lombok.Getter;
import java.util.List;
import java.time.LocalDate;

@Getter
public class WorkoutSessionRequestDto {

    @NotNull
    @NotBlank
    @PastOrPresent(message = "Invalid date.")
    private LocalDate date;

    @NotNull
    @NotBlank
    @Size(max = 15, message = "workout name cannot be more than 15 characters long.")
    private String name;

    @NotNull
    @NotBlank
    private List<ExerciseRequestDto> exercises;

    public WorkoutSessionRequestDto(LocalDate date, String name, List<ExerciseRequestDto> exercises){
        this.date = date;
        this.name = name;
        this.exercises = exercises;
    }

}

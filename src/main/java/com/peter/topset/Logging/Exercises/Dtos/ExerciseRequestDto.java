package com.peter.topset.Logging.Exercises.Dtos;

import com.peter.topset.Logging.Sets.Dtos.SetRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class ExerciseRequestDto {

@NotNull
@NotBlank
private String exerciseName;

@NotNull
@NotBlank
private List<SetRequestDto> sets;

@NotNull
@NotBlank
private boolean pr;

}

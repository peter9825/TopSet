package com.peter.topset.Logging.Exercises.Dtos;

import com.peter.topset.Logging.Sets.Dtos.SetResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExerciseResponseDto {

private Long id;
private String exerciseName;
private List<SetResponseDto> sets;
private boolean pr;


public ExerciseResponseDto(Long id, String exerciseName, List<SetResponseDto> sets, boolean pr){
    this.id = id;
    this.exerciseName = exerciseName;
    this.sets = sets;
    this.pr = pr;

}


}

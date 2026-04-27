package com.peter.topset.Logging.Sets.Dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetResponseDto {

    private Long id;
    private int setNumber;
    private double weight;
    private int reps;


    public SetResponseDto(Long id, int setNumber, double weight, int reps){
        this.id = id;
        this.setNumber = setNumber;
        this.weight = weight;
        this.reps = reps;
    }
}

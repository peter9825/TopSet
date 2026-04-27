package com.peter.topset.Logging.Sets.Dtos;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SetRequestDto {

    @NotNull
    @NotBlank
    @Min(value = 1,message = "An exercise must have at least 1 set logged.")
    private int setNumber;

    @NotNull
    @NotBlank
    private double weight;

    @NotNull
    @NotBlank
    @Min(value = 1, message = "An exercise must be logged with at least 1 rep.")
    private int reps;


}

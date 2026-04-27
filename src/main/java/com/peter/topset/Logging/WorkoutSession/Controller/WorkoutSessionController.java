package com.peter.topset.Logging.WorkoutSession.Controller;

import com.peter.topset.Logging.WorkoutSession.Dtos.WorkoutSessionRequestDto;
import com.peter.topset.Logging.WorkoutSession.Dtos.WorkoutSessionResponseDto;
import com.peter.topset.Logging.WorkoutSession.Service.WorkoutSessionService;
import com.peter.topset.Users.Entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workouts")
public class WorkoutSessionController {
    private final WorkoutSessionService service;
    public WorkoutSessionController(WorkoutSessionService service){

        this.service = service;
    }


    @GetMapping
    public List<WorkoutSessionResponseDto> getAll(){
        return service.getAll();
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<WorkoutSessionResponseDto> createWorkout(@RequestBody WorkoutSessionRequestDto dto){
        WorkoutSessionResponseDto response = service.createWorkout(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public WorkoutSessionResponseDto getById(@PathVariable Long id){
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public WorkoutSessionResponseDto update(@PathVariable Long id, @RequestBody WorkoutSessionRequestDto dto){
        return service.update(id,dto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }


}

package com.peter.topset.Logging.WorkoutSession.Service;
import com.peter.topset.Logging.Exercises.Entity.ExerciseEntity;
import com.peter.topset.Logging.WorkoutSession.Dtos.WorkoutSessionRequestDto;
import com.peter.topset.Logging.WorkoutSession.Dtos.WorkoutSessionResponseDto;
import com.peter.topset.Logging.WorkoutSession.Entity.WorkoutSessionEntity;
import com.peter.topset.Logging.WorkoutSession.Repository.WorkoutSessionRepository;
import com.peter.topset.Logging.WorkoutSession.Mapper.WorkoutSessionMapper;
import com.peter.topset.Users.Entity.UserEntity;
import com.peter.topset.Users.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutSessionService {

    private final WorkoutSessionRepository repository;
    private final WorkoutSessionMapper mapper;
    private final UserRepository userRepository;

    public WorkoutSessionService(WorkoutSessionRepository repository, WorkoutSessionMapper mapper, UserRepository userRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    //CREATE
    @Transactional
    public WorkoutSessionResponseDto createWorkout(WorkoutSessionRequestDto dto) {

       UserEntity currentUser = getCurrentUser();

            WorkoutSessionEntity workout = mapper.toEntity(dto);
            currentUser.addWorkout(workout);
            WorkoutSessionEntity saved = repository.save(workout);
            return mapper.toDto(saved);
    }

    //READ
    @Transactional
    public List <WorkoutSessionResponseDto> getAll(){

        UserEntity currentUser = getCurrentUser();

        return repository.
                findAllByUsers(currentUser)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

    }

    //READ by Id
    @Transactional
    public WorkoutSessionResponseDto getById(Long id){

        UserEntity currentUser = getCurrentUser();

        WorkoutSessionEntity workout = repository.findByIdAndUsers(id,currentUser)
                .orElseThrow(() -> new RuntimeException("Workout Session does not exist."));
        return mapper.toDto(workout);
    }

    //UPDATE
    @Transactional
    public WorkoutSessionResponseDto update( Long id, WorkoutSessionRequestDto dto){

        UserEntity currentUser = getCurrentUser();

        WorkoutSessionEntity workout = repository.findByIdAndUsers(id,currentUser)
                .orElseThrow(() -> new RuntimeException("Workout Session does not exist."));

        workout.setName(dto.getName());
        workout.setDate(dto.getDate());

        workout.getExercises().clear();
        WorkoutSessionEntity rebuilt = mapper.toEntity(dto);
        for (ExerciseEntity ex : rebuilt.getExercises()) {
            workout.addExercise(ex);
        }

        WorkoutSessionEntity saved = repository.save(workout);
        return mapper.toDto(saved);

    }


    //DELETE
    @Transactional
    public void delete(Long id){
        UserEntity currentUser = getCurrentUser();

      WorkoutSessionEntity workout = repository.findByIdAndUsers(id,currentUser)
                .orElseThrow(() -> new RuntimeException("Workout Session does not exist."));

       repository.delete(workout);

    }



//helper to get authenticated user
    private UserEntity getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {

            throw new RuntimeException("User could not be identified.");
        }
         UserEntity currentUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found."));

        return currentUser;

    }


}






package com.peter.topset.LoggingTests.Service;

import com.peter.topset.Logging.Exercises.Entity.ExerciseEntity;
import com.peter.topset.Logging.WorkoutSession.Dtos.WorkoutSessionRequestDto;
import com.peter.topset.Logging.WorkoutSession.Dtos.WorkoutSessionResponseDto;
import com.peter.topset.Logging.WorkoutSession.Entity.WorkoutSessionEntity;
import com.peter.topset.Logging.WorkoutSession.Mapper.WorkoutSessionMapper;
import com.peter.topset.Logging.WorkoutSession.Repository.WorkoutSessionRepository;
import com.peter.topset.Users.Entity.UserEntity;
import com.peter.topset.Users.Repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import com.peter.topset.Logging.WorkoutSession.Service.WorkoutSessionService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutSessionServiceTest {

    @Mock
    private WorkoutSessionRepository repository;

    @Mock
    private WorkoutSessionMapper mapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WorkoutSessionService service;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createWorkout_ShouldSaveWorkoutForCurrentUser() {
        String email = "user@email.com";
        mockCurrentUser(email);

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail(email);

        WorkoutSessionRequestDto requestDto =
                new WorkoutSessionRequestDto(LocalDate.of(2026, 4, 24), "Push Day", List.of());

        WorkoutSessionEntity workoutEntity = new WorkoutSessionEntity(
                LocalDate.of(2026, 4, 24),
                "Push Day"
        );

        WorkoutSessionEntity savedEntity = new WorkoutSessionEntity(
                LocalDate.of(2026, 4, 24),
                "Push Day"
        );
        savedEntity.setId(10L);

        WorkoutSessionResponseDto responseDto =
                new WorkoutSessionResponseDto(10L, LocalDate.of(2026, 4, 24), "Push Day", List.of());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(mapper.toEntity(requestDto)).thenReturn(workoutEntity);
        when(repository.save(workoutEntity)).thenReturn(savedEntity);
        when(mapper.toDto(savedEntity)).thenReturn(responseDto);

        WorkoutSessionResponseDto result = service.createWorkout(requestDto);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getName()).isEqualTo("Push Day");

        verify(userRepository).findByEmail(email);
        verify(mapper).toEntity(requestDto);
        verify(repository).save(workoutEntity);
        verify(mapper).toDto(savedEntity);
    }

    @Test
    void getAll_ShouldReturnCurrentUsersWorkouts() {
        String email = "user@email.com";
        mockCurrentUser(email);

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail(email);

        WorkoutSessionEntity workout1 = new WorkoutSessionEntity(LocalDate.of(2026, 4, 24), "Push Day");
        workout1.setId(1L);

        WorkoutSessionEntity workout2 = new WorkoutSessionEntity(LocalDate.of(2026, 4, 25), "Pull Day");
        workout2.setId(2L);

        WorkoutSessionResponseDto dto1 =
                new WorkoutSessionResponseDto(1L, LocalDate.of(2026, 4, 24), "Push Day", List.of());

        WorkoutSessionResponseDto dto2 =
                new WorkoutSessionResponseDto(2L, LocalDate.of(2026, 4, 25), "Pull Day", List.of());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(repository.findAllByUsers(user)).thenReturn(List.of(workout1, workout2));
        when(mapper.toDto(workout1)).thenReturn(dto1);
        when(mapper.toDto(workout2)).thenReturn(dto2);

        List<WorkoutSessionResponseDto> result = service.getAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Push Day");
        assertThat(result.get(1).getName()).isEqualTo("Pull Day");

        verify(repository).findAllByUsers(user);
    }

    @Test
    void getById_ShouldReturnWorkout_WhenWorkoutBelongsToCurrentUser() {
        String email = "user@email.com";
        Long workoutId = 1L;
        mockCurrentUser(email);

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail(email);

        WorkoutSessionEntity workout = new WorkoutSessionEntity(LocalDate.of(2026, 4, 24), "Push Day");
        workout.setId(workoutId);

        WorkoutSessionResponseDto responseDto =
                new WorkoutSessionResponseDto(workoutId, LocalDate.of(2026, 4, 24), "Push Day", List.of());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(repository.findByIdAndUsers(workoutId, user)).thenReturn(Optional.of(workout));
        when(mapper.toDto(workout)).thenReturn(responseDto);

        WorkoutSessionResponseDto result = service.getById(workoutId);

        assertThat(result.getId()).isEqualTo(workoutId);
        assertThat(result.getName()).isEqualTo("Push Day");

        verify(repository).findByIdAndUsers(workoutId, user);
    }

    @Test
    void getById_ShouldThrowException_WhenWorkoutDoesNotExistForCurrentUser() {
        String email = "user@email.com";
        Long workoutId = 99L;
        mockCurrentUser(email);

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(repository.findByIdAndUsers(workoutId, user)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(workoutId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Workout Session does not exist.");

        verify(repository).findByIdAndUsers(workoutId, user);
    }

    @Test
    void update_ShouldUpdateWorkout_WhenWorkoutBelongsToCurrentUser() {
        String email = "user@email.com";
        Long workoutId = 1L;
        mockCurrentUser(email);

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail(email);

        WorkoutSessionRequestDto requestDto =
                new WorkoutSessionRequestDto(LocalDate.of(2026, 4, 26), "Updated Push Day", List.of());

        WorkoutSessionEntity existingWorkout =
                new WorkoutSessionEntity(LocalDate.of(2026, 4, 24), "Push Day");
        existingWorkout.setId(workoutId);

        WorkoutSessionEntity rebuiltWorkout =
                new WorkoutSessionEntity(LocalDate.of(2026, 4, 26), "Updated Push Day");

        ExerciseEntity exercise = new ExerciseEntity();
        rebuiltWorkout.addExercise(exercise);

        WorkoutSessionResponseDto responseDto =
                new WorkoutSessionResponseDto(workoutId, LocalDate.of(2026, 4, 26), "Updated Push Day", List.of());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(repository.findByIdAndUsers(workoutId, user)).thenReturn(Optional.of(existingWorkout));
        when(mapper.toEntity(requestDto)).thenReturn(rebuiltWorkout);
        when(repository.save(existingWorkout)).thenReturn(existingWorkout);
        when(mapper.toDto(existingWorkout)).thenReturn(responseDto);

        WorkoutSessionResponseDto result = service.update(workoutId, requestDto);

        assertThat(existingWorkout.getName()).isEqualTo("Updated Push Day");
        assertThat(existingWorkout.getDate()).isEqualTo(LocalDate.of(2026, 4, 26));
        assertThat(result.getName()).isEqualTo("Updated Push Day");

        verify(repository).save(existingWorkout);
        verify(mapper).toDto(existingWorkout);
    }

    @Test
    void delete_ShouldDeleteWorkout_WhenWorkoutBelongsToCurrentUser() {
        String email = "user@email.com";
        Long workoutId = 1L;
        mockCurrentUser(email);

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail(email);

        WorkoutSessionEntity workout =
                new WorkoutSessionEntity(LocalDate.of(2026, 4, 24), "Push Day");
        workout.setId(workoutId);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(repository.findByIdAndUsers(workoutId, user)).thenReturn(Optional.of(workout));

        service.delete(workoutId);

        verify(repository).delete(workout);
    }

    @Test
    void delete_ShouldThrowException_WhenWorkoutDoesNotExistForCurrentUser() {
        String email = "user@email.com";
        Long workoutId = 99L;
        mockCurrentUser(email);

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(repository.findByIdAndUsers(workoutId, user)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(workoutId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Workout Session does not exist.");

        verify(repository, never()).delete(any());
    }

    @Test
    void getAll_ShouldThrowException_WhenNoAuthenticationExists() {
        assertThatThrownBy(() -> service.getAll())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User could not be identified.");

        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void getAll_ShouldThrowException_WhenCurrentUserNotFound() {
        String email = "missing@email.com";
        mockCurrentUser(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getAll())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found.");

        verify(userRepository).findByEmail(email);
    }

    private void mockCurrentUser(String email) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        AuthorityUtils.createAuthorityList("ROLE_USER")
                )
        );
    }
}
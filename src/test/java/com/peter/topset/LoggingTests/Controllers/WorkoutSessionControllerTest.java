package com.peter.topset.LoggingTests.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peter.topset.Logging.WorkoutSession.Dtos.WorkoutSessionRequestDto;
import com.peter.topset.Logging.WorkoutSession.Dtos.WorkoutSessionResponseDto;
import com.peter.topset.Logging.WorkoutSession.Service.WorkoutSessionService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class WorkoutSessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WorkoutSessionService service;

    @Test
    void getAll_ShouldReturnAllWorkouts() throws Exception {
        List<WorkoutSessionResponseDto> workouts = List.of(
                new WorkoutSessionResponseDto(1L, LocalDate.of(2026, 4, 24), "Push Day", List.of()),
                new WorkoutSessionResponseDto(2L, LocalDate.of(2026, 4, 25), "Pull Day", List.of())
        );

        when(service.getAll()).thenReturn(workouts);

        mockMvc.perform(get("/api/v1/workouts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Push Day"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Pull Day"));

        verify(service).getAll();
    }

    @Test
    void getById_ShouldReturnWorkout() throws Exception {
        Long workoutId = 1L;

        WorkoutSessionResponseDto responseDto =
                new WorkoutSessionResponseDto(1L, LocalDate.of(2026, 4, 24), "Push Day", List.of());

        when(service.getById(workoutId)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/workouts/{id}", workoutId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Push Day"));

        verify(service).getById(workoutId);
    }

    @Test
    void createWorkout_ShouldReturnCreatedWorkout() throws Exception {
        WorkoutSessionRequestDto requestDto =
                new WorkoutSessionRequestDto(LocalDate.of(2026, 4, 24), "Push Day", List.of());

        WorkoutSessionResponseDto responseDto =
                new WorkoutSessionResponseDto(1L, LocalDate.of(2026, 4, 24), "Push Day", List.of());

        when(service.createWorkout(any(WorkoutSessionRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Push Day"));

        verify(service).createWorkout(any(WorkoutSessionRequestDto.class));
    }

    @Test
    void update_ShouldReturnUpdatedWorkout() throws Exception {
        Long workoutId = 1L;

        WorkoutSessionRequestDto requestDto =
                new WorkoutSessionRequestDto(LocalDate.of(2026, 4, 24), "Updated Push Day", List.of());

        WorkoutSessionResponseDto responseDto =
                new WorkoutSessionResponseDto(1L, LocalDate.of(2026, 4, 24), "Updated Push Day", List.of());

        when(service.update(eq(workoutId), any(WorkoutSessionRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/v1/workouts/{id}", workoutId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Push Day"));

        verify(service).update(eq(workoutId), any(WorkoutSessionRequestDto.class));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        Long workoutId = 1L;

        doNothing().when(service).delete(workoutId);

        mockMvc.perform(delete("/api/v1/workouts/{id}", workoutId))
                .andExpect(status().isNoContent());

        verify(service).delete(workoutId);
    }
}
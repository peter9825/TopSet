package com.peter.topset.UserTests.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peter.topset.Users.Dtos.UpdateUserRequestDto;
import com.peter.topset.Users.Dtos.UserResponseDto;
import com.peter.topset.Users.Service.AdminService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    AdminService service;


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllUsers() throws Exception{

        List<UserResponseDto> users = List.of(
                new UserResponseDto(1L,"user1@email.com","user123"),
                new UserResponseDto(2L, "user2@email.com","user1234")
        );

        when(service.getAll()).thenReturn(users);

        mockMvc.perform(get("/api/v1/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("user1@email.com"))
                .andExpect(jsonPath("$[0].username").value("user123"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].email").value("user2@email.com"))
                .andExpect(jsonPath("$[1].username").value("user1234"));

    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateUser() throws Exception {
        Long userId = 1L;

        UpdateUserRequestDto requestDto = new UpdateUserRequestDto("user3");

        UserResponseDto responseDto = new UserResponseDto(
                1L,
                "user1@email.com",
                "user3"
        );

        when(service.update(eq(userId), any(UpdateUserRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/v1/admin/users/{id}",userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("user1@email.com"))
                .andExpect(jsonPath("$.username").value("user3"));
    }



    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetUserById() throws Exception{

        Long userId = 1L;

        UserResponseDto user = new UserResponseDto(
                1L,
                "user1@email.com",
                "user1"
        );

        when(service.getById(userId)).thenReturn(user);


        mockMvc.perform(get("/api/v1/admin/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("user1@email.com"))
                .andExpect(jsonPath("$.username").value("user1"));

    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteUser() throws Exception{
        long userId = 1L;

        mockMvc.perform(delete("/api/v1/admin/users/{id}",userId))
                .andExpect(status().isNoContent());

        verify(service).delete(userId);
    }

}
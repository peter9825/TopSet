package com.peter.topset.UserTests.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peter.topset.Users.Dtos.UpdateUserRequestDto;
import com.peter.topset.Users.Dtos.UserResponseDto;
import com.peter.topset.Users.Service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    UserService service;



    @Test
    @WithMockUser(roles = "USER")
    public void testUpdateUser() throws Exception {

        UpdateUserRequestDto requestDto = new UpdateUserRequestDto("user3");

        when(service.update(any(UpdateUserRequestDto.class)))
                .thenReturn(new UserResponseDto(
                        1L,
                        "user@email.com",
                        "user3"
                ));

        mockMvc.perform(put("/api/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user3"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteUser() throws Exception {

        mockMvc.perform(delete("/api/v1/users/me"))
                .andExpect(status().isNoContent());

        verify(service).delete();
    }

}

package com.peter.topset.UserTests.Service;

import com.peter.topset.Users.Dtos.UpdateUserRequestDto;
import com.peter.topset.Users.Dtos.UserResponseDto;
import com.peter.topset.Users.Entity.UserEntity;
import com.peter.topset.Users.Repository.UserRepository;
import com.peter.topset.Users.Service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void update_ShouldUpdateCurrentUsersUsername() {
        String email = "user@email.com";

        UserEntity existingUser = new UserEntity();
        existingUser.setId(1L);
        existingUser.setEmail(email);
        existingUser.setUsername("oldName");

        UpdateUserRequestDto dto = new UpdateUserRequestDto("newName");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(email, null, AuthorityUtils.createAuthorityList("ROLE_USER"))
        );

        when(repository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        UserResponseDto result = service.update(dto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getUsername()).isEqualTo("newName");

        assertThat(existingUser.getUsername()).isEqualTo("newName");
        verify(repository).findByEmail(email);
    }

    @Test
    void delete_ShouldDeleteCurrentUser() {
        String email = "user@email.com";

        UserEntity existingUser = new UserEntity();
        existingUser.setId(1L);
        existingUser.setEmail(email);
        existingUser.setUsername("user1");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(email, null, AuthorityUtils.createAuthorityList("ROLE_USER"))
        );

        when(repository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        service.delete();

        verify(repository).findByEmail(email);
        verify(repository).delete(existingUser);
    }

    @Test
    void update_ShouldThrowException_WhenNoAuthenticationExists() {
        UpdateUserRequestDto dto = new UpdateUserRequestDto("newName");

        assertThatThrownBy(() -> service.update(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User could not be identified.");

        verify(repository, never()).findByEmail(anyString());
    }

    @Test
    void update_ShouldThrowException_WhenUserNotFound() {
        String email = "missing@email.com";

        UpdateUserRequestDto dto = new UpdateUserRequestDto("newName");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(email, null, AuthorityUtils.createAuthorityList("ROLE_USER"))
        );

        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found.");

        verify(repository).findByEmail(email);
    }
}
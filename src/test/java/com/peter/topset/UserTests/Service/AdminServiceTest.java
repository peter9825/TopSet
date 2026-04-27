package com.peter.topset.UserTests.Service;
import com.peter.topset.Users.Dtos.UpdateUserRequestDto;
import com.peter.topset.Users.Dtos.UserResponseDto;
import com.peter.topset.Users.Entity.UserEntity;
import com.peter.topset.Users.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.peter.topset.Users.Service.AdminService;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private AdminService service;

    @Test
    void getAll_ShouldReturnAllUsers() {
        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        user1.setEmail("user1@email.com");
        user1.setUsername("user1");

        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        user2.setEmail("user2@email.com");
        user2.setUsername("user2");

        when(repository.findAll()).thenReturn(List.of(user1, user2));

        List<UserResponseDto> result = service.getAll();

        assertThat(result).hasSize(2);

        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getEmail()).isEqualTo("user1@email.com");
        assertThat(result.get(0).getUsername()).isEqualTo("user1");

        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getEmail()).isEqualTo("user2@email.com");
        assertThat(result.get(1).getUsername()).isEqualTo("user2");

        verify(repository).findAll();
    }

    @Test
    void getById_ShouldReturnUser_WhenUserExists() {
        Long userId = 1L;

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEmail("user@email.com");
        user.setUsername("user1");

        when(repository.findById(userId)).thenReturn(Optional.of(user));

        UserResponseDto result = service.getById(userId);

        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getEmail()).isEqualTo("user@email.com");
        assertThat(result.getUsername()).isEqualTo("user1");

        verify(repository).findById(userId);
    }

    @Test
    void getById_ShouldThrowException_WhenUserNotFound() {
        Long userId = 99L;

        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");

        verify(repository).findById(userId);
    }

    @Test
    void update_ShouldUpdateUsername_WhenUserExists() {
        Long userId = 1L;

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEmail("user@email.com");
        user.setUsername("oldName");

        UpdateUserRequestDto dto = new UpdateUserRequestDto("newName");

        when(repository.findById(userId)).thenReturn(Optional.of(user));

        UserResponseDto result = service.update(userId, dto);

        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getEmail()).isEqualTo("user@email.com");
        assertThat(result.getUsername()).isEqualTo("newName");

        assertThat(user.getUsername()).isEqualTo("newName");

        verify(repository).findById(userId);
    }

    @Test
    void update_ShouldThrowException_WhenUserNotFound() {
        Long userId = 99L;
        UpdateUserRequestDto dto = new UpdateUserRequestDto("newName");

        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(userId, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");

        verify(repository).findById(userId);
    }

    @Test
    void delete_ShouldDeleteUserById() {
        Long userId = 1L;

        service.delete(userId);

        verify(repository).deleteById(userId);
    }
}

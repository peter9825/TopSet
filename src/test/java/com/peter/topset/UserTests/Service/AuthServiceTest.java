package com.peter.topset.UserTests.Service;

import com.peter.topset.Users.Dtos.AuthResponseDto;
import com.peter.topset.Users.Dtos.CreateUserRequestDto;
import com.peter.topset.Users.Dtos.LoginRequestDto;
import com.peter.topset.Users.Entity.RoleEntity;
import com.peter.topset.Users.Entity.UserEntity;
import com.peter.topset.Users.Repository.RoleRepository;
import com.peter.topset.Users.Repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.peter.topset.Users.Security.JwtService;
import com.peter.topset.Users.Service.AuthService;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService service;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void register_ShouldCreateUser_WhenEmailIsNotAlreadyRegistered() {
        CreateUserRequestDto dto = new CreateUserRequestDto(
                "user@email.com",
                "snake123",
                "Password123!",
                "Password123!"
        );

        RoleEntity role = new RoleEntity();
        role.setName("ROLE_USER");

        when(repository.findByEmail("user@email.com")).thenReturn(Optional.empty());
        when(encoder.encode("Password123!")).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));

        ResponseEntity<String> response = service.register(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo("user registered.");

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(repository).save(userCaptor.capture());

        UserEntity savedUser = userCaptor.getValue();

        assertThat(savedUser.getEmail()).isEqualTo("user@email.com");
        assertThat(savedUser.getUsername()).isEqualTo("snake123");
        assertThat(savedUser.getPasswordHash()).isEqualTo("encodedPassword");
        assertThat(savedUser.getRoles()).containsExactly(role);

        verify(repository).findByEmail("user@email.com");
        verify(encoder).encode("Password123!");
        verify(roleRepository).findByName("ROLE_USER");
    }

    @Test
    void register_ShouldThrowException_WhenEmailAlreadyExists() {
        CreateUserRequestDto dto = new CreateUserRequestDto(
                "user@email.com",
                "snake123",
                "Password123!",
                "Password123!"
        );

        UserEntity existingUser = new UserEntity();
        existingUser.setEmail("user@email.com");

        when(repository.findByEmail("user@email.com"))
                .thenReturn(Optional.of(existingUser));

        assertThatThrownBy(() -> service.register(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Account already exists. Please log in.");

        verify(repository).findByEmail("user@email.com");
        verify(repository, never()).save(any(UserEntity.class));
        verify(encoder, never()).encode(anyString());
        verify(roleRepository, never()).findByName(anyString());
    }

    @Test
    void register_ShouldThrowException_WhenUserRoleNotFound() {
        CreateUserRequestDto dto = new CreateUserRequestDto(
                "user@email.com",
                "snake123",
                "Password123!",
                "Password123!"
        );

        when(repository.findByEmail("user@email.com")).thenReturn(Optional.empty());
        when(encoder.encode("Password123!")).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.register(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("user role not found.");

        verify(repository).findByEmail("user@email.com");
        verify(encoder).encode("Password123!");
        verify(roleRepository).findByName("ROLE_USER");
        verify(repository, never()).save(any(UserEntity.class));
    }

    @Test
    void login_ShouldAuthenticateUserAndReturnJwt() {
        LoginRequestDto dto = new LoginRequestDto(
                "user@email.com",
                "Password123!"
        );

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(jwtService.generateToken(authentication))
                .thenReturn("fake.jwt.token");

        ResponseEntity<AuthResponseDto> response = service.login(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAccessToken()).isEqualTo("fake.jwt.token");

        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isEqualTo(authentication);

        ArgumentCaptor<UsernamePasswordAuthenticationToken> tokenCaptor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);

        verify(authenticationManager).authenticate(tokenCaptor.capture());

        UsernamePasswordAuthenticationToken authToken = tokenCaptor.getValue();

        assertThat(authToken.getPrincipal()).isEqualTo("user@email.com");
        assertThat(authToken.getCredentials()).isEqualTo("Password123!");

        verify(jwtService).generateToken(authentication);
    }
}

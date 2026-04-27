package com.peter.topset.Users.Service;

import com.peter.topset.Users.Dtos.*;
import com.peter.topset.Users.Entity.RoleEntity;
import com.peter.topset.Users.Entity.UserEntity;
import com.peter.topset.Users.Repository.RoleRepository;
import com.peter.topset.Users.Repository.UserRepository;
import com.peter.topset.Users.Security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class AuthService {

private final UserRepository repository;

private final PasswordEncoder encoder;

private final RoleRepository roleRepository;

private final AuthenticationManager authenticationManager;

private final JwtService jwtService;

public AuthService(UserRepository repository, PasswordEncoder encoder, RoleRepository roleRepository,
                   AuthenticationManager authenticationManager, JwtService jwtService){
    this.repository = repository;
    this.encoder = encoder;
    this.roleRepository = roleRepository;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
}


@Transactional
public ResponseEntity<String> register (CreateUserRequestDto dto){
    if (repository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Account already exists. Please log in.");
    }

    UserEntity newUser = new UserEntity();
    newUser.setEmail(dto.getEmail());
    newUser.setUsername(dto.getUsername());
    newUser.setPasswordHash(encoder.encode(dto.getPassword()));

    RoleEntity role = roleRepository.findByName("ROLE_USER")
            .orElseThrow(() -> new RuntimeException("user role not found."));

    newUser.setRoles(Collections.singletonList(role));

    repository.save(newUser);
    return new ResponseEntity<>("user registered.",HttpStatus.CREATED);
}


@Transactional
public ResponseEntity<AuthResponseDto> login (LoginRequestDto dto){

    Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String token = jwtService.generateToken(authentication);
    return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
}

}

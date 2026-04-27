package com.peter.topset.Users.Service;

import com.peter.topset.Users.Dtos.UpdateUserRequestDto;
import com.peter.topset.Users.Dtos.UserResponseDto;
import com.peter.topset.Users.Entity.UserEntity;
import com.peter.topset.Users.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

private final UserRepository repository;

public AdminService(UserRepository repository){
    this.repository = repository;
}

    @Transactional
    public List<UserResponseDto> getAll(){

        return repository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public UserResponseDto getById(Long id) {
        UserEntity existingUser = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return toDto(existingUser);

    }


    @Transactional
    public UserResponseDto update(Long id, UpdateUserRequestDto dto){

        UserEntity existingUser = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setUsername(dto.getUsername());

        return toDto(existingUser);
    }


    @Transactional
    public void delete(Long id){
        repository.deleteById(id);
    }



    public UserResponseDto toDto(UserEntity e){
        return new UserResponseDto(e.getId(),e.getEmail(), e.getUsername());
    }

}

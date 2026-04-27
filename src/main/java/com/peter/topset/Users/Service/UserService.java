package com.peter.topset.Users.Service;

import com.peter.topset.Users.Dtos.UpdateUserRequestDto;
import com.peter.topset.Users.Dtos.UserResponseDto;
import com.peter.topset.Users.Entity.UserEntity;
import com.peter.topset.Users.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository){
        this.repository = repository;
    }



    @Transactional
    public UserResponseDto update(UpdateUserRequestDto dto){

        UserEntity existingUser = getCurrentUser();

        existingUser.setUsername(dto.getUsername());

        return toDto(existingUser);
    }


    @Transactional
    public void delete(){

        UserEntity existingUser = getCurrentUser();
        repository.delete(existingUser);
    }



    private UserEntity getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {

            throw new RuntimeException("User could not be identified.");
        }
        UserEntity currentUser = repository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found."));

        return currentUser;

    }



    public UserResponseDto toDto(UserEntity e){
        return new UserResponseDto(e.getId(),e.getEmail(),e.getUsername());
    }
}

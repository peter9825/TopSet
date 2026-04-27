package com.peter.topset.Users.Dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {

    private Long id;
    private String email;
    private String username;


    public UserResponseDto(Long id, String email, String username){
        this.id = id;
        this.email = email;
        this.username = username;

    }


}

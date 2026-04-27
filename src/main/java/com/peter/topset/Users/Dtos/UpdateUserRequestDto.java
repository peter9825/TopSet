package com.peter.topset.Users.Dtos;

import lombok.Getter;

//allow users to change these fields in their account once registered.
@Getter
public class UpdateUserRequestDto {
    private String username;

    public UpdateUserRequestDto(String username) {
        this.username = username;
    }
}

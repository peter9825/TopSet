package com.peter.topset.Users.Controller;

import com.peter.topset.Users.Dtos.UpdateUserRequestDto;
import com.peter.topset.Users.Dtos.UserResponseDto;
import com.peter.topset.Users.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/me")
public class UserController {

    private final UserService service;

    public UserController(UserService service){
        this.service = service;

    }

    @PutMapping
    public UserResponseDto update(@RequestBody UpdateUserRequestDto dto){
        return service.update(dto);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    public void delete(){
        service.delete();
    }

}


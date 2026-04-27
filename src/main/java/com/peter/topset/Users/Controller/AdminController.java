package com.peter.topset.Users.Controller;

import com.peter.topset.Users.Dtos.UpdateUserRequestDto;
import com.peter.topset.Users.Dtos.UserResponseDto;
import com.peter.topset.Users.Service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
public class AdminController {

private final AdminService service;

public AdminController(AdminService service){
    this.service = service;
}


@GetMapping
public List<UserResponseDto> getAll(){
    return service.getAll();
}


@GetMapping("/{id}")
public UserResponseDto getById(@PathVariable Long id){
    return service.getById(id);
}


@PutMapping("/{id}")
public UserResponseDto update(@PathVariable Long id, @RequestBody UpdateUserRequestDto dto){
    return service.update(id,dto);
}


@ResponseStatus(HttpStatus.NO_CONTENT)
@DeleteMapping("/{id}")
public void delete(@PathVariable Long id){
    service.delete(id);
}

}

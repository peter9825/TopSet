package com.peter.topset.Users.Controller;
import com.peter.topset.Users.Dtos.AuthResponseDto;
import com.peter.topset.Users.Dtos.CreateUserRequestDto;
import com.peter.topset.Users.Dtos.LoginRequestDto;
import com.peter.topset.Users.Service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service){

        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody CreateUserRequestDto dto){
        return service.register(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto dto){
        return service.login(dto);

    }

    /* DEBUG
    @GetMapping("/whoami")
    public String whoami(Authentication authentication) {
        return authentication == null
                ? "null"
                : authentication.getName() + " " + authentication.getAuthorities();
    }
*/

}

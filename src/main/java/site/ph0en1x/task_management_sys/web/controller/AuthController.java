package site.ph0en1x.task_management_sys.web.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ph0en1x.task_management_sys.model.auth.JwtRequest;
import site.ph0en1x.task_management_sys.model.auth.JwtResponse;
import site.ph0en1x.task_management_sys.model.user.User;
import site.ph0en1x.task_management_sys.model.user.UserDto;
import site.ph0en1x.task_management_sys.model.user.UserMapper;
import site.ph0en1x.task_management_sys.service.AuthService;
import site.ph0en1x.task_management_sys.service.UserService;
import site.ph0en1x.task_management_sys.web.validation.onCreate;

@Tag(name = "Auth controller", description = "Auth API v1")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody JwtRequest loginRequest) {
        log.debug("Get login request with login:{} and password: {}"
                , loginRequest.getUsername(), loginRequest.getPassword());
        return authService.login(loginRequest);
    }

    @PostMapping("/registration")
    public UserDto registration(
            @Validated(onCreate.class)
            @RequestBody UserDto dto) {
        log.debug("Get registration request with login:{} and password: {}"
                , dto.getEmail(), dto.getPassword());
        User user = userMapper.toEntity(dto);
        User createdUser = userService.create(user);
        return userMapper.toDto(createdUser);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody String refreshToken) {
        log.debug("receive refresh token {}", refreshToken);
        return authService.refresh(refreshToken);
    }
}

package site.ph0en1x.task_management_sys.web.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.ph0en1x.task_management_sys.model.user.*;
import site.ph0en1x.task_management_sys.model.user.UserMapper;
import site.ph0en1x.task_management_sys.service.impl.UserServiceImpl;
import site.ph0en1x.task_management_sys.web.validation.onUpdate;


@Tag(name = "User controller", description = "User API v1")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
@Validated
public class UserController {

    private final UserServiceImpl service;

    private final UserMapper mapper;

    @PutMapping
    @Operation(summary = "Update user")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#dto.id)")
    public UserDto update(@Validated(onUpdate.class) @RequestBody UserDto dto) {
        User updated = service.update(mapper.toEntity(dto));
        return mapper.toDto(updated);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user info by id")
    public UserDto getUserById(@PathVariable Long id) {
        return mapper.toDto(service.getById(id));
    }

}

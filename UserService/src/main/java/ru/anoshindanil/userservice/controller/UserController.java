package ru.anoshindanil.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.anoshindanil.userservice.dto.UserCreateRequestDto;
import ru.anoshindanil.userservice.dto.UserResponseDto;
import ru.anoshindanil.userservice.dto.UserUpdateRequestDto;
import ru.anoshindanil.userservice.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "Методы работы с пользователями")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Создать пользователя (только для AuthService)")
    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserCreateRequestDto request) {
        userService.createUser(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить профиль пользователя")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) {
        UserResponseDto userById = userService.getUserById(id);
        return ResponseEntity.ok(userById);
    }

    @Operation(summary = "Поиск пользователей")
    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> searchUser(@RequestParam String query) {
        List<UserResponseDto> userList = userService.searchUsers(query);
        return ResponseEntity.ok(userList);
    }

    @Operation(summary = "Обновить профиль пользователя")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable UUID id, @RequestBody UserUpdateRequestDto request) {
        UserResponseDto updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }
}

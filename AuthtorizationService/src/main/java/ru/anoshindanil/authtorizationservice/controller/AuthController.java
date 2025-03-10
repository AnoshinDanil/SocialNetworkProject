package ru.anoshindanil.authtorizationservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.anoshindanil.authtorizationservice.dto.LoginRequestDto;
import ru.anoshindanil.authtorizationservice.model.AuthResponse;
import ru.anoshindanil.authtorizationservice.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "AuthController", description = "Методы аутентификации и регистрации")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Вход в систему")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Создание реквезитов пользователя")
    @PostMapping("/credentials-request")
    public ResponseEntity<Void> createCredentials(@RequestBody LoginRequestDto request) {
        authService.createUserCredentials(request);
        return ResponseEntity.ok().build();
    }
}

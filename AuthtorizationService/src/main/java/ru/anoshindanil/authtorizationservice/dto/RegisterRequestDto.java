package ru.anoshindanil.authtorizationservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDto {

    @NotBlank
    @Schema(description = "Имя пользователя", example = "test_user")
    private String username;

    @Email
    @Schema(description = "Email пользователя", example = "test@example.com")
    private String email;

    @NotBlank
    @Schema(description = "Пароль", example = "mypassword123")
    private String password;
}

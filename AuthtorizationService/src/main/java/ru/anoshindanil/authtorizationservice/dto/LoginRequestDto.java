package ru.anoshindanil.authtorizationservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    @Email
    @Schema(description = "Email пользователя", example = "test@example.com")
    private String email;

    @NotBlank
    @Schema(description = "Пароль", example = "mypassword123")
    private String password;
}

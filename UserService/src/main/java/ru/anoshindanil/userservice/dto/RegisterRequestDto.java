package ru.anoshindanil.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

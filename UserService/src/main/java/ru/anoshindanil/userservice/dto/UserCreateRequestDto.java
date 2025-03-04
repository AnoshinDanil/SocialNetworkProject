package ru.anoshindanil.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequestDto {

    @NotBlank
    @Schema(description = "Имя пользователя (логин)", example = "johndoe")
    private String username;

    @Email
    @Schema(description = "Email пользователя", example = "test@example.com")
    private String email;
}

package ru.anoshindanil.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDto {

    @Schema(description = "Имя пользователя", example = "test_user")
    private String name;

    @Schema(description = "Био пользователя", example = "Java-разработчик, люблю путешествия")
    private String bio;

    @Schema(description = "URL-адрес аватара пользователя", example = "https://example.com/avatar.jpg")
    private String avatarUrl;
}

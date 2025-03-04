package ru.anoshindanil.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ru.anoshindanil.userservice.entity.User;

@Getter
@Setter
public class UserResponseDto {

    @Schema(description = "Имя пользователя (логин)", example = "johndoe")
    private String username;

    @Schema(description = "Email пользователя", example = "test@example.com")
    private String email;

    @Schema(description = "Имя пользователя", example = "test_user")
    private String name;

    @Schema(description = "Био пользователя", example = "Java-разработчик, люблю путешествия")
    private String bio;

    @Schema(description = "URL-адрес аватара пользователя", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    public UserResponseDto(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.bio = user.getBio();
        this.avatarUrl = user.getAvatarUrl();
    }
}

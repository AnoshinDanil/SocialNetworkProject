package ru.anoshindanil.userservice.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность пользователя")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Уникальный идентификатор пользователя", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Column(unique = true, nullable = false)
    @Schema(description = "Имя пользователя (логин)", example = "johndoe")
    private String username;

    @Column(unique = true, nullable = false)
    @Schema(description = "Электронная почта пользователя", example = "johndoe@example.com")
    private String email;

    @Column(name="password_hash", nullable=false)
    @Schema(description = "Хэш пароля пользователя")
    private String passwordHash;

    @Column
    @Schema(description = "Полное имя пользователя", example = "John Doe")
    private String name;

    @Column
    @Schema(description = "Краткое описание профиля", example = "Java-разработчик, люблю путешествия")
    private String bio;

    @Column(name="avatar")
    @Schema(description = "URL-адрес аватара пользователя", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    @Column(name="created_at")
    @Schema(description = "Дата и время создания пользователя", example = "2024-02-29T12:00:00")
    private LocalDateTime createdAt = LocalDateTime.now();
}


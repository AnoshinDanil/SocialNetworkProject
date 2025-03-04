package ru.anoshindanil.authtorizationservice.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.anoshindanil.authtorizationservice.enums.Role;

import java.util.UUID;

@Entity
@Table(name = "user_credentials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность заявки пользователя")
public class UserCredentials {

    @Id
    @Schema(description = "Уникальный идентификатор пользователя", example = "550e8400-e29b-41d4-a716-446655440000")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    @Schema(description = "Электронная почта пользователя", example = "johndoe@example.com")
    private String email;

    @Column(name="password_hash", nullable=false)
    @Schema(description = "Хэш пароля пользователя")
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Роль пользователя", example = "USER")
    private Role role;
}

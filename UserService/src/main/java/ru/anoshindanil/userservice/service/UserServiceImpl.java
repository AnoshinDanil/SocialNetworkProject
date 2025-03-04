package ru.anoshindanil.userservice.service;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.anoshindanil.userservice.dto.UserCreateRequestDto;
import ru.anoshindanil.userservice.dto.UserResponseDto;
import ru.anoshindanil.userservice.dto.UserUpdateRequestDto;
import ru.anoshindanil.userservice.entity.User;
import ru.anoshindanil.userservice.exceptions.UserNotFoundException;
import ru.anoshindanil.userservice.repository.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Tag(name = "Пользователи", description = "Методы для работы с пользователями")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Hidden
    @Override
    @Operation(
            summary = "Создать профиль пользователя",
            description = "Сохраняет пользователя в базу данных (только для AuthService)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное создание пользователя"),
                    @ApiResponse(responseCode = "400", description = "Неверные данные")
            }
    )
    public void createUser(UserCreateRequestDto request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        userRepository.save(user);
    }


    @Override
    @Operation(
            summary = "Получить профиль пользователя",
            description = "Возвращает информацию о пользователе по его уникальному идентификатору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный поиск и получение пользователя"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            }
    )
    public UserResponseDto getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return new UserResponseDto(user);
    }

    @Override
    @Operation(
            summary = "Поиск пользователей",
            description = "Ищет пользователей по части имени. Возвращает список найденных пользователей или пустой массив",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список найденных пользователей (может быть пустым)"),
                    @ApiResponse(responseCode = "404", description = "Пользователи не найден")
            }
    )
    public List<UserResponseDto> searchUsers(String query) {
        List<User> users = userRepository.findByUsernameContainingIgnoreCase(query);
        return users.stream().map(UserResponseDto::new).collect(Collectors.toList());
    }


    @Override
    @Operation(
            summary = "Обновление профиля пользователя",
            description = "Позволяет обновить данные о пользователе (имя, био, аватар)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные успешно обновлены"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            }
    )
    public UserResponseDto updateUser(UUID userId, UserUpdateRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setName(request.getName());
        user.setBio(request.getBio());
        user.setAvatarUrl(request.getAvatarUrl());

        userRepository.save(user);
        return new UserResponseDto(user);
    }
}

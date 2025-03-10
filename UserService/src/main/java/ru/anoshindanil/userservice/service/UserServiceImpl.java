package ru.anoshindanil.userservice.service;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.anoshindanil.userservice.dto.RegisterCredentialsRequestDto;
import ru.anoshindanil.userservice.dto.RegisterRequestDto;
import ru.anoshindanil.userservice.dto.UserResponseDto;
import ru.anoshindanil.userservice.dto.UserUpdateRequestDto;
import ru.anoshindanil.userservice.entity.User;
import ru.anoshindanil.userservice.exceptions.UserAlreadyExistsException;
import ru.anoshindanil.userservice.exceptions.UserNotFoundException;
import ru.anoshindanil.userservice.repository.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "Методы для работы с пользователями")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    @Hidden
    @Operation(
            summary = "Создать профиль пользователя",
            description = "Сохраняет пользователя в базу данных",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное создание пользователя"),
                    @ApiResponse(responseCode = "400", description = "Неверные данные")
            }
    )
    @Override
    public UserResponseDto register(RegisterRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email уже используется");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        createUserCredentials(request.getEmail(), request.getPassword());
        return new UserResponseDto(user);
    }

    private void createUserCredentials(String email, String password) {
        String authServiceUrl = "http://localhost:8081/api/auth/credentials-request";
        RegisterCredentialsRequestDto request = new RegisterCredentialsRequestDto(email, password);

        try {
            restTemplate.postForEntity(authServiceUrl, request, Void.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании учетных данных в AuthService", e);
        }
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
    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return new UserResponseDto(user);
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
        List<User> users = userRepository.findByUsernameContainingIgnoreCase(query).orElseThrow(() -> new UserNotFoundException("Users not found"));
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

package ru.anoshindanil.authtorizationservice.service;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.anoshindanil.authtorizationservice.dto.LoginRequestDto;
import ru.anoshindanil.authtorizationservice.dto.RegisterRequestDto;
import ru.anoshindanil.authtorizationservice.entity.User;
import ru.anoshindanil.authtorizationservice.enums.Role;
import ru.anoshindanil.authtorizationservice.model.AuthResponse;
import ru.anoshindanil.authtorizationservice.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Tag(name = "Аутентификация и регистрация", description = "Сервис для аутентификации пользователей и регистрации новых аккаунтов")
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Operation(
            summary = "Логин пользователя",
            description = "Выполняет аутентификацию пользователя по email и паролю, возвращает JWT токен.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешная аутентификация и получение токена"),
                    @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
            }
    )
    public AuthResponse login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid password");
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }

    @Override
    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Регистрирует нового пользователя, создает аккаунт и возвращает JWT токен.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешная регистрация и получение токена"),
                    @ApiResponse(responseCode = "400", description = "Пользователь с таким email уже существует")
            }
    )
    public AuthResponse register(RegisterRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}

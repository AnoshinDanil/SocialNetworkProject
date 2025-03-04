package ru.anoshindanil.authtorizationservice.service;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;
import ru.anoshindanil.authtorizationservice.dto.LoginRequestDto;
import ru.anoshindanil.authtorizationservice.dto.RegisterRequestDto;
import ru.anoshindanil.authtorizationservice.dto.UserCreateRequestDto;
import ru.anoshindanil.authtorizationservice.entity.UserCredentials;
import ru.anoshindanil.authtorizationservice.enums.Role;
import ru.anoshindanil.authtorizationservice.model.AuthResponse;
import ru.anoshindanil.authtorizationservice.repository.UserCredentialsRepository;

@Service
@RequiredArgsConstructor
@Tag(name = "Операции аутентификации и регистрации", description = "Сервис для аутентификации пользователей и регистрации новых аккаунтов")
public class AuthServiceImpl implements AuthService {
    private final UserCredentialsRepository userCredentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RestTemplate restTemplate;

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
        UserCredentials user = userCredentialsRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail(),user.getRole());

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
        if (userCredentialsRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email уже используется");
        }

        UserCredentials credentials = new UserCredentials();
        credentials.setEmail(request.getEmail());
        credentials.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        credentials.setRole(Role.USER);

        userCredentialsRepository.save(credentials);

        UserCreateRequestDto userCreateRequest = new UserCreateRequestDto(request.getUsername(), request.getEmail());

        String userServiceUrl = "localhost:8082/api/users/create";
        restTemplate.postForObject(userServiceUrl, userCreateRequest, Void.class);

        String token = jwtService.generateToken(credentials.getEmail(),credentials.getRole());

        return new AuthResponse(token);
    }
}

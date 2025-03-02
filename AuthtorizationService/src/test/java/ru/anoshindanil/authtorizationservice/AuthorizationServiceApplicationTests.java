package ru.anoshindanil.authtorizationservice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.anoshindanil.authtorizationservice.dto.LoginRequestDto;
import ru.anoshindanil.authtorizationservice.dto.RegisterRequestDto;
import ru.anoshindanil.authtorizationservice.entity.User;
import ru.anoshindanil.authtorizationservice.enums.Role;
import ru.anoshindanil.authtorizationservice.model.AuthResponse;
import ru.anoshindanil.authtorizationservice.repository.UserRepository;
import ru.anoshindanil.authtorizationservice.service.AuthService;
import ru.anoshindanil.authtorizationservice.service.JwtService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag(name = "AuthorizationServiceTest", description = "Тесты для проверки регистрации и логина")
class AuthorizationServiceApplicationTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequestDto registerRequest;
    private LoginRequestDto loginRequest;
    private User user;
    private String encodedPassword = "encoded_password";
    private final String jwtToken = "mocked_jwt_token";

    @BeforeEach
    @Operation(summary = "Создание дто и юзера", description = "Создаем дто и экземпляр юзера")
    void setUp() {
        registerRequest = new RegisterRequestDto("testUser", "test@example.com", "password123");
        loginRequest = new LoginRequestDto("test@example.com", "password123");

        user = new User(
                UUID.randomUUID(),
                "johndoe",
                "johndoe@example.com",
                "hashedPassword",
                "John Doe",
                "Java-разработчик, люблю путешествия",
                "https://example.com/avatar.jpg",
                LocalDateTime.now(),
                Role.USER
        );
    }

    @Test
    @Operation(summary = "Тест register_Success", description = "Тест проверяет поведение метода register и возвращает токен")
    void register_Success() {
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn(encodedPassword);
        when(jwtService.generateToken(any(User.class))).thenReturn(jwtToken);

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals(jwtToken, response.getToken());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @Operation(summary = "Тест register_UserAlreadyExists", description = "Тест проверяет поведение метода register в случае, если пользователь с таким email уже существует, и ожидает ошибку")
    void register_UserAlreadyExists() {
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> authService.register(registerRequest));
        assertEquals("User with this email already exists", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @Operation(summary = "Тест login_Success", description = "Тест проверяет поведение метода login и возвращает токен")
    void login_Success() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn(jwtToken);

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals(jwtToken, response.getToken());

        verify(jwtService, times(1)).generateToken(user);
    }

    @Test
    @Operation(summary = "Тест login_UserNotFound", description = "Тест проверяет поведение метода login и в случае, если пользователь не найден, ожидает ошибку")
    void login_UserNotFound() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    @Operation(summary = "Тест login_InvalidPassword", description = "Тест проверяет поведение метода login и в случае, если пароль не правильный, ожидает ошибку")
    void login_InvalidPassword() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())).thenReturn(false);

        Exception exception = assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
        assertEquals("Invalid password", exception.getMessage());
    }
}

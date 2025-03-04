package ru.anoshindanil.authtorizationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.anoshindanil.authtorizationservice.dto.LoginRequestDto;
import ru.anoshindanil.authtorizationservice.dto.RegisterRequestDto;
import ru.anoshindanil.authtorizationservice.entity.UserCredentials;
import ru.anoshindanil.authtorizationservice.enums.Role;
import ru.anoshindanil.authtorizationservice.repository.UserCredentialsRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Tag(name = "AuthorizationControllerTest", description = "Тесты для проверки контроллера")
class AuthorizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    @Operation(summary = "Очистка репозитория перед запуском тестов")
    void setUp() {
        userCredentialsRepository.deleteAll();
    }

    @Test
    @Operation(summary = "Тест register_Success", description = "Тест проверяет поведение метода register и возвращает токен")
    void register_Success() throws Exception {
        RegisterRequestDto request = new RegisterRequestDto("testUser", "test@example.com", "password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @Operation(summary = "Тест register_UserAlreadyExists", description = "Тест проверяет поведение метода register в случае, если пользователь с таким email уже существует, и ожидает ошибку")
    void register_UserAlreadyExists() throws Exception {
        userCredentialsRepository.save(
                new UserCredentials(
                        null,
                        "test@example.com",
                        passwordEncoder.encode("password123"),
                        Role.USER
                )
        );

        RegisterRequestDto request = new RegisterRequestDto("testUser", "test@example.com", "password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Operation(summary = "Тест login_Success", description = "Тест проверяет поведение метода login и возвращает токен")
    void login_Success() throws Exception {
        userCredentialsRepository.save(
                new UserCredentials(
                        null,
                        "johndoe@example.com",
                        passwordEncoder.encode("password123"),
                        Role.USER
                )
        );

        LoginRequestDto request = new LoginRequestDto("johndoe@example.com", "password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @Operation(summary = "Тест login_UserNotFound", description = "Тест проверяет поведение метода login и в случае, если пользователь не найден, ожидает ошибку")
    void login_UserNotFound() throws Exception {
        LoginRequestDto request = new LoginRequestDto("notfound@example.com", "password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}

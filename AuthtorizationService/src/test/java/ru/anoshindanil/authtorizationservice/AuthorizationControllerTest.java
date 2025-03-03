package ru.anoshindanil.authtorizationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.anoshindanil.authtorizationservice.dto.LoginRequestDto;
import ru.anoshindanil.authtorizationservice.dto.RegisterRequestDto;
import ru.anoshindanil.authtorizationservice.entity.User;
import ru.anoshindanil.authtorizationservice.enums.Role;
import ru.anoshindanil.authtorizationservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthorizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void register_Success() throws Exception {
        RegisterRequestDto request = new RegisterRequestDto("testUser", "test@example.com", "password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void register_UserAlreadyExists() throws Exception {
        userRepository.save(
                new User(
                        null,
                        "johndoe",
                        "test@example.com",
                        passwordEncoder.encode("password123"),
                        "John Doe",
                        "Java-разработчик, люблю путешествия",
                        "https://example.com/avatar.jpg",
                        LocalDateTime.now(),
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
    void login_Success() throws Exception {
        userRepository.save(
                new User(
                        null,
                        "johndoe",
                        "johndoe@example.com",
                        passwordEncoder.encode("password123"),
                        "John Doe",
                        "Java-разработчик, люблю путешествия",
                        "https://example.com/avatar.jpg",
                        LocalDateTime.now(),
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
    void login_UserNotFound() throws Exception {
        LoginRequestDto request = new LoginRequestDto("notfound@example.com", "password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}

package ru.anoshindanil.userservice.config;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
    @Bean
    @Operation(summary = "Получить PasswordEncoder", description = "Создаёт и возвращает BCryptPasswordEncoder.")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

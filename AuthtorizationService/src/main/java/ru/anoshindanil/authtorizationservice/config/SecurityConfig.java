package ru.anoshindanil.authtorizationservice.config;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.anoshindanil.authtorizationservice.filter.JwtFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Tag(name = "Security", description = "Настройки безопасности приложения")
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    @Operation(summary = "Настройка безопасности", description = "Отключает CSRF, настраивает JWT и разрешает Swagger.")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // отключение CSRF защиты
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                ) //Всё, что по пути /api/auth/** не требует авторизации
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // сессии отключаем, будут jwt
                .exceptionHandling(
                        exception -> exception
                                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                ) //настройка обработчика ошибок. Пусть возвращает 401
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); //фильтр jwt токена на каждый запрос

        return http.build();
    }

    @Bean
    @Operation(summary = "Получить PasswordEncoder", description = "Создаёт и возвращает BCryptPasswordEncoder.")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

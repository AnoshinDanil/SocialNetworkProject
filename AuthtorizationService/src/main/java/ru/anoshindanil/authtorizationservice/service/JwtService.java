package ru.anoshindanil.authtorizationservice.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.anoshindanil.authtorizationservice.entity.User;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@Tag(name = "JWT аутентификация", description = "Сервис для работы с JWT токенами: генерация и валидация.")
public class JwtService {

    @Value("${jwt.expression}")
    private Long jwtExpression;

    @Value("${jwt.secretkey}")
    private String jwtSecretKey;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
    }

    @Operation(
            summary = "Генерация JWT токена",
            description = "Генерирует JWT токен для пользователя с ролью и временем действия.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Токен успешно сгенерирован")
            }
    )
    public String generateToken(@Parameter(description = "Пользователь для генерации токена")User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpression))
                .signWith(secretKey)
                .compact();
    }

    @Operation(
            summary = "Валидация JWT токена",
            description = "Проверяет JWT токен на валидность и возвращает его данные (claims).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Токен валиден"),
                    @ApiResponse(responseCode = "400", description = "Неверный или истекший токен")
            }
    )
    public Claims tokenValid(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Invalid JWT token");
        }
    }
}

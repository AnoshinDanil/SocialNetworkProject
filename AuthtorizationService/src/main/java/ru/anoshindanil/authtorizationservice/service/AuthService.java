package ru.anoshindanil.authtorizationservice.service;

import ru.anoshindanil.authtorizationservice.dto.LoginRequestDto;
import ru.anoshindanil.authtorizationservice.model.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequestDto request);
    void createUserCredentials(LoginRequestDto request);
}

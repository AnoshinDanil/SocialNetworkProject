package ru.anoshindanil.userservice.service;

import ru.anoshindanil.userservice.dto.RegisterRequestDto;
import ru.anoshindanil.userservice.dto.UserResponseDto;
import ru.anoshindanil.userservice.dto.UserUpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDto register(RegisterRequestDto request);
    UserResponseDto getUserByEmail(String email);
    UserResponseDto getUserById(UUID id);
    List<UserResponseDto> searchUsers(String query);
    UserResponseDto updateUser(UUID userId, UserUpdateRequestDto request);
}

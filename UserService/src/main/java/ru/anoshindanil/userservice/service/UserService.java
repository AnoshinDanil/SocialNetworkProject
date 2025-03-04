package ru.anoshindanil.userservice.service;

import ru.anoshindanil.userservice.dto.UserCreateRequestDto;
import ru.anoshindanil.userservice.dto.UserResponseDto;
import ru.anoshindanil.userservice.dto.UserUpdateRequestDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void createUser(UserCreateRequestDto request);
    UserResponseDto getUserById(UUID userId);
    List<UserResponseDto> searchUsers(String query);
    UserResponseDto updateUser(UUID userId, UserUpdateRequestDto request);
}

package ru.anoshindanil.authtorizationservice.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.anoshindanil.authtorizationservice.entity.UserCredentials;
import ru.anoshindanil.authtorizationservice.repository.UserCredentialsRepository;

@Service
@AllArgsConstructor
@Tag(name = "Аутентификация", description = "Сервис для загрузки данных пользователя")
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserCredentialsRepository userRepository;

    @Override
    @Operation(summary = "Загрузка пользователя по почте(используется для логина)", description = "Ищет пользователя в базе данных и возвращает UserDetails для аутентификации.")
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserCredentials user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        return new UserDetailsImpl(user.getEmail(), user.getPasswordHash(), user.getRole());
    }
}

package ru.anoshindanil.authtorizationservice.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.anoshindanil.authtorizationservice.entity.User;
import ru.anoshindanil.authtorizationservice.repository.UserRepository;

@Service
@AllArgsConstructor
@Tag(name = "Аутентификация", description = "Сервис для загрузки данных пользователя")
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    @Operation(summary = "Загрузка пользователя по имени", description = "Ищет пользователя в базе данных и возвращает UserDetails для аутентификации.")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new UserDetailsImpl(user.getUsername(), user.getPasswordHash(), user.getRole());
    }
}

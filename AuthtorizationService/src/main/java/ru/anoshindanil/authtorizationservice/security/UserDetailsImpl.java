package ru.anoshindanil.authtorizationservice.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.anoshindanil.authtorizationservice.enums.Role;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
@Schema(description = "Модель пользователя для Spring Security")
public class UserDetailsImpl implements UserDetails {

    @Schema(description = "Имя пользователя (логин)", example = "johndoe")
    private String username;

    @Schema(description = "Хэш пароля пользователя")
    private String password;

    @Schema(description = "Роль пользователя", example = "USER")
    private Role role;

    public UserDetailsImpl(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}

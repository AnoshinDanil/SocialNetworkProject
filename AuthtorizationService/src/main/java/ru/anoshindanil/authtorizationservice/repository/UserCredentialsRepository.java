package ru.anoshindanil.authtorizationservice.repository;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.anoshindanil.authtorizationservice.entity.UserCredentials;

import java.util.Optional;

@Repository
@Tag(name = "UserCredentialsRepository", description = "Репозиторий с реквезитами пользователей")
public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Integer> {
    Optional<UserCredentials> findByEmail(String email);
}

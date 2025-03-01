package ru.anoshindanil.authtorizationservice.repository;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.anoshindanil.authtorizationservice.entity.User;

import java.util.Optional;

@Repository
@Tag(name = "UserRepository", description = "Операции с пользователями")
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}

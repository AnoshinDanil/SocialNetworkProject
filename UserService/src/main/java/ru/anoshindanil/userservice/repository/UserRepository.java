package ru.anoshindanil.userservice.repository;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.anoshindanil.userservice.entity.User;

import java.util.List;
import java.util.UUID;

@Repository
@Tag(name = "UserRepository", description = "Операции с пользователями")
public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findByUsernameContainingIgnoreCase(String username);
}

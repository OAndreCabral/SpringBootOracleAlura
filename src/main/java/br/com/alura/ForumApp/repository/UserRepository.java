package br.com.alura.ForumApp.repository;

import br.com.alura.ForumApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}

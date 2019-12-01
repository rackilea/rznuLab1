package hr.fer.rznu.rznulab1.persistence.repository;

import hr.fer.rznu.rznulab1.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

package hr.fer.rznu.rznulab1.persistence.repository;

import hr.fer.rznu.rznulab1.persistence.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByName(String name);
}

package ru.top.smartcity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.top.smartcity.entities.Role;
import ru.top.smartcity.models.ERole;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);

    Boolean existsByName(ERole name);
}

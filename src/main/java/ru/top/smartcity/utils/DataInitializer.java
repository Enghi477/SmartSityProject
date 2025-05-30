package ru.top.smartcity.utils;


import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.top.smartcity.entities.Role;
import ru.top.smartcity.entities.User;
import ru.top.smartcity.models.ERole;
import ru.top.smartcity.repositories.LandmarkRepository;
import ru.top.smartcity.repositories.RoleRepository;
import ru.top.smartcity.repositories.UserRepository;

import java.util.Arrays;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final LandmarkRepository landmarkRepository;

    @PostConstruct
    @Transactional
    public void init() {
        try {
            createRoles();

            createAdminUser();

        } catch (Exception e) {
            System.err.println("Error during data initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createRoles() {
        Arrays.stream(ERole.values()).forEach(role -> {
            if (!roleRepository.existsByName(role)) {
                Role newRole = new Role(role);
                roleRepository.save(newRole);
                System.out.println("Created role: " + role.name());
            }
        });
    }

    private void createAdminUser() {
        String adminEmail = "admin@example.com";
        String adminPassword = "admin123";

        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setFirstname("Admin");
            admin.setLastname("Admin");
            admin.setAge(30);
            admin.setPhone("+1234567890");

            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new IllegalStateException("Admin role not found"));
            admin.setRoles(Set.of(adminRole));

            userRepository.save(admin);
        }
    }
}
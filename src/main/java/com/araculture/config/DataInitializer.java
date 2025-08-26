package com.araculture.config;

import com.araculture.models.User;
import com.araculture.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Crea admin si no existe
        userRepository.findByUsername("admin").orElseGet(() -> {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@araculture.com");
            admin.setPassword(passwordEncoder.encode("Admin123$"));
            admin.setFirstName("Ara");
            admin.setLastName("Admin");
            admin.setAddress("HQ");
            admin.setRoles(Set.of("ROLE_ADMIN", "ROLE_USER"));
            return userRepository.save(admin);
        });
    }
}

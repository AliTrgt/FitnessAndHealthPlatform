package com.example.HealthAndFitnessPlatform.service;

import com.example.HealthAndFitnessPlatform.model.Role;
import com.example.HealthAndFitnessPlatform.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DatabaseLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        createRoleIfNotFound("ROLE_CUSTOMER");
        createRoleIfNotFound("ROLE_MANAGER");
        createRoleIfNotFound("ROLE_ADMIN");
    }

    private void createRoleIfNotFound(String roleName) {
        Role role = roleRepository.findByRole(roleName);
        if (role == null) {
            role = new Role();
            role.setRole(roleName);
            roleRepository.save(role);
        }
    }
}
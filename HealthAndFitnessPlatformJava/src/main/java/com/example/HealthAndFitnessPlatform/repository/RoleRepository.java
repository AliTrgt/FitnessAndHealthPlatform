package com.example.HealthAndFitnessPlatform.repository;

import com.example.HealthAndFitnessPlatform.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    Role findByRole(String role);
}

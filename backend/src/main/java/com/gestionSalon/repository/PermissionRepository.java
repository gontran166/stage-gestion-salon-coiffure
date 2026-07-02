package com.gestionSalon.repository;

import com.gestionSalon.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository
        extends JpaRepository<Permission, Long> {
}
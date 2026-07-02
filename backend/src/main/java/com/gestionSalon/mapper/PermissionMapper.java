package com.gestionSalon.mapper;

import com.gestionSalon.dto.permission.PermissionDTO;
import com.gestionSalon.entity.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {

    public PermissionDTO toDTO(
            Permission permission
    ) {
        return PermissionDTO.builder()
                .id(permission.getId())
                .nom(permission.getNom())
                .build();
    }
}

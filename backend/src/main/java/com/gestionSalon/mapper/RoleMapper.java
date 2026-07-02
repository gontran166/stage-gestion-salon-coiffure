package com.gestionSalon.mapper;


import com.gestionSalon.dto.role.RoleDTO;
import com.gestionSalon.entity.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RoleMapper {
    private final PermissionMapper permissionMapper;

    public RoleDTO toDTO(Role role) {
        return RoleDTO.builder()
                .id(role.getId())
                .nom(role.getNom())
                .permissions(role.getPermissions().stream()
                    .map(permissionMapper::toDTO)
                    .toList())
                .build();
    }
}

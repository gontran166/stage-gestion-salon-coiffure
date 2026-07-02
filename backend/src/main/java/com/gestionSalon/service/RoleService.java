package com.gestionSalon.service;

import com.gestionSalon.dto.role.RoleDTO;
import com.gestionSalon.dto.role.UpdateRolePermissionsDTO;
import com.gestionSalon.entity.Permission;
import com.gestionSalon.entity.Role;
import com.gestionSalon.mapper.RoleMapper;
import com.gestionSalon.repository.PermissionRepository;
import com.gestionSalon.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    // la liste des rôles
    public List<RoleDTO> getAllRoles() {

        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toDTO)
                .toList();
    }

    // changer les permissions d'un rôle
    @Transactional
    public RoleDTO updateRolePermissions(
            Long roleId,
            UpdateRolePermissionsDTO dto
    ) {

        Role role = roleRepository
                .findById(roleId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Rôle introuvable."
                        )
                );

        List<Permission> permissions =
                permissionRepository.findAllById(
                        dto.getPermissionIds()
                );

        if (permissions.size() != dto.getPermissionIds().size()) {

            throw new EntityNotFoundException(
                    "Une ou plusieurs permissions sont introuvables."
            );
        }

        role.setPermissions(permissions);

        roleRepository.save(role);

        return roleMapper.toDTO(role);
    }
}

package com.gestionSalon.controller;


import com.gestionSalon.dto.role.RoleDTO;
import com.gestionSalon.dto.role.UpdateRolePermissionsDTO;
import com.gestionSalon.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {

        return ResponseEntity.ok(
                roleService.getAllRoles()
        );
    }

    @PutMapping("/{id}/permissions")
    public ResponseEntity<RoleDTO> updatePermissions(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRolePermissionsDTO dto
    ) {

        return ResponseEntity.ok(
                roleService.updateRolePermissions(
                        id,
                        dto
                )
        );
    }
}

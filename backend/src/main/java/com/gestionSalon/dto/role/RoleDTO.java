package com.gestionSalon.dto.role;

import com.gestionSalon.dto.permission.PermissionDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoleDTO {

    private Long id;
    private String nom;
    private List<PermissionDTO> permissions;
}
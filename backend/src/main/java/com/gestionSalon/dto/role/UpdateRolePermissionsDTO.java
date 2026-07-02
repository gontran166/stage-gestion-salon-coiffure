package com.gestionSalon.dto.role;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class UpdateRolePermissionsDTO {

    @NotEmpty(message = "Au moins une permission est requise")
    private List<Long> permissionIds;
}
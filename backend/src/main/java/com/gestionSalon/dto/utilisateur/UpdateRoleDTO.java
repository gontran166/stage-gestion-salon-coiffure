package com.gestionSalon.dto.utilisateur;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateRoleDTO {

    @NotBlank(message = "Le rôle est obligatoire")
    private String role;
}
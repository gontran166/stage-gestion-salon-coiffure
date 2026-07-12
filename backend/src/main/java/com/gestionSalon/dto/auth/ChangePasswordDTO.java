package com.gestionSalon.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(
        name = "ChangePasswordDTO",
        description = "Informations nécessaires pour modifier le mot de passe de l'utilisateur authentifié."
)
public class ChangePasswordDTO {

    @Schema(
            description = "Mot de passe actuel de l'utilisateur",
            example = "ancienMotDePasse123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(
            message = "L'ancien mot de passe est obligatoire"
    )
    private String ancienMotDePasse;

    @Schema(
            description = "Nouveau mot de passe souhaité",
            example = "nouveauMotDePasse123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(
            message = "Le nouveau mot de passe est obligatoire"
    )
    private String nouveauMotDePasse;

    @Schema(
            description = "Confirmation du nouveau mot de passe",
            example = "nouveauMotDePasse123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(
            message = "La confirmation est obligatoire"
    )
    private String confirmationMotDePasse;
}
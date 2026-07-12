package com.gestionSalon.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(
        name = "ConnexionDTO",
        description = "Informations nécessaires pour l'authentification d'un utilisateur."
)
public class ConnexionDTO {

    @Schema(
            description = "Numéro de téléphone utilisé comme identifiant de connexion",
            example = "+22670000003",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(
            message = "Le numéro de téléphone est obligatoire"
    )
    private String telephone;

    @Schema(
            description = "Mot de passe du compte utilisateur",
            example = "client123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(
            message = "Le mot de passe est obligatoire"
    )
    private String motDePasse;
}
package com.gestionSalon.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "InscriptionDTO",
        description = "Informations nécessaires pour l'inscription d'un nouveau client."
)
public class InscriptionDTO {

    @Schema(
            description = "Nom de famille du client",
            example = "Ouédraogo"
    )
    private String nom;

    @Schema(
            description = "Prénom du client",
            example = "Adama"
    )
    private String prenom;

    @Schema(
            description = "Numéro de téléphone utilisé pour la connexion",
            example = "+22670000003",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    private String telephone;

    @Schema(
            description = "Mot de passe du compte (minimum 8 caractères)",
            example = "client123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(
            min = 8,
            message = "Le mot de passe doit contenir au moins 8 caractères"
    )
    private String motDePasse;
}
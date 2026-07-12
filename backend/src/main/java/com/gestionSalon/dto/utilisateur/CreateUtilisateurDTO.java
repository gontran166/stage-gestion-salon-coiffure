package com.gestionSalon.dto.utilisateur;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(
        name = "CreateUtilisateurDTO",
        description = """
                Informations nécessaires pour créer manuellement un utilisateur.
                
                Cette opération est réservée au gérant (ADMIN) et permet de créer
                un compte CLIENT, PRESTATAIRE ou ADMIN.
                """
)
public class CreateUtilisateurDTO {

    @Schema(
            description = "Nom de famille de l'utilisateur",
            example = "Sawadogo"
    )
    private String nom;

    @Schema(
            description = "Prénom de l'utilisateur",
            example = "Moussa"
    )
    private String prenom;

    @Schema(
            description = "Numéro de téléphone utilisé comme identifiant de connexion",
            example = "+22670000002",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(
            message = "Le téléphone est obligatoire"
    )
    private String telephone;

    @Schema(
            description = "Mot de passe initial du compte",
            example = "prestataire123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(
            message = "Le mot de passe est obligatoire"
    )
    private String motDePasse;

    @Schema(
            description = "Rôle attribué à l'utilisateur",
            example = "PRESTATAIRE",
            allowableValues = {
                    "ADMIN",
                    "PRESTATAIRE",
                    "CLIENT"
            },
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(
            message = "Le rôle est obligatoire"
    )
    private String role;
}
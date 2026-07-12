package com.gestionSalon.dto.utilisateur;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(
        name = "UpdateUtilisateurDTO",
        description = """
                Informations permettant de modifier un utilisateur existant.
                Réservé au gérant (ADMIN).
                """
)
public class UpdateUtilisateurDTO {

    @Schema(
            description = "Nom de famille de l'utilisateur",
            example = "Sawadogo",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @Schema(
            description = "Prénom de l'utilisateur",
            example = "Moussa",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @Schema(
            description = "Numéro de téléphone utilisé pour la connexion",
            example = "+22670000002",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    /*@Schema(
            description = "Rôle attribué à l'utilisateur",
            example = "PRESTATAIRE",
            allowableValues = {
                    "ADMIN",
                    "PRESTATAIRE",
                    "CLIENT"
            },
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Le rôle est obligatoire")
    private String role;*/
}
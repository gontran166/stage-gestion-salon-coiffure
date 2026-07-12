package com.gestionSalon.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(
        name = "UpdateProfileDTO",
        description = """
                Informations permettant à un utilisateur connecté
                de mettre à jour son propre profil.
                """
)
public class UpdateProfileDTO {

    @Schema(
            description = "Nom de famille de l'utilisateur",
            example = "Ouedraogo",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(
            message = "Le nom est obligatoire"
    )
    private String nom;

    @Schema(
            description = "Prénom de l'utilisateur",
            example = "Adama",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(
            message = "Le prénom est obligatoire"
    )
    private String prenom;

    @Schema(
            description = "Numéro de téléphone utilisé pour la connexion",
            example = "+22670000003",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(
            message = "Le téléphone est obligatoire"
    )
    private String telephone;
}
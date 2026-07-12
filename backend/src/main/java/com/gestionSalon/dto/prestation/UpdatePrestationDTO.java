package com.gestionSalon.dto.prestation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(
        name = "UpdatePrestationDTO",
        description = "Informations nécessaires pour modifier une prestation existante du salon."
)
public class UpdatePrestationDTO {

    @Schema(
            description = "Nom de la prestation",
            example = "Coupe Homme Premium",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(
            message = "Le nom est obligatoire"
    )
    private String nom;

    @Schema(
            description = "Durée estimée de la prestation en minutes",
            example = "45",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
            message = "La durée est obligatoire"
    )
    @Positive(
            message = "La durée doit être positive"
    )
    private Integer dureeMinutes;

    @Schema(
            description = "Prix de la prestation en FCFA",
            example = "5000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
            message = "Le prix est obligatoire"
    )
    @Positive(
            message = "Le prix doit être positif"
    )
    private BigDecimal prix;

    @Schema(
            description = "Catégorie de la prestation",
            example = "Coiffure"
    )
    private String categorie;

    @Schema(
            description = "Description détaillée de la prestation",
            example = "Coupe premium avec shampooing et finition professionnelle."
    )
    private String description;

    @Schema(
            description = "Indique si la prestation est actuellement proposée à la réservation",
            example = "true"
    )
    private Boolean actif;
}
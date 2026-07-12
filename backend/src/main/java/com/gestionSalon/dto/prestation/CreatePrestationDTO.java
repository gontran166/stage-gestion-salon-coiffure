package com.gestionSalon.dto.prestation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(
        name = "CreatePrestationDTO",
        description = "Informations nécessaires pour créer une nouvelle prestation proposée par le salon."
)
public class CreatePrestationDTO {

    @Schema(
            description = "Nom de la prestation",
            example = "Coupe Homme",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(
            message = "Le nom est obligatoire"
    )
    private String nom;

    @Schema(
            description = "Durée estimée de la prestation en minutes",
            example = "30",
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
            example = "3000",
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
            description = "Catégorie de la prestation permettant le regroupement et le filtrage",
            example = "Coiffure"
    )
    private String categorie;

    @Schema(
            description = "Description détaillée de la prestation",
            example = "Coupe classique avec finition et mise en forme."
    )
    private String description;
}
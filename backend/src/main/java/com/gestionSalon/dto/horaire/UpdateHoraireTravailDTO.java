package com.gestionSalon.dto.horaire;

import com.gestionSalon.entity.enumeration.JourSemaine;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
@Schema(
        name = "UpdateHoraireTravailDTO",
        description = "Informations nécessaires pour modifier un horaire de travail existant d'un prestataire."
)
public class UpdateHoraireTravailDTO {

    @Schema(
            description = "Jour de la semaine concerné par l'horaire de travail",
            example = "MARDI",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
            message = "Le jour est obligatoire."
    )
    private JourSemaine jourSemaine;

    @Schema(
            description = "Nouvelle heure de début du travail",
            example = "09:00:00",
            type = "string",
            format = "time",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
            message = "L'heure de début est obligatoire."
    )
    private LocalTime heureDebut;

    @Schema(
            description = "Nouvelle heure de fin du travail",
            example = "18:00:00",
            type = "string",
            format = "time",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
            message = "L'heure de fin est obligatoire."
    )
    private LocalTime heureFin;
}
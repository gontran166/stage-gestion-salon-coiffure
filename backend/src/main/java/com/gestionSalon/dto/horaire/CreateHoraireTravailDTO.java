package com.gestionSalon.dto.horaire;

import com.gestionSalon.entity.enumeration.JourSemaine;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
@Schema(
        name = "CreateHoraireTravailDTO",
        description = "Informations nécessaires pour créer un horaire de travail pour un prestataire."
)
public class CreateHoraireTravailDTO {

    @Schema(
            description = "Jour de la semaine concerné par l'horaire de travail",
            example = "LUNDI",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
            message = "Le jour est obligatoire."
    )
    private JourSemaine jourSemaine;

    @Schema(
            description = "Heure de début du travail",
            example = "08:00:00",
            type = "string",
            format = "time",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
            message = "L'heure de début est obligatoire."
    )
    private LocalTime heureDebut;

    @Schema(
            description = "Heure de fin du travail",
            example = "17:00:00",
            type = "string",
            format = "time",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
            message = "L'heure de fin est obligatoire."
    )
    private LocalTime heureFin;
}
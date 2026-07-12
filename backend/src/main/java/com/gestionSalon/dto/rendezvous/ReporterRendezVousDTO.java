package com.gestionSalon.dto.rendezvous;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Schema(
        name = "ReporterRendezVousDTO",
        description = """
                Informations nécessaires pour reporter un rendez-vous existant.
                
                Utilisé lorsqu'un client souhaite modifier la date ou l'heure
                d'un rendez-vous déjà planifié.
                """
)
public class ReporterRendezVousDTO {

    @Schema(
            description = "Nouvelle date du rendez-vous",
            example = "2026-08-20",
            type = "string",
            format = "date",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
            message = "La date est obligatoire."
    )
    private LocalDate date;

    @Schema(
            description = "Nouvelle heure de début du rendez-vous",
            example = "14:00:00",
            type = "string",
            format = "time",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
            message = "L'heure de début est obligatoire."
    )
    private LocalTime heureDebut;

    @Schema(
            description = "Nouvelle heure de fin du rendez-vous",
            example = "14:30:00",
            type = "string",
            format = "time",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
            message = "L'heure de fin est obligatoire."
    )
    private LocalTime heureFin;
}
package com.gestionSalon.dto.rendezvous;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Schema(
        name = "CreateRendezVousDTO",
        description = "Informations nécessaires pour réserver un rendez-vous auprès d'un prestataire."
)
public class CreateRendezVousDTO {

    @Schema(
            description = "Identifiant de la prestation souhaitée",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
            message = "La prestation est obligatoire."
    )
    private Long prestationId;

    @Schema(
            description = "Identifiant du prestataire sélectionné pour réaliser la prestation",
            example = "2",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
            message = "Le prestataire est obligatoire."
    )
    private Long prestataireId;

    @Schema(
            description = "Date du rendez-vous",
            example = "2026-08-15",
            type = "string",
            format = "date",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
            message = "La date est obligatoire."
    )
    private LocalDate date;

    @Schema(
            description = "Heure de début du créneau réservé",
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
            description = "Heure de fin du créneau réservé",
            example = "09:30:00",
            type = "string",
            format = "time",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
            message = "L'heure de fin est obligatoire."
    )
    private LocalTime heureFin;
}
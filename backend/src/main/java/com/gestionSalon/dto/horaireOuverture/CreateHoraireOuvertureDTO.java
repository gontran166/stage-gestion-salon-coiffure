package com.gestionSalon.dto.horaireOuverture;

import com.gestionSalon.entity.enumeration.JourSemaine;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
@Schema(
        name = "CreateHoraireOuvertureDTO",
        description = "Informations nécessaires pour créer une plage d'ouverture du salon."
)
public class CreateHoraireOuvertureDTO {

    @Schema(
            description = "Jour de la semaine concerné par la plage d'ouverture",
            example = "LUNDI",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(
            message = "Le jour est obligatoire."
    )
    private JourSemaine jourSemaine;

    @Schema(
            description = "Heure de début de l'ouverture du salon",
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
            description = "Heure de fin de l'ouverture du salon",
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
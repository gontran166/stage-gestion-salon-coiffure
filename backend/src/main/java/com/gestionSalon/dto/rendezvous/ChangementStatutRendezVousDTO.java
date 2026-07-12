package com.gestionSalon.dto.rendezvous;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "ChangementStatutRendezVousDTO",
        description = """
                Informations complémentaires associées à un changement de statut d'un rendez-vous.
                
                Utilisé lors de l'annulation, de la validation (HONORE)
                ou du signalement d'absence du client (NO_SHOW).
                """
)
public class ChangementStatutRendezVousDTO {

    @Schema(
            description = "Note ou commentaire laissé par le prestataire ou le client concernant le changement de statut du rendez-vous",
            example = "Client absent au rendez-vous malgré plusieurs tentatives de contact.",
            maxLength = 500
    )
    @Size(
            max = 500,
            message = "La note ne doit pas dépasser 500 caractères."
    )
    private String notes;
}
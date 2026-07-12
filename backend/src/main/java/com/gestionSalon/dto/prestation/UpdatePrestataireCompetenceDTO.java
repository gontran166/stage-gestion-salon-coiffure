package com.gestionSalon.dto.prestation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@Schema(
        name = "UpdatePrestataireCompetenceDTO",
        description = """
                Liste des prestataires auxquels une prestation doit être attribuée.
                
                Utilisé lors de l'association d'une prestation à un ou plusieurs prestataires.
                """
)
public class UpdatePrestataireCompetenceDTO {

    @Schema(
            description = "Liste des identifiants des prestataires qui pourront réaliser la prestation",
            example = "[2, 5, 8]",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotEmpty(
            message = "La liste des prestataires est obligatoire."
    )
    private List<Long> prestataireIds;

}
package com.gestionSalon.dto.prestation;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class UpdatePrestataireCompetenceDTO {

    @NotEmpty(
            message = "La liste des prestations est obligatoire."
    )
    private List<Long> prestataireIds;

}
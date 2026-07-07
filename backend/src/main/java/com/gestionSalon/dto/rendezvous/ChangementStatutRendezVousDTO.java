package com.gestionSalon.dto.rendezvous;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangementStatutRendezVousDTO {

    @Size(max = 500)
    private String notes;
}

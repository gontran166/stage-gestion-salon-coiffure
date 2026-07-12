package com.gestionSalon.dto.prestation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PrestationPopulaireDTO {

    private Long prestationId;
    private String nomPrestation;
    private Long nombreReservations;
}

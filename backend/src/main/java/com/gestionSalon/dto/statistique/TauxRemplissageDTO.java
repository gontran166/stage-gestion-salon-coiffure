package com.gestionSalon.dto.statistique;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TauxRemplissageDTO {

    private Long prestataireId;

    private String nomPrestataire;

    private LocalDate date;

    private Long minutesDisponibles;

    private Long minutesReservees;

    private Double tauxRemplissage;
}

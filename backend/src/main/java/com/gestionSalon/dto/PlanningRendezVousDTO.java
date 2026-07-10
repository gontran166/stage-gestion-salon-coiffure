package com.gestionSalon.dto;

import com.gestionSalon.entity.enumeration.StatutRendezVous;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class PlanningRendezVousDTO {

    private Long rendezVousId;
    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private StatutRendezVous statut;
    private Long clientId;
    private String nomClient;
    private Long prestationId;
    private String nomPrestation;
}

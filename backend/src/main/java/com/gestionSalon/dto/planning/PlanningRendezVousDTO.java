package com.gestionSalon.dto.planning;

import com.gestionSalon.entity.enumeration.JourSemaine;
import com.gestionSalon.entity.enumeration.StatutRendezVous;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class PlanningRendezVousDTO {

    private Long rendezVousId;
    private LocalDate date;
    private JourSemaine jourSemaine;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private StatutRendezVous statut;
    private Long clientId;
    private String nomClient;
    private String telephone;
    private Long prestationId;
    private String nomPrestation;
    private BigDecimal prixPrestation;
}

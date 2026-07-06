package com.gestionSalon.dto.rendezvous;

import com.gestionSalon.entity.enumeration.StatutRendezVous;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class RendezVousDTO {

    private Long id;

    private Long prestationId;
    private String prestationNom;

    private Long prestataireId;
    private String prestataireNom;
    private String prestatairePrenom;

    private LocalDate date;

    private LocalTime heureDebut;
    private LocalTime heureFin;

    private StatutRendezVous statut;
}
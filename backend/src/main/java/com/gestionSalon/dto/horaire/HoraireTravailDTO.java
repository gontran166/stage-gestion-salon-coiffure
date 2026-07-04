package com.gestionSalon.dto.horaire;

import com.gestionSalon.entity.JourSemaine;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class HoraireTravailDTO {

    private Long id;
    private JourSemaine jourSemaine;
    private LocalTime heureDebut;
    private LocalTime heureFin;
}
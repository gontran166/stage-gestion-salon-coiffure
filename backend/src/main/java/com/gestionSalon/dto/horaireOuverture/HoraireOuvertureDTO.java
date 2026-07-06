package com.gestionSalon.dto.horaireOuverture;

import com.gestionSalon.entity.enumeration.JourSemaine;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class HoraireOuvertureDTO {

    private Long id;
    private JourSemaine jourSemaine;
    private LocalTime heureDebut;
    private LocalTime heureFin;
}
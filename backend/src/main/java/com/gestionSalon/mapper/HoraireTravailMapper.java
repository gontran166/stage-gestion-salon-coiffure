package com.gestionSalon.mapper;

import com.gestionSalon.dto.horaire.HoraireTravailDTO;
import com.gestionSalon.entity.HoraireTravail;
import org.springframework.stereotype.Component;

@Component
public class HoraireTravailMapper {

    public HoraireTravailDTO toDTO(HoraireTravail horaire)
    {

        return HoraireTravailDTO.builder()
                .id(horaire.getId())
                .jourSemaine(horaire.getJourSemaine())
                .heureDebut(horaire.getHeureDebut())
                .heureFin(horaire.getHeureFin())
                .build();
    }
}
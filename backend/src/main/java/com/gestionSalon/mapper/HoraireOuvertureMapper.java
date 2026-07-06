package com.gestionSalon.mapper;

import com.gestionSalon.dto.horaireOuverture.HoraireOuvertureDTO;
import com.gestionSalon.entity.HoraireOuverture;
import org.springframework.stereotype.Component;

@Component
public class HoraireOuvertureMapper {

    public HoraireOuvertureDTO toDTO(
            HoraireOuverture horaire
    ) {

        return HoraireOuvertureDTO.builder()
                .id(horaire.getId())
                .jourSemaine(horaire.getJourSemaine())
                .heureDebut(horaire.getHeureDebut())
                .heureFin(horaire.getHeureFin())
                .build();
    }
}
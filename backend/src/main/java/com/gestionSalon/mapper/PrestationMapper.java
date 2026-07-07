package com.gestionSalon.mapper;

import com.gestionSalon.dto.prestation.PrestationDTO;
import com.gestionSalon.entity.Prestation;
import org.springframework.stereotype.Component;

@Component
public class PrestationMapper {

    public PrestationDTO toDTO(Prestation prestation) {

        return PrestationDTO.builder()
                .id(prestation.getId())
                .nom(prestation.getNom())
                .dureeMinutes(prestation.getDureeMinutes())
                .prix(prestation.getPrix())
                .categorie(prestation.getCategorie())
                .description(prestation.getDescription())
                .actif(prestation.getActif())
                .build();
    }
}
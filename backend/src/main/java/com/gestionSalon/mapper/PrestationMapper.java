package com.gestionSalon.mapper;

import com.gestionSalon.dto.prestation.PrestationDTO;
import com.gestionSalon.entity.Prestation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PrestationMapper {

    @Value("${app.base-url}")
    private String baseUrl;

    public PrestationDTO toDTO(Prestation prestation) {

        String url = null;
        if(prestation.getImageUrl() != null){
            url = baseUrl + prestation.getImageUrl();
        }

        return PrestationDTO.builder()
                .id(prestation.getId())
                .nom(prestation.getNom())
                .dureeMinutes(prestation.getDureeMinutes())
                .prix(prestation.getPrix())
                .categorie(prestation.getCategorie())
                .description(prestation.getDescription())
                .imageUrl(url)
                .actif(prestation.getActif())
                .build();
    }
}
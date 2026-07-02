package com.gestionSalon.mapper;

import com.gestionSalon.dto.auth.InscriptionDTO;
import com.gestionSalon.entity.Utilisateur;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationMapper {

    /**
     * Convertit un InscriptionDTO en entité Utilisateur (sans le mot de passe encodé,
     * car l'encodage se fait spécifiquement au niveau du service).
     */
    public Utilisateur toEntity(InscriptionDTO dto) {
        if (dto == null) {
            return null;
        }

        return Utilisateur.builder()
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .telephone(dto.getTelephone())
                .email(dto.getEmail())
                .build();
    }
}

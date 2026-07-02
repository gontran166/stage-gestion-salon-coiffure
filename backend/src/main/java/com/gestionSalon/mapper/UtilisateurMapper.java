package com.gestionSalon.mapper;

import com.gestionSalon.dto.ProfileDTO;
import com.gestionSalon.dto.utilisateur.UtilisateurDTO;
import com.gestionSalon.entity.Utilisateur;
import org.springframework.stereotype.Component;



@Component

public class UtilisateurMapper {

    public ProfileDTO toProfileDTO(Utilisateur utilisateur){
        return ProfileDTO.builder()
                .id(utilisateur.getId())
                .nom(utilisateur.getNom())
                .prenom(utilisateur.getPrenom())
                .telephone(utilisateur.getTelephone())
                .role(utilisateur.getRole().getNom())
                .actif(utilisateur.getActif())
                .build();
    }

    public UtilisateurDTO toUtilisateurDTO(Utilisateur utilisateur){
        return UtilisateurDTO.builder()
                .id(utilisateur.getId())
                .nom(utilisateur.getNom())
                .prenom(utilisateur.getPrenom())
                .telephone(utilisateur.getTelephone())
                .role(utilisateur.getRole().getNom())
                .actif(utilisateur.getActif())
                .build();
    }

}

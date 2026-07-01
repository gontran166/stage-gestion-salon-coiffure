package com.gestionSalon.service;

import com.gestionSalon.dto.ProfileDTO;
import com.gestionSalon.entity.Utilisateur;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    public ProfileDTO getProfil () {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateur =
                (Utilisateur) authentication.getPrincipal();

        return ProfileDTO.builder()
                .id(utilisateur.getId())
                .nom(utilisateur.getNom())
                .prenom(utilisateur.getPrenom())
                .telephone(utilisateur.getTelephone())
                .email(utilisateur.getEmail())
                .role(utilisateur.getRole().getNom())
                .actif(utilisateur.getActif())
                .build();
    }
    
}

package com.gestionSalon.dto.utilisateur;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UtilisateurDTO {

    private Long id;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String role;
    private Boolean actif;
}

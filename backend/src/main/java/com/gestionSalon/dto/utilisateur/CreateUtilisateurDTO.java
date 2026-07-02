package com.gestionSalon.dto.utilisateur;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUtilisateurDTO {

    private String nom;

    private String prenom;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;


    @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;

    @NotBlank(message = "Le rôle est obligatoire")
    private String role;
}
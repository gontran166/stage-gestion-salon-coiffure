package com.gestionSalon.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String role;
    private Boolean actif;
}

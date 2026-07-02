package com.gestionSalon.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProfileDTO {

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    @NotBlank
    private String telephone;

    @Email
    @NotBlank
    private String email;
}
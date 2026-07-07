package com.gestionSalon.dto.prestation;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PrestationDTO {

    private Long id;
    private String nom;
    private Integer dureeMinutes;
    private BigDecimal prix;
    private String categorie;
    private String description;
    private Boolean actif;
}
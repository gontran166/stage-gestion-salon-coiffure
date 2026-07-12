package com.gestionSalon.dto.statistique;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatistiqueAnnulationDTO {

    private Long totalRendezVous;

    private Long annulations;

    private Long noShow;

    private Double tauxAnnulation;
}

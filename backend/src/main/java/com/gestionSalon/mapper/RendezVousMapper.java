package com.gestionSalon.mapper;

import com.gestionSalon.dto.rendezvous.RendezVousDTO;
import com.gestionSalon.entity.RendezVous;
import org.springframework.stereotype.Component;

@Component
public class RendezVousMapper {

    public RendezVousDTO toDTO(RendezVous rendezVous) {

        return RendezVousDTO.builder()
                .id(rendezVous.getId())

                .prestationId(rendezVous.getPrestation().getId())
                .prestationNom(rendezVous.getPrestation().getNom())

                .prestataireId(rendezVous.getPrestataire().getId())
                .prestataireNom(rendezVous.getPrestataire().getNom())
                .prestatairePrenom(rendezVous.getPrestataire().getPrenom())

                .date(rendezVous.getDate())

                .heureDebut(rendezVous.getHeureDebut())
                .heureFin(rendezVous.getHeureFin())

                .statut(rendezVous.getStatut())

                .build();
    }
}
package com.gestionSalon.mapper;

import com.gestionSalon.dto.planning.PlanningRendezVousDTO;
import com.gestionSalon.dto.rendezvous.RendezVousDTO;
import com.gestionSalon.entity.RendezVous;
import com.gestionSalon.service.DisponibiliteService;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Data
public class RendezVousMapper {

    private final DisponibiliteService disponibiliteService;

    public RendezVousDTO toDTO(RendezVous rendezVous) {

        return RendezVousDTO.builder()
                .id(rendezVous.getId())

                .prestationId(rendezVous.getPrestation().getId())
                .prestationNom(rendezVous.getPrestation().getNom())
                .prixPrestation(rendezVous.getPrestation().getPrix())

                .prestataireId(rendezVous.getPrestataire().getId())
                .prestataireNom(rendezVous.getPrestataire().getNom() + " "+rendezVous.getPrestataire().getPrenom())

                .date(rendezVous.getDate())

                .heureDebut(rendezVous.getHeureDebut())
                .heureFin(rendezVous.getHeureFin())

                .statut(rendezVous.getStatut())

                .build();
    }

    public List<PlanningRendezVousDTO> toPlanningRendezVousDTOS(List<RendezVous> rendezVous){

        return rendezVous.stream()
                .map(rdv -> PlanningRendezVousDTO.builder()
                        .rendezVousId(rdv.getId())
                        .date(rdv.getDate())
                        .jourSemaine(disponibiliteService.convertirJour(rdv.getDate()))
                        .heureDebut(rdv.getHeureDebut())
                        .heureFin(rdv.getHeureFin())
                        .statut(rdv.getStatut())

                        .clientId(rdv.getClient().getId())
                        .nomClient(
                                rdv.getClient().getPrenom()
                                        + " "
                                        + rdv.getClient().getNom()
                        )
                        .telephone(rdv.getClient().getTelephone())

                        .prestationId(rdv.getPrestation().getId())
                        .nomPrestation(rdv.getPrestation().getNom())
                        .prixPrestation(rdv.getPrestation().getPrix())
                        .prestataireId(rdv.getPrestataire().getId())
                        .nomPrestataire(rdv.getPrestataire().getNom()+" "+rdv.getPrestataire().getPrenom())
                        .build())
                .toList();
    }
}
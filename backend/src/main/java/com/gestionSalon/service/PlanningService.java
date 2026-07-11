package com.gestionSalon.service;

import com.gestionSalon.dto.planning.PlanningRendezVousDTO;
import com.gestionSalon.entity.RendezVous;
import com.gestionSalon.entity.Utilisateur;
import com.gestionSalon.entity.enumeration.StatutRendezVous;
import com.gestionSalon.mapper.RendezVousMapper;
import com.gestionSalon.repository.RendezVousRepository;
import com.gestionSalon.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@Data
public class PlanningService {

    private final RendezVousRepository rendezVousRepository;
    private final RendezVousMapper rendezVousMapper;
    private final UtilisateurRepository utilisateurRepository;


    public List<PlanningRendezVousDTO> getPlanningJour(
            Long prestataireId,
            LocalDate date
    ){
        List<RendezVous> rendezVous =
                rendezVousRepository
                        .findByPrestataireIdAndDateAndStatutAndSupprimeeFalseOrderByHeureDebutAsc(
                                prestataireId,
                                date,
                                StatutRendezVous.CONFIRME
                        );

        return rendezVousMapper.toPlanningRendezVousDTOS(rendezVous);

    }

    public List<PlanningRendezVousDTO> getPlanningSemaine(
            Long prestataireId,
            LocalDate dateReference
    ){
        LocalDate debutSemaine =
                dateReference.with(DayOfWeek.MONDAY);

        LocalDate finSemaine =
                dateReference.with(DayOfWeek.SUNDAY);

        Utilisateur prestataire = utilisateurRepository.findByIdAndSupprimeeFalse(prestataireId)
                .orElseThrow(()->
                        new EntityNotFoundException("Prestataire introuvable"));

        List<RendezVous> rendezVous =
                rendezVousRepository
                        .findByPrestataireIdAndStatutAndDateBetweenAndSupprimeeFalseOrderByDateAscHeureDebutAsc(
                                prestataireId,
                                StatutRendezVous.CONFIRME,
                                debutSemaine,
                                finSemaine
                        );

        return rendezVousMapper.toPlanningRendezVousDTOS(rendezVous);
    }

}

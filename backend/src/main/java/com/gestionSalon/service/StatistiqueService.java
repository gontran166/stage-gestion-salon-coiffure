package com.gestionSalon.service;

import com.gestionSalon.dto.prestation.PrestationPopulaireDTO;
import com.gestionSalon.dto.statistique.StatistiqueAnnulationDTO;
import com.gestionSalon.dto.statistique.TauxRemplissageDTO;
import com.gestionSalon.entity.HoraireTravail;
import com.gestionSalon.entity.RendezVous;
import com.gestionSalon.entity.Utilisateur;
import com.gestionSalon.entity.enumeration.JourSemaine;
import com.gestionSalon.entity.enumeration.StatutRendezVous;
import com.gestionSalon.repository.HoraireTravailRepository;
import com.gestionSalon.repository.RendezVousRepository;
import com.gestionSalon.repository.UtilisateurRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class StatistiqueService {

    private final RendezVousRepository rendezVousRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final HoraireTravailRepository horaireTravailRepository;
    private final DisponibiliteService disponibiliteService;

    public List<PrestationPopulaireDTO> getPrestationsPopulaires(
            LocalDate debut,
            LocalDate fin
    ) {

        List<Object[]> resultats;

        if (debut != null && fin != null) {

            resultats =
                    rendezVousRepository.getPrestationsPopulairesWithPeriod(
                            debut,
                            fin
                    );

        } else {

            resultats =
                    rendezVousRepository.getPrestationsPopulaires();
        }

        return resultats.stream()
                .map(r -> new PrestationPopulaireDTO(
                        (Long) r[0],
                        (String) r[1],
                        (Long) r[2]
                ))
                .toList();
    }

    public List<TauxRemplissageDTO> getTauxRemplissage( LocalDate date)
    {

        List<Utilisateur> prestataires =
                utilisateurRepository
                        .findByRole_NomAndSupprimeeFalse("PRESTATAIRE");

        JourSemaine jourSemaine = disponibiliteService.convertirJour(date);

        List<TauxRemplissageDTO> resultat = new ArrayList<>();

        for (Utilisateur prestataire : prestataires) {

            // 1. Horaires de travail du prestataire ce jour
            List<HoraireTravail> horairesTravail =
                    horaireTravailRepository
                            .findByPrestataireIdAndJourSemaineAndSupprimeeFalse(
                                    prestataire.getId(),
                                    jourSemaine
                            );

            // 2. Rendez-vous du prestataire à cette date
            List<RendezVous> rendezVous =
                    rendezVousRepository
                            .findByPrestataireIdAndDateAndStatutInAndSupprimeeFalse(
                                    prestataire.getId(),
                                    date,
                                    List.of(
                                            StatutRendezVous.CONFIRME,
                                            StatutRendezVous.HONORE
                                    )
                            );

            // 3. Minutes disponibles
            long minutesDisponibles =
                    horairesTravail.stream()
                            .mapToLong(h ->
                                    Duration.between(
                                            h.getHeureDebut(),
                                            h.getHeureFin()
                                    ).toMinutes()
                            )
                            .sum();

            // 4. Minutes réservées
            long minutesReservees =
                    rendezVous.stream()
                            .mapToLong(r ->
                                    Duration.between(
                                            r.getHeureDebut(),
                                            r.getHeureFin()
                                    ).toMinutes()
                            )
                            .sum();

            // 5. Taux
            double taux =
                    minutesDisponibles == 0
                            ? 0
                            : (minutesReservees * 100.0)
                              / minutesDisponibles;

            resultat.add(
                    TauxRemplissageDTO.builder()
                            .prestataireId(prestataire.getId())
                            .nomPrestataire(
                                    prestataire.getPrenom() + " " +
                                            prestataire.getNom()
                            )
                            .date(date)
                            .minutesDisponibles(minutesDisponibles)
                            .minutesReservees(minutesReservees)
                            .tauxRemplissage(
                                    Math.round(taux * 100.0) / 100.0
                            )
                            .build()
            );
        }

        return resultat;
    }


    public StatistiqueAnnulationDTO getStatistiquesAnnulations(
            LocalDate debut,
            LocalDate fin
    ) {

        long total;
        long annulations;
        long noShow;

        if ((debut == null && fin != null)
                || (debut != null && fin == null)) {

            throw new IllegalArgumentException(
                    "Les dates début et fin doivent être fournies ensemble."
            );
        }

        if (debut != null && fin != null) {

            if (debut.isAfter(fin)) {
                throw new IllegalArgumentException(
                        "La date de début doit être antérieure à la date de fin."
                );
            }

            total =
                    rendezVousRepository
                            .countByDateBetweenAndSupprimeeFalse(
                                    debut,
                                    fin
                            );

            annulations =
                    rendezVousRepository
                            .countByStatutAndDateBetweenAndSupprimeeFalse(
                                    StatutRendezVous.ANNULE,
                                    debut,
                                    fin
                            );

            noShow =
                    rendezVousRepository
                            .countByStatutAndDateBetweenAndSupprimeeFalse(
                                    StatutRendezVous.NO_SHOW,
                                    debut,
                                    fin
                            );

        } else {

            total =
                    rendezVousRepository
                            .countBySupprimeeFalse();

            annulations =
                    rendezVousRepository
                            .countByStatutAndSupprimeeFalse(
                                    StatutRendezVous.ANNULE
                            );

            noShow =
                    rendezVousRepository
                            .countByStatutAndSupprimeeFalse(
                                    StatutRendezVous.NO_SHOW
                            );
        }

        double tauxAnnulation =
                total == 0
                        ? 0.0
                        : (annulations * 100.0) / total;

        return StatistiqueAnnulationDTO.builder()
                .totalRendezVous(total)
                .annulations(annulations)
                .noShow(noShow)
                .tauxAnnulation(
                        Math.round(tauxAnnulation * 100.0) / 100.0
                )
                .build();
    }

}

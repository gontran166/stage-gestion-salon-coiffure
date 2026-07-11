package com.gestionSalon.service;

import com.gestionSalon.dto.CreneauDisponibleDTO;
import com.gestionSalon.entity.*;
import com.gestionSalon.entity.enumeration.JourSemaine;
import com.gestionSalon.entity.enumeration.StatutRendezVous;
import com.gestionSalon.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DisponibiliteService {

    private final PrestationRepository prestationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final HoraireOuvertureRepository horaireOuvertureRepository;
    private final HoraireTravailRepository horaireTravailRepository;
    private final RendezVousRepository rendezVousRepository;

    public JourSemaine convertirJour(LocalDate date) {

        return switch (date.getDayOfWeek()) {
            case MONDAY -> JourSemaine.LUNDI;
            case TUESDAY -> JourSemaine.MARDI;
            case WEDNESDAY -> JourSemaine.MERCREDI;
            case THURSDAY -> JourSemaine.JEUDI;
            case FRIDAY -> JourSemaine.VENDREDI;
            case SATURDAY -> JourSemaine.SAMEDI;
            case SUNDAY -> JourSemaine.DIMANCHE;
        };
    }

    private boolean chevauche(
            LocalTime debut1,
            LocalTime fin1,
            LocalTime debut2,
            LocalTime fin2
    ) {

        return debut1.isBefore(fin2)
                && debut2.isBefore(fin1);
    }

    public List<CreneauDisponibleDTO> getCreneauxDisponibles(
            Long prestationId,
            Long prestataireId,
            LocalDate date
    ) throws BadRequestException {
        // vérifier que la prestation choisie existe
        Prestation prestation = prestationRepository.findByIdAndSupprimeeFalse(prestationId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Prestation introuvable."));

        // Vérifier que le prestataire choisi existe
        Utilisateur prestataire = utilisateurRepository.findById(prestataireId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Prestataire introuvable."));

        // vérifier que le prestataire choisi sait faire la prestation demandée (compétence)
        if (!prestataire.getPrestations().contains(prestation)) {

            throw new BadRequestException(
                    "Le prestataire ne réalise pas cette prestation."
            );
        }

        // Déterminer le jour de la semaine à partir de la date
        JourSemaine jour = convertirJour(date);

        // Vérifier que le salon est ouvert ce jour là sinon retourner une liste vide (pas de créneaux disponibles)
        List<HoraireOuverture> horairesOuverture =
                horaireOuvertureRepository
                        .findByJourSemaineAndSupprimeeFalse(jour);

        if (horairesOuverture.isEmpty()) {
            return List.of();
        }

        // récuperer les horaires de travail du prestataire choisi
        // si pas d'horaire de travail retourner une liste vide
        List<HoraireTravail> horairesTravail =
                horaireTravailRepository
                        .findByPrestataireIdAndJourSemaineAndSupprimeeFalse(
                                prestataire.getId(),
                                jour
                        );

        if (horairesTravail.isEmpty()) {
            return List.of();
        }

        // récuperer les rendez-vous du jour pour le prestataire choisi
        List<RendezVous> rendezVous =
                rendezVousRepository
                        .findByPrestataireAndDateAndStatutAndSupprimeeFalse(
                                prestataire,
                                date,
                                StatutRendezVous.CONFIRME
                        );

        // créer la liste des créneaux disponibles
        List<CreneauDisponibleDTO> disponibilites =
                new ArrayList<>();

        // récuperer la durée de la prestation
        long duree = prestation.getDureeMinutes();

        // construire les créneaux disponibles
        for (HoraireTravail horaire : horairesTravail) {

            LocalTime debut = horaire.getHeureDebut();
            LocalTime fin = horaire.getHeureFin();

            while (!debut.plusMinutes(duree).isAfter(fin)) {

                LocalTime finCreneau =
                        debut.plusMinutes(duree);

                boolean occupe = false;

                for (RendezVous rdv : rendezVous) {

                    if (chevauche(
                            debut,
                            finCreneau,
                            rdv.getHeureDebut(),
                            rdv.getHeureFin()
                    )) {

                        occupe = true;
                        break;
                    }
                }

                if (!occupe) {

                    disponibilites.add(
                            CreneauDisponibleDTO.builder()
                                    .heureDebut(debut)
                                    .heureFin(finCreneau)
                                    .build()
                    );
                }

                debut = finCreneau;
            }
        }

        // retirer les créneaux écoulés si jour courant
        if (date.equals(LocalDate.now())) {

            LocalTime maintenant = LocalTime.now();

            disponibilites.removeIf(
                    creneau ->
                            !creneau.getHeureDebut()
                                    .isAfter(maintenant)
            );
        }

        return disponibilites;
    }

}

package com.gestionSalon.service;

import com.gestionSalon.dto.CreneauDisponibleDTO;
import com.gestionSalon.dto.planning.PlanningRendezVousDTO;
import com.gestionSalon.dto.rendezvous.ChangementStatutRendezVousDTO;
import com.gestionSalon.dto.rendezvous.CreateRendezVousDTO;
import com.gestionSalon.dto.rendezvous.RendezVousDTO;
import com.gestionSalon.dto.rendezvous.ReporterRendezVousDTO;
import com.gestionSalon.entity.Prestation;
import com.gestionSalon.entity.RendezVous;
import com.gestionSalon.entity.Utilisateur;
import com.gestionSalon.entity.enumeration.StatutRendezVous;
import com.gestionSalon.mapper.RendezVousMapper;
import com.gestionSalon.repository.PrestationRepository;
import com.gestionSalon.repository.RendezVousRepository;
import com.gestionSalon.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RendezVousService {

    private final UtilisateurRepository utilisateurRepository;
    private final PrestationRepository prestationRepository;
    private final DisponibiliteService disponibiliteService;
    private final RendezVousRepository rendezVousRepository;
    private final RendezVousMapper rendezVousMapper;

    @Transactional
    public RendezVousDTO createRendezVous(
            CreateRendezVousDTO dto,
            Utilisateur client
    ) throws BadRequestException {

        if (dto.getDate().isBefore(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "La date du rendez-vous ne peut pas être dans le passé."
            );
        }

        if (
                dto.getDate().equals(LocalDate.now())
                        && !dto.getHeureDebut().isAfter(LocalTime.now())
        ) {

            throw new IllegalArgumentException(
                    "Le créneau sélectionné est déjà passé."
            );
        }

        /* Vérification prestation */
        Prestation prestation = prestationRepository.findByIdAndSupprimeeFalse(
                dto.getPrestationId()
        ).orElseThrow(
                () -> new EntityNotFoundException(
                        "Prestation introuvable."
                )
        );

        /* Vérification prestataire */
        Utilisateur prestataire =
                utilisateurRepository.findByIdAndSupprimeeFalse(
                        dto.getPrestataireId()
                ).orElseThrow(
                        () -> new EntityNotFoundException(
                                "Prestataire introuvable."
                        )
                );

        /* Vérification compétence */
        if (!prestataire.getPrestations().contains(prestation)) {

            throw new BadRequestException(
                    "Le prestataire ne réalise pas cette prestation."
            );
        }

        /* Vérification cohérence du créneau */
        LocalTime heureFinAttendue = dto.getHeureDebut().plusMinutes(prestation.getDureeMinutes());
        if (!heureFinAttendue.equals(dto.getHeureFin())) {

            throw new BadRequestException(
                    "Le créneau sélectionné est invalide."
            );
        }

        /* Vérification disponibilité */
        List<CreneauDisponibleDTO> disponibilites =
                disponibiliteService.getCreneauxDisponibles(
                        prestation.getId(),
                        prestataire.getId(),
                        dto.getDate()
                );
        boolean disponible = disponibilites.stream()
                .anyMatch(c ->
                        c.getHeureDebut().equals(dto.getHeureDebut())
                                &&
                                c.getHeureFin().equals(dto.getHeureFin())
                );

        if (!disponible) {

            throw new BadRequestException(
                    "Ce créneau n'est plus disponible."
            );
        }

        /* Création du rendez-vous */
        RendezVous rendezVous = new RendezVous();

        rendezVous.setClient(client);
        rendezVous.setPrestataire(prestataire);
        rendezVous.setPrestation(prestation);

        rendezVous.setDate(dto.getDate());

        rendezVous.setHeureDebut(dto.getHeureDebut());

        rendezVous.setHeureFin(dto.getHeureFin());

        rendezVous.setStatut(StatutRendezVous.CONFIRME);

        // Sauvegarde
        rendezVousRepository.save(rendezVous);

        return rendezVousMapper.toDTO(
                rendezVous
        );
    }

    public List<RendezVousDTO> getMesRendezVousClient(
            Utilisateur utilisateur
    ) {

        return rendezVousRepository
                .findByClientAndSupprimeeFalseOrderByDateAscHeureDebutAsc(
                        utilisateur
                )
                .stream()
                .map(rendezVousMapper::toDTO)
                .toList();
    }


    public List<PlanningRendezVousDTO> getMesRendezVousPrestataire(
            Utilisateur utilisateur,
            LocalDate dateReference
    ) {

        if(dateReference != null){
            LocalDate debutSemaine =
                    dateReference.with(DayOfWeek.MONDAY);

            LocalDate finSemaine =
                    dateReference.with(DayOfWeek.SUNDAY);

            List<RendezVous> rdv = rendezVousRepository
                    .findByPrestataireIdAndDateBetweenAndSupprimeeFalseOrderByDateAscHeureDebutAsc(
                            utilisateur.getId(),
                            debutSemaine,
                            finSemaine
                    );
            return rendezVousMapper.toPlanningRendezVousDTOS(rdv);
        }

        List<RendezVous> rdv = rendezVousRepository
                .findByPrestataireAndSupprimeeFalseOrderByDateAscHeureDebutAsc(
                        utilisateur
                );
        return rendezVousMapper.toPlanningRendezVousDTOS(rdv);

    }

    public List<PlanningRendezVousDTO> getRendezVous(
            Long id
    ) {
        Utilisateur prestataire = utilisateurRepository.findByIdAndSupprimeeFalse(id)
                .orElseThrow(()->
                        new EntityNotFoundException(
                                "Prestataire introuvable."
                        ));

        List<RendezVous> rendezVous = rendezVousRepository.findByPrestataireAndSupprimeeFalseOrderByDateAscHeureDebutAsc(prestataire);

        return rendezVousMapper.toPlanningRendezVousDTOS(rendezVous);

    }

    @Transactional
    public RendezVousDTO reporterRendezVous(
            Long rendezVousId,
            ReporterRendezVousDTO dto,
            Utilisateur client
    ) throws BadRequestException {
        /* Recherche le rendez-vous à modifier */
        RendezVous rendezVous =
                rendezVousRepository.findByIdAndSupprimeeFalse(rendezVousId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Rendez-vous introuvable."
                                ));

        /* Vérifier que le rendez-vous appartient bien au client */
        if (!rendezVous.getClient().getId().equals(client.getId())) {

            throw new AccessDeniedException(
                    "Vous ne pouvez modifier que vos propres rendez-vous."
            );
        }

        /* Vérifier que le rendez-vous n'est déjà annulé ou honoré */
        if (rendezVous.getStatut() == StatutRendezVous.ANNULE || rendezVous.getStatut() == StatutRendezVous.HONORE || rendezVous.getStatut() == StatutRendezVous.NO_SHOW) {

            throw new BadRequestException(
                    "Impossible de reporter ce rendez-vous."
            );
        }

        /* Vérification durée */
        LocalTime heureFinAttendue =
                dto.getHeureDebut()
                        .plusMinutes(
                                rendezVous.getPrestation()
                                        .getDureeMinutes()
                        );

        if (!heureFinAttendue.equals(dto.getHeureFin())) {

            throw new IllegalArgumentException(
                    "Le créneau sélectionné est invalide."
            );
        }

        /* Vérification disponibilité */
        List<CreneauDisponibleDTO> disponibilites =
                disponibiliteService.getCreneauxDisponibles(
                        rendezVous.getPrestation().getId(),
                        rendezVous.getPrestataire().getId(),
                        dto.getDate()
                );
        boolean disponible = disponibilites.stream()
                .anyMatch(c ->
                        c.getHeureDebut().equals(dto.getHeureDebut())
                                &&
                                c.getHeureFin().equals(dto.getHeureFin())
                );

        /* Mise à jour du rendez-vous */
        if (!disponible) {

            throw new IllegalArgumentException(
                    "Ce créneau n'est plus disponible."
            );
        }

        rendezVous.setDate(dto.getDate());
        rendezVous.setHeureDebut(dto.getHeureDebut());
        rendezVous.setHeureFin(dto.getHeureFin());

        /* Sauvegarder le rendez-vous */
        RendezVous rv = rendezVousRepository.save(rendezVous);

        return rendezVousMapper.toDTO(rv);

    }

    // annuler rendez-vous
    public RendezVousDTO annulerRendezVous(
            Long rendezVousId,
            ChangementStatutRendezVousDTO dto,
            Utilisateur client
    ){

        // chercher le rendez-vous à annuler
        RendezVous rendezVous =
                rendezVousRepository.findByIdAndSupprimeeFalse(rendezVousId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Rendez-vous introuvable."
                                ));

        // Vérifier que le rendez-vous concerne bien le client ou le prestataire
        boolean estClient =
                rendezVous.getClient().getId()
                        .equals(client.getId());

        boolean estPrestataire =
                rendezVous.getPrestataire().getId()
                        .equals(client.getId());

        if (!estClient && !estPrestataire) {

            throw new AccessDeniedException(
                    "Vous ne pouvez pas annuler ce rendez-vous."
            );
        }

        // seul les rendez-vous confirmé peuvent être annulé
        if (rendezVous.getStatut() != StatutRendezVous.CONFIRME) {

            throw new IllegalArgumentException(
                    "Seuls les rendez-vous confirmés peuvent être annulés."
            );
        }

        // Vérifier que le rendez-vous n'est pas déjà passé
        LocalDateTime debutRendezVous =
                LocalDateTime.of(
                        rendezVous.getDate(),
                        rendezVous.getHeureDebut()
                );

        if (LocalDateTime.now().isAfter(debutRendezVous)) {

            throw new IllegalArgumentException(
                    "Ce rendez-vous est déjà passé et ne peut plus être annulé."
            );
        }

        // Vérifier le délai minimal de 30 minutes
        LocalDateTime limiteAnnulation =
                debutRendezVous.minusMinutes(30);

        if (LocalDateTime.now().isAfter(limiteAnnulation)) {

            throw new IllegalArgumentException(
                    "L'annulation n'est plus possible moins de 30 minutes avant le rendez-vous."
            );
        }

        // Annulation
        rendezVous.setStatut(StatutRendezVous.ANNULE);

        // si c'est un prestataire, renseigner le champ note
        if(estPrestataire){
            if (dto != null) {
                rendezVous.setNotes(dto.getNotes());
            }
        }

        // Sauvegarde
        RendezVous rv = rendezVousRepository.save(rendezVous);

        return rendezVousMapper.toDTO(rv);

    }

    public RendezVousDTO marquerHonore(
            Long rendezVousId,
            ChangementStatutRendezVousDTO dto,
            Utilisateur client
    ){

        // chercher le rendez-vous à annuler
        RendezVous rendezVous =
                rendezVousRepository.findByIdAndSupprimeeFalse(rendezVousId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Rendez-vous introuvable."
                                ));

        // Vérifier que le rendez-vous concerne bien le prestataire
        if (!rendezVous.getPrestataire().getId().equals(client.getId())) {

            throw new AccessDeniedException(
                    "Vous ne pouvez pas marquer ce rendez-vous compte honoré."
            );
        }

        // seul les rendez-vous confirmé peuvent être marquer no_show
        if (rendezVous.getStatut() != StatutRendezVous.CONFIRME) {

            throw new IllegalArgumentException(
                    "Seuls les rendez-vous confirmés peuvent être marquer honoré."
            );
        }

        // vérifier que le rendez-vous est terminé
        LocalDateTime finRendezVous =
                LocalDateTime.of(
                        rendezVous.getDate(),
                        rendezVous.getHeureFin()
                );

        if (LocalDateTime.now().isBefore(finRendezVous)) {

            throw new IllegalArgumentException(
                    "Le rendez-vous n'est pas encore terminé."
            );
        }

        // marquer honore
        rendezVous.setStatut(StatutRendezVous.HONORE);

        // ajouter la note
        if(dto != null) {
            rendezVous.setNotes(dto.getNotes());
        }

        // Sauvegarde
        RendezVous rv = rendezVousRepository.save(rendezVous);

        return rendezVousMapper.toDTO(rv);

    }

    public RendezVousDTO marquerNO_SHOW(
            Long rendezVousId,
            ChangementStatutRendezVousDTO dto,
            Utilisateur client
    ){

        // chercher le rendez-vous à annuler
        RendezVous rendezVous =
                rendezVousRepository.findByIdAndSupprimeeFalse(rendezVousId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Rendez-vous introuvable."
                                ));

        // Vérifier que le rendez-vous concerne bien le prestataire
        if (!rendezVous.getPrestataire().getId().equals(client.getId())) {

            throw new AccessDeniedException(
                    "Vous ne pouvez pas marquer ce rendez-vous no_show."
            );
        }

        // seul les rendez-vous confirmé peuvent être marquer no_show
        if (rendezVous.getStatut() != StatutRendezVous.CONFIRME) {

            throw new IllegalArgumentException(
                    "Seuls les rendez-vous confirmés peuvent être marquer no_show."
            );
        }

        // vérifier que le rendez-vous est terminé
        LocalDateTime finRendezVous =
                LocalDateTime.of(
                        rendezVous.getDate(),
                        rendezVous.getHeureFin()
                );

        if (LocalDateTime.now().isBefore(finRendezVous)) {

            throw new IllegalArgumentException(
                    "La date ou l'heure du rendez-vous n'est pas encore passé."
            );
        }

        // marquer no_show
        rendezVous.setStatut(StatutRendezVous.NO_SHOW);

        // ajouter la note
        if(dto != null) {
            rendezVous.setNotes(dto.getNotes());
        }

        // Sauvegarde
        RendezVous rv = rendezVousRepository.save(rendezVous);

        return rendezVousMapper.toDTO(rv);

    }


}

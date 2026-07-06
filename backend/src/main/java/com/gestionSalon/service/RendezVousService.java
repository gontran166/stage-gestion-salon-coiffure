package com.gestionSalon.service;

import com.gestionSalon.dto.CreneauDisponibleDTO;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    public List<RendezVousDTO> getMesRendezVous(
            Utilisateur utilisateur
    ) {

        if(utilisateur.getRole().getNom().equals("CLIENT")){
            return rendezVousRepository
                    .findByClientAndSupprimeeFalseOrderByDateAscHeureDebutAsc(
                            utilisateur
                    )
                    .stream()
                    .map(rendezVousMapper::toDTO)
                    .toList();
        }else{
            return rendezVousRepository
                    .findByPrestataireAndSupprimeeFalseOrderByDateAscHeureDebutAsc(
                            utilisateur
                    )
                    .stream()
                    .map(rendezVousMapper::toDTO)
                    .toList();
        }

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

    public RendezVousDTO annulerRendezVous(
            Long rendezVousId,
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

        // Annulation
        rendezVous.setStatut(StatutRendezVous.ANNULE);

        // Sauvegarde
        RendezVous rv = rendezVousRepository.save(rendezVous);

        return rendezVousMapper.toDTO(rv);

    }


    public RendezVousDTO marquerNO_SHOW(
            Long rendezVousId,
            Utilisateur client
    ){

        if(!client.getRole().getNom().equals("PRESTATAIRE")){
            throw new AccessDeniedException(
                    "Vous ne pouvez pas marquer ce rendez-vous no_show."
            );
        }

        // chercher le rendez-vous à annuler
        RendezVous rendezVous =
                rendezVousRepository.findByIdAndSupprimeeFalse(rendezVousId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Rendez-vous introuvable."
                                ));

        // Vérifier que le rendez-vous concerne bien le client ou le prestataire
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

        // marquer no_show
        rendezVous.setStatut(StatutRendezVous.NO_SHOW);

        // Sauvegarde
        RendezVous rv = rendezVousRepository.save(rendezVous);

        return rendezVousMapper.toDTO(rv);

    }


}

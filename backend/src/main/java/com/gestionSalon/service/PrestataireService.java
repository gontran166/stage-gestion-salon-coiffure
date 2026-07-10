package com.gestionSalon.service;

import com.gestionSalon.dto.PlanningRendezVousDTO;
import com.gestionSalon.dto.horaire.CreateHoraireTravailDTO;
import com.gestionSalon.dto.horaire.HoraireTravailDTO;
import com.gestionSalon.dto.horaire.UpdateHoraireTravailDTO;
import com.gestionSalon.dto.prestation.PrestationDTO;
import com.gestionSalon.dto.prestation.UpdatePrestataireCompetenceDTO;
import com.gestionSalon.dto.prestation.UpdatePrestatairePrestationsDTO;
import com.gestionSalon.dto.response.MessageResponse;
import com.gestionSalon.dto.utilisateur.UtilisateurDTO;
import com.gestionSalon.entity.*;
import com.gestionSalon.mapper.HoraireTravailMapper;
import com.gestionSalon.mapper.PrestationMapper;
import com.gestionSalon.mapper.UtilisateurMapper;
import com.gestionSalon.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class PrestataireService {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;
    private final PrestationMapper prestationMapper;
    private final PrestationRepository prestationRepository;
    private final HoraireTravailRepository horaireTravailRepository;
    private final HoraireTravailMapper horaireTravailMapper;
    private final HoraireOuvertureRepository horaireOuvertureRepository;
    private final RendezVousRepository rendezVousRepository;

    private boolean inclusDansHoraireOuverture(
            LocalTime debutTravail,
            LocalTime finTravail,
            LocalTime debutOuverture,
            LocalTime finOuverture
    ) {

        return !debutTravail.isBefore(debutOuverture)
                && !finTravail.isAfter(finOuverture);
    }


    public List<UtilisateurDTO> findPrestataire(){

        return utilisateurRepository
                .findByRole_NomAndSupprimeeFalse("PRESTATAIRE")
                .stream().map(utilisateurMapper::toUtilisateurDTO).toList();
    }

    @Transactional
    public List<PrestationDTO> addPrestations(
            Long prestataireId,
            UpdatePrestatairePrestationsDTO dto
    ) {

        // s'assurer que le prestataire existe
        Utilisateur prestataire =
                utilisateurRepository.findById(prestataireId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Prestataire introuvable."
                                ));

        // s'assurer qu'il possède le rôle prestateur
        if (!"PRESTATAIRE".equals(
                prestataire.getRole().getNom()
        )) {

            throw new IllegalArgumentException(
                    "Cet utilisateur n'est pas un prestataire."
            );
        }

        // s'assurer que les prestations envoyer existent
        List<Prestation> prestations =
                prestationRepository.findByIdInAndSupprimeeFalse(
                        dto.getPrestationIds()
                );

        if (prestations.size()
                != dto.getPrestationIds().size()) {

            throw new IllegalArgumentException(
                    "Une ou plusieurs prestations sont introuvables."
            );
        }

        // recuperer les compétences (prestation associé) existant
        List<Prestation> prestationsActuelles =
                prestataire.getPrestations();

        // Ajouter seulement les nouvelles prestations trouvé dans la liste envoyée
        for (Prestation prestation : prestations) {

            if (!prestationsActuelles.contains(prestation)) {
                prestationsActuelles.add(prestation);
            }

        }

        utilisateurRepository.save(prestataire);

        return prestataire.getPrestations()
                .stream()
                .map(prestationMapper::toDTO)
                .toList();
    }

    @Transactional
    public void addPrestationPrestataires(
            Long prestationId,
            UpdatePrestataireCompetenceDTO dto
    ) throws BadRequestException {

        // s'assurer que le prestataire existe
        Prestation prestation =
                prestationRepository.findById(prestationId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Prestation introuvable."
                                ));


        List<Utilisateur> prestataire =
                utilisateurRepository.findByIdInAndSupprimeeFalse(
                        dto.getPrestataireIds()
                );

        if (prestataire.size()
                != dto.getPrestataireIds().size()) {

            throw new IllegalArgumentException(
                    "Un ou plusieurs prestataires sont introuvables."
            );
        }

        for(Utilisateur utilisateur: prestataire){
            if (!utilisateur.getRole().getNom().equals("PRESTATAIRE")){
                throw new BadRequestException("Certains utilisateurs dans la liste ne sont pas des prestataires");
            }
        }


        for(Utilisateur utilisateur: prestataire){
            List<Prestation> prestationsActuelles =
                    utilisateur.getPrestations();

            if (!prestationsActuelles.contains(prestation)) {
                prestationsActuelles.add(prestation);
            }

            utilisateurRepository.save(utilisateur);
        }
    }

    public List<PrestationDTO> getPrestations(
            Long prestataireId
    ) {

        Utilisateur prestataire =
                utilisateurRepository.findByIdAndSupprimeeFalse(prestataireId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Prestataire introuvable."
                                ));

        if (!"PRESTATAIRE".equals(
                prestataire.getRole().getNom()
        )) {

            throw new IllegalArgumentException(
                    "Cet utilisateur n'est pas un prestataire."
            );
        }

        return prestataire.getPrestations()
                .stream()
                .map(prestationMapper::toDTO)
                .toList();
    }

    @Transactional
    public MessageResponse removePrestation(
            Long prestataireId,
            Long prestationId
    ) {

        Utilisateur prestataire =
                utilisateurRepository.findById(prestataireId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Prestataire introuvable."
                                ));

        if (!"PRESTATAIRE".equals(
                prestataire.getRole().getNom()
        )) {

            throw new IllegalArgumentException(
                    "Cet utilisateur n'est pas un prestataire."
            );
        }

        Prestation prestation =
                prestationRepository.findByIdAndSupprimeeFalse(prestationId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Prestation introuvable."
                                ));

        boolean removed =
                prestataire.getPrestations()
                        .removeIf(p ->
                                p.getId().equals(prestation.getId())
                        );

        if (!removed) {
            throw new IllegalArgumentException(
                    "Cette prestation n'est pas associée au prestataire."
            );
        }

        utilisateurRepository.save(prestataire);

        return MessageResponse.builder()
                .message("Prestation rétirée avec succès.")
                .build();
    }

    private boolean chevauche(LocalTime debut1, LocalTime fin1, LocalTime debut2, LocalTime fin2) {

        return debut1.isBefore(fin2)
                && debut2.isBefore(fin1);
    }

    @Transactional
    public HoraireTravailDTO createHoraireTravail(
            Long prestataireId,
            CreateHoraireTravailDTO dto
    ) {

        // vérifier que le prestataire existe
        Utilisateur prestataire =
                utilisateurRepository.findById(prestataireId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Prestataire introuvable."
                                ));

        // vérifier qu'il possède bien le rôle prestataire
        if (!"PRESTATAIRE".equals(
                prestataire.getRole().getNom()
        )) {

            throw new IllegalArgumentException(
                    "Cet utilisateur n'est pas un prestataire."
            );
        }

        // vérifier que l'heure du début est cohérent avec l'heure de la fin
        if (!dto.getHeureDebut().isBefore(
                dto.getHeureFin()
        )) {

            throw new IllegalArgumentException(
                    "L'heure de début doit être antérieure à l'heure de fin."
            );
        }

        // vérifier que le salon est ouvert pour le jour conserné et que l'horaire de travail est inclu dans l'horaire d'ouverture du salon du jour
        List<HoraireOuverture> horairesOuverture =
                horaireOuvertureRepository
                        .findByJourSemaineAndSupprimeeFalse(
                                dto.getJourSemaine()
                        );

        if (horairesOuverture.isEmpty()) {

            throw new IllegalArgumentException(
                    "Aucun horaire d'ouverture n'est défini pour ce jour."
            );
        }

        boolean inclus = false;

        for (HoraireOuverture ouverture : horairesOuverture) {

            if (inclusDansHoraireOuverture(
                    dto.getHeureDebut(),
                    dto.getHeureFin(),
                    ouverture.getHeureDebut(),
                    ouverture.getHeureFin()
            )) {

                inclus = true;
                break;
            }
        }

        if (!inclus) {

            throw new IllegalArgumentException(
                    "L'horaire de travail doit être inclus dans les horaires d'ouverture du salon."
            );
        }

        // récuperer la liste des horaires de travail existant
        List<HoraireTravail> horairesExistants =
                horaireTravailRepository
                        .findByPrestataireIdAndJourSemaineAndSupprimeeFalse(
                                prestataireId,
                                dto.getJourSemaine()
                        );

        // vérifier qu'il n'y a pas de chevauchement
        for (HoraireTravail horaire : horairesExistants) {

            if (chevauche(
                    dto.getHeureDebut(),
                    dto.getHeureFin(),
                    horaire.getHeureDebut(),
                    horaire.getHeureFin()
            )) {

                throw new IllegalArgumentException(
                        "Ce créneau chevauche un horaire existant."
                );
            }
        }

        HoraireTravail horaire =
                HoraireTravail.builder()
                        .jourSemaine(dto.getJourSemaine())
                        .heureDebut(dto.getHeureDebut())
                        .heureFin(dto.getHeureFin())
                        .prestataire(prestataire)
                        .build();

        horaire.setSupprimee(false);

        horaireTravailRepository.save(horaire);

        return horaireTravailMapper.toDTO(horaire);
    }

    public List<HoraireTravailDTO> getHorairesTravail(
            Long prestataireId
    ) {

        Utilisateur prestataire =
                utilisateurRepository.findById(prestataireId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Prestataire introuvable."
                                ));

        if (!"PRESTATAIRE".equals(
                prestataire.getRole().getNom()
        )) {

            throw new IllegalArgumentException(
                    "Cet utilisateur n'est pas un prestataire."
            );
        }

        return horaireTravailRepository
                .findByPrestataireIdAndSupprimeeFalseOrderByJourSemaineAscHeureDebutAsc(prestataireId)
                .stream()
                .map(horaireTravailMapper::toDTO)
                .toList();
    }

    @Transactional
    public HoraireTravailDTO updateHoraireTravail(
            Long prestataireId,
            Long horaireId,
            UpdateHoraireTravailDTO dto
    ) {

        Utilisateur prestataire =
                utilisateurRepository.findById(prestataireId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Prestataire introuvable."
                                ));

        if (!"PRESTATAIRE".equals(
                prestataire.getRole().getNom()
        )) {

            throw new IllegalArgumentException(
                    "Cet utilisateur n'est pas un prestataire."
            );
        }

        HoraireTravail horaire =
                horaireTravailRepository.findByIdAndSupprimeeFalse(horaireId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Horaire introuvable."
                                ));

        if (!horaire.getPrestataire()
                .getId()
                .equals(prestataireId)) {

            throw new IllegalArgumentException(
                    "Cet horaire n'appartient pas à ce prestataire."
            );
        }

        if (!dto.getHeureDebut().isBefore(
                dto.getHeureFin()
        )) {

            throw new IllegalArgumentException(
                    "L'heure de début doit être antérieure à l'heure de fin."
            );
        }

        // vérifier que le salon est ouvert pour le jour conserné et que l'horaire de travail est inclu dans l'horaire d'ouverture du salon du jour
        List<HoraireOuverture> horairesOuverture =
                horaireOuvertureRepository
                        .findByJourSemaineAndSupprimeeFalse(
                                dto.getJourSemaine()
                        );

        if (horairesOuverture.isEmpty()) {

            throw new IllegalArgumentException(
                    "Aucun horaire d'ouverture n'est défini pour ce jour."
            );
        }

        boolean inclus = false;

        for (HoraireOuverture ouverture : horairesOuverture) {

            if (inclusDansHoraireOuverture(
                    dto.getHeureDebut(),
                    dto.getHeureFin(),
                    ouverture.getHeureDebut(),
                    ouverture.getHeureFin()
            )) {

                inclus = true;
                break;
            }
        }

        if (!inclus) {

            throw new IllegalArgumentException(
                    "L'horaire de travail doit être inclus dans les horaires d'ouverture du salon."
            );
        }

        List<HoraireTravail> horairesExistants =
                horaireTravailRepository
                        .findByPrestataireIdAndJourSemaineAndIdNotAndSupprimeeFalse(
                                prestataireId,
                                dto.getJourSemaine(),
                                horaireId
                        );

        for (HoraireTravail existant : horairesExistants) {

            if (chevauche(
                    dto.getHeureDebut(),
                    dto.getHeureFin(),
                    existant.getHeureDebut(),
                    existant.getHeureFin()
            )) {

                throw new IllegalArgumentException(
                        "Ce créneau chevauche un horaire existant."
                );
            }
        }

        horaire.setJourSemaine(dto.getJourSemaine());
        horaire.setHeureDebut(dto.getHeureDebut());
        horaire.setHeureFin(dto.getHeureFin());

        horaireTravailRepository.save(horaire);

        return horaireTravailMapper.toDTO(horaire);
    }

    @Transactional
    public void deleteHoraireTravail(
            Long prestataireId,
            Long horaireId
    ) {

        Utilisateur prestataire =
                utilisateurRepository.findById(prestataireId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Prestataire introuvable."
                                ));

        if (!"PRESTATAIRE".equals(
                prestataire.getRole().getNom()
        )) {

            throw new IllegalArgumentException(
                    "Cet utilisateur n'est pas un prestataire."
            );
        }

        HoraireTravail horaire =
                horaireTravailRepository.findByIdAndSupprimeeFalse(horaireId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Horaire introuvable."
                                ));

        if (!horaire.getPrestataire()
                .getId()
                .equals(prestataireId)) {

            throw new IllegalArgumentException(
                    "Cet horaire n'appartient pas à ce prestataire."
            );
        }

        horaire.setSupprimee(true);
        horaire.setDateSuppression(LocalDateTime.now());
        horaireTravailRepository.save(horaire);
    }

    public List<PlanningRendezVousDTO> getPlanningJour(
            Long prestataireId,
            LocalDate date,
            Utilisateur utilisateurConnecte
    ){

        if (!utilisateurConnecte.getId().equals(prestataireId)) {
            throw new AccessDeniedException(
                    "Accès refusé au planning d'un autre prestataire."
            );
        }

        List<RendezVous> rendezVous =
                rendezVousRepository
                        .findByPrestataireIdAndDateAndSupprimeeFalseOrderByHeureDebutAsc(
                                prestataireId,
                                date
                        );

        return rendezVous.stream()
                .map(rdv -> PlanningRendezVousDTO.builder()
                        .rendezVousId(rdv.getId())
                        .date(rdv.getDate())
                        .heureDebut(rdv.getHeureDebut())
                        .heureFin(rdv.getHeureFin())
                        .statut(rdv.getStatut())

                        .clientId(rdv.getClient().getId())
                        .nomClient(
                                rdv.getClient().getPrenom()
                                        + " "
                                        + rdv.getClient().getNom()
                        )

                        .prestationId(rdv.getPrestation().getId())
                        .nomPrestation(
                                rdv.getPrestation().getNom()
                        )
                        .build())
                .toList();

    }



}

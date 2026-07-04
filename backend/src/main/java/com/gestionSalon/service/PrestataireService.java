package com.gestionSalon.service;

import com.gestionSalon.dto.horaire.CreateHoraireTravailDTO;
import com.gestionSalon.dto.horaire.HoraireTravailDTO;
import com.gestionSalon.dto.horaire.UpdateHoraireTravailDTO;
import com.gestionSalon.dto.prestation.PrestationDTO;
import com.gestionSalon.dto.prestation.UpdatePrestatairePrestationsDTO;
import com.gestionSalon.dto.response.MessageResponse;
import com.gestionSalon.dto.utilisateur.UtilisateurDTO;
import com.gestionSalon.entity.HoraireTravail;
import com.gestionSalon.entity.Prestation;
import com.gestionSalon.entity.Utilisateur;
import com.gestionSalon.mapper.HoraireTravailMapper;
import com.gestionSalon.mapper.PrestationMapper;
import com.gestionSalon.mapper.UtilisateurMapper;
import com.gestionSalon.repository.HoraireTravailRepository;
import com.gestionSalon.repository.PrestationRepository;
import com.gestionSalon.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<PrestationDTO> getPrestations(
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

}

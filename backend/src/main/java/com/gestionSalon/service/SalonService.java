package com.gestionSalon.service;

import com.gestionSalon.dto.horaireOuverture.CreateHoraireOuvertureDTO;
import com.gestionSalon.dto.horaireOuverture.HoraireOuvertureDTO;
import com.gestionSalon.dto.horaireOuverture.UpdateHoraireOuvertureDTO;
import com.gestionSalon.entity.HoraireOuverture;
import com.gestionSalon.mapper.HoraireOuvertureMapper;
import com.gestionSalon.repository.HoraireOuvertureRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalonService {

    private final HoraireOuvertureRepository horaireOuvertureRepository;
    private final HoraireOuvertureMapper horaireOuvertureMapper;

    private boolean chevauche(
            LocalTime debut1,
            LocalTime fin1,
            LocalTime debut2,
            LocalTime fin2
    ) {

        return debut1.isBefore(fin2)
                && debut2.isBefore(fin1);
    }

    @Transactional
    public HoraireOuvertureDTO createHoraireOuverture(
            CreateHoraireOuvertureDTO dto
    ) {


        // vérifier cohérence entre heure début et heure fin
        if (!dto.getHeureDebut().isBefore(
                dto.getHeureFin()
        )) {

            throw new IllegalArgumentException(
                    "L'heure de début doit être antérieure à l'heure de fin."
            );
        }


        // vérifier chevauchement
        List<HoraireOuverture> horairesExistants =
                horaireOuvertureRepository
                        .findByJourSemaineAndSupprimeeFalse(
                                dto.getJourSemaine()
                        );

        for (HoraireOuverture horaire : horairesExistants) {

            if (chevauche(
                    dto.getHeureDebut(),
                    dto.getHeureFin(),
                    horaire.getHeureDebut(),
                    horaire.getHeureFin()
            )) {

                throw new IllegalArgumentException(
                        "Ce créneau chevauche un horaire d'ouverture existant."
                );
            }
        }

        HoraireOuverture horaire =
                HoraireOuverture.builder()
                        .jourSemaine(dto.getJourSemaine())
                        .heureDebut(dto.getHeureDebut())
                        .heureFin(dto.getHeureFin())
                        .supprimee(false)
                        .build();

        horaireOuvertureRepository.save(horaire);

        return horaireOuvertureMapper.toDTO(horaire);
    }

    public List<HoraireOuvertureDTO> getHorairesOuverture() {

        return horaireOuvertureRepository
                .findBySupprimeeFalseOrderByJourSemaineAscHeureDebutAsc()
                .stream()
                .map(horaireOuvertureMapper::toDTO)
                .toList();
    }

    @Transactional
    public HoraireOuvertureDTO updateHoraireOuverture(
            Long id,
            UpdateHoraireOuvertureDTO dto
    ) {

        HoraireOuverture horaire =
                horaireOuvertureRepository.findByIdAndSupprimeeFalse(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Horaire d'ouverture introuvable."
                                ));


        if (!dto.getHeureDebut().isBefore(
                dto.getHeureFin()
        )) {

            throw new IllegalArgumentException(
                    "L'heure de début doit être antérieure à l'heure de fin."
            );
        }

        List<HoraireOuverture> horairesExistants =
                horaireOuvertureRepository
                        .findByJourSemaineAndSupprimeeFalseAndIdNot(
                                dto.getJourSemaine(),
                                id
                        );

        for (HoraireOuverture existant : horairesExistants) {

            if (chevauche(
                    dto.getHeureDebut(),
                    dto.getHeureFin(),
                    existant.getHeureDebut(),
                    existant.getHeureFin()
            )) {

                throw new IllegalArgumentException(
                        "Ce créneau chevauche un horaire d'ouverture existant."
                );
            }
        }

        horaire.setJourSemaine(dto.getJourSemaine());
        horaire.setHeureDebut(dto.getHeureDebut());
        horaire.setHeureFin(dto.getHeureFin());

        horaireOuvertureRepository.save(horaire);

        return horaireOuvertureMapper.toDTO(horaire);
    }

    @Transactional
    public void deleteHoraireOuverture(Long id) {

        HoraireOuverture horaire =
                horaireOuvertureRepository.findByIdAndSupprimeeFalse(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Horaire d'ouverture introuvable."
                                ));

        horaire.setSupprimee(true);
        horaire.setDateSuppression(LocalDateTime.now());

        horaireOuvertureRepository.save(horaire);
    }


}

package com.gestionSalon.service;

import com.gestionSalon.dto.prestation.CreatePrestationDTO;
import com.gestionSalon.dto.prestation.PrestationDTO;
import com.gestionSalon.dto.prestation.UpdatePrestationDTO;
import com.gestionSalon.dto.response.MessageResponse;
import com.gestionSalon.entity.Prestation;
import com.gestionSalon.entity.Utilisateur;
import com.gestionSalon.mapper.PrestationMapper;
import com.gestionSalon.repository.PrestationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PrestationService {

    private final PrestationRepository prestationRepository;
    private final PrestationMapper prestationMapper;

    public PrestationDTO create(CreatePrestationDTO dto) {

        if (prestationRepository.existsByNomAndSupprimeeFalse(dto.getNom())) {
            throw new IllegalArgumentException(
                    "Cette prestation existe déjà."
            );
        }

        Prestation prestation = Prestation.builder()
                .nom(dto.getNom())
                .dureeMinutes(dto.getDureeMinutes())
                .prix(dto.getPrix())
                .build();

        prestation.setSupprimee(false);

        prestationRepository.save(prestation);

        return prestationMapper.toDTO(prestation);
    }

    public Page<PrestationDTO> findAll(int page, int size)
    {

        Pageable pageable =
                PageRequest.of(page, size);

        return prestationRepository
                .findAllBySupprimeeFalse(pageable)
                .map(prestationMapper::toDTO);
    }

    public PrestationDTO findById(Long id) {

        Prestation prestation =
                prestationRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Prestation introuvable."
                                ));

        return prestationMapper.toDTO(prestation);
    }

    public PrestationDTO update(Long id, UpdatePrestationDTO dto) {

        Prestation prestation =
                prestationRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Prestation introuvable."
                                ));

        prestation.setNom(dto.getNom());
        prestation.setDureeMinutes(dto.getDureeMinutes());
        prestation.setPrix(dto.getPrix());

        prestationRepository.save(prestation);

        return prestationMapper.toDTO(prestation);
    }

    public MessageResponse delete(Long id) {

        Prestation prestation =
                prestationRepository.findByIdAndSupprimeeFalse(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Prestation introuvable."
                                ));

        for (Utilisateur prestataire : prestation.getPrestataires()) {
            prestataire.getPrestations().remove(prestation);
        }

        prestation.getPrestataires().clear();

        prestation.setSupprimee(true);
        prestation.setDateSuppression(LocalDateTime.now());
        prestationRepository.save(prestation);

        return MessageResponse.builder()
                .message("prestation supprimée avec succès.")
                .build();
    }
}
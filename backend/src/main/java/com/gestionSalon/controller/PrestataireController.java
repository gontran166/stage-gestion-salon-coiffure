package com.gestionSalon.controller;


import com.gestionSalon.dto.PlanningRendezVousDTO;
import com.gestionSalon.dto.horaire.CreateHoraireTravailDTO;
import com.gestionSalon.dto.horaire.HoraireTravailDTO;
import com.gestionSalon.dto.horaire.UpdateHoraireTravailDTO;
import com.gestionSalon.dto.prestation.PrestationDTO;
import com.gestionSalon.dto.prestation.UpdatePrestatairePrestationsDTO;
import com.gestionSalon.dto.response.MessageResponse;
import com.gestionSalon.dto.utilisateur.UtilisateurDTO;
import com.gestionSalon.entity.Utilisateur;
import com.gestionSalon.repository.UtilisateurRepository;
import com.gestionSalon.service.PrestataireService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/prestataires")
@RequiredArgsConstructor
public class PrestataireController {

    private final PrestataireService prestataireService;
    private final UtilisateurRepository utilisateurRepository;

    private Utilisateur getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String telephone = authentication.getName();

        return utilisateurRepository
                .findByTelephoneAndSupprimeeFalse(
                        telephone
                )
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Utilisateur introuvable."
                        )
                );
    }

    @GetMapping("/{id}/prestations")
    public ResponseEntity<List<PrestationDTO>>
    getPrestations(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                prestataireService.getPrestations(id)
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UtilisateurDTO>> getPrestataires(){
        return ResponseEntity.ok(
                prestataireService.findPrestataire()
        );
    }

    @PutMapping("/{id}/prestations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PrestationDTO>>
    addPrestations(
            @PathVariable Long id,
            @Valid @RequestBody
            UpdatePrestatairePrestationsDTO dto
    ) {

        return ResponseEntity.ok(
                prestataireService.addPrestations(
                        id,
                        dto
                )
        );
    }


    @DeleteMapping("/{id}/prestations/{prestationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse>
    removePrestation(
            @PathVariable Long id,
            @PathVariable Long prestationId
    ) {

        return ResponseEntity.ok(
                prestataireService.removePrestation(id, prestationId)
        );
    }

    @PostMapping("/{id}/horaires-travail")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HoraireTravailDTO>
    createHoraireTravail(
            @PathVariable Long id,
            @Valid @RequestBody CreateHoraireTravailDTO dto
    ) {

        return ResponseEntity.ok(
                prestataireService.createHoraireTravail(
                        id,
                        dto
                )
        );
    }

    @GetMapping("/{id}/horaires-travail")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HoraireTravailDTO>>
    getHorairesTravail(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                prestataireService.getHorairesTravail(id)
        );
    }

    @PutMapping("/{id}/horaires-travail/{horaireId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HoraireTravailDTO>
    updateHoraireTravail(
            @PathVariable Long id,
            @PathVariable Long horaireId,
            @Valid @RequestBody UpdateHoraireTravailDTO dto
    ) {

        return ResponseEntity.ok(
                prestataireService.updateHoraireTravail(
                        id,
                        horaireId,
                        dto
                )
        );
    }

    @DeleteMapping("/{id}/horaires-travail/{horaireId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse>
    deleteHoraireTravail(
            @PathVariable Long id,
            @PathVariable Long horaireId
    ) {

        prestataireService.deleteHoraireTravail(
                id,
                horaireId
        );

        return ResponseEntity.ok(
                MessageResponse.builder()
                        .message(
                                "Horaire supprimé avec succès."
                        )
                        .build()
        );
    }

    @GetMapping("/{id}/planning/jour")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<List<PlanningRendezVousDTO>> getPlanningJour(
            @PathVariable Long id,
            @RequestParam LocalDate date
    ) {

        return ResponseEntity.ok(
                prestataireService.getPlanningJour(
                        id,
                        date,
                        getCurrentUser()
                )
        );
    }

}

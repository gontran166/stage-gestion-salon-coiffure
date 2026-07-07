package com.gestionSalon.controller;

import com.gestionSalon.dto.rendezvous.ChangementStatutRendezVousDTO;
import com.gestionSalon.dto.rendezvous.CreateRendezVousDTO;
import com.gestionSalon.dto.rendezvous.RendezVousDTO;
import com.gestionSalon.dto.rendezvous.ReporterRendezVousDTO;
import com.gestionSalon.entity.Utilisateur;
import com.gestionSalon.repository.UtilisateurRepository;
import com.gestionSalon.service.RendezVousService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rendez-vous")
@RequiredArgsConstructor
public class RendezVousController {

    private final RendezVousService rendezVousService;
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

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<RendezVousDTO> createRendezVous(
            @Valid @RequestBody CreateRendezVousDTO dto
    ) throws BadRequestException {

        Utilisateur client = getCurrentUser();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        rendezVousService.createRendezVous(
                                dto,
                                client
                        )
                );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENT','PRESTATAIRE')")
    public ResponseEntity<List<RendezVousDTO>>
    getMesRendezVous() {

        return ResponseEntity.ok(
                rendezVousService.getMesRendezVous(
                        getCurrentUser()
                )
        );
    }

    @PatchMapping("/{id}/reporter")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<RendezVousDTO> reporterRendezVous(
            @PathVariable Long id,
            @Valid @RequestBody ReporterRendezVousDTO dto
    ) throws BadRequestException {

        return ResponseEntity.ok(
                rendezVousService.reporterRendezVous(
                        id,
                        dto,
                        getCurrentUser()
                )
        );
    }

    @PatchMapping("/{id}/annuler")
    @PreAuthorize("hasAnyRole('CLIENT','PRESTATAIRE')")
    public ResponseEntity<RendezVousDTO> annulerRendezVous(
            @PathVariable Long id,
            @RequestBody(required = false)
            ChangementStatutRendezVousDTO dto
    ) {

        return ResponseEntity.ok(
                rendezVousService.annulerRendezVous(
                        id,
                        dto,
                        getCurrentUser()
                )
        );
    }

    @PatchMapping("/{id}/honore")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<RendezVousDTO> marquerCommeHonore(
            @PathVariable Long id,
            @RequestBody(required = false)
            ChangementStatutRendezVousDTO dto
    ) {

        return ResponseEntity.ok(
                rendezVousService.marquerHonore(
                        id,
                        dto,
                        getCurrentUser()
                )
        );
    }

    @PatchMapping("/{id}/no-show")
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<RendezVousDTO> marquerCommeNoShow(
            @PathVariable Long id,
            @RequestBody(required = false)
            ChangementStatutRendezVousDTO dto
    ) {

        return ResponseEntity.ok(
                rendezVousService.marquerNO_SHOW(
                        id,
                        dto,
                        getCurrentUser()
                )
        );
    }


}

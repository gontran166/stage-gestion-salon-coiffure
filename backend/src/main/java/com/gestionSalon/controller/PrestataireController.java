package com.gestionSalon.controller;



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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestataires")
@Tag(name = "Gestion des prestataires",description = "CRUD complet plus d'autres fonctionnalités")
@RequiredArgsConstructor
public class PrestataireController {

    private final PrestataireService prestataireService;
    private final UtilisateurRepository utilisateurRepository;


    @GetMapping("/{id}/prestations")
    @Operation(summary = "Récupérer les prestation (compétence) d'un prestataire",description = "Récupérer les prestations qu'un prestataire donnée sait réaliser")
    public ResponseEntity<List<PrestationDTO>>
    getPrestations(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                prestataireService.getPrestations(id)
        );
    }

    @GetMapping
    @Operation(summary = "Récupérer la liste des prestataires", description = "Récupérer tous les prestataires du salon, réserver au gérant")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UtilisateurDTO>> getPrestataires(){
        return ResponseEntity.ok(
                prestataireService.findPrestataire()
        );
    }

    @PutMapping("/{id}/prestations")
    @Operation(summary = "Attribuer des prestations (compétences) à un prestataire", description = "Définir les prestations qu'un prestataire va réaliser. Réserver au gérant")
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
    @Operation(summary = "Retirer une prestation (compétence) à un prestataire", description = "Réserver au gérant")
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
    @Operation(summary = "Définir les horaires de travail d'un prestataire", description = "Réserver au gérant")
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
    @Operation(summary = "Récupérer les horaires de travail d'un prestataire", description = "Réserver au gérant")
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
    @Operation(summary = "Modifier un horaire de travail d'un prestataire", description = "Réserver au gérant")
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
    @Operation(summary = "Supprimer un horaire de travail d'un prestataire", description = "Réserver au gérant")
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

}

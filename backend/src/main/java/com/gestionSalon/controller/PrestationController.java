package com.gestionSalon.controller;

import com.gestionSalon.dto.prestation.CreatePrestationDTO;
import com.gestionSalon.dto.prestation.PrestationDTO;
import com.gestionSalon.dto.prestation.UpdatePrestataireCompetenceDTO;
import com.gestionSalon.dto.prestation.UpdatePrestationDTO;
import com.gestionSalon.dto.response.MessageResponse;
import com.gestionSalon.dto.utilisateur.UtilisateurDTO;
import com.gestionSalon.service.PrestataireService;
import com.gestionSalon.service.PrestationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestations")
@Tag(name = "Gestion des prestations", description = "Gestion des prestations proposées par le salon")
@RequiredArgsConstructor
public class PrestationController {

    private final PrestationService prestationService;
    private final PrestataireService prestataireService;

    @PostMapping
    @Operation(summary = "Ajouter une nouvelle prestation", description = "Réserver au gérant")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PrestationDTO> create(
            @Valid @RequestBody CreatePrestationDTO dto
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(prestationService.create(dto));
    }

    @PutMapping("/{id}/prestataires")
    @Operation(summary = "Attribuer une prestation (compétence) à des prestataires", description = "Réserver au gérant")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse>
    addCompetenceToPrestataires(
            @PathVariable Long id,
            @Valid @RequestBody
            UpdatePrestataireCompetenceDTO dto
    ) throws BadRequestException {

        prestataireService.addPrestationPrestataires(
                id,
                dto
        );

        return ResponseEntity.ok(
                MessageResponse.builder()
                        .message(
                                "Compétence attribué aux prestataires avec succès."
                        )
                        .build()
        );

    }

    @GetMapping
    @Operation(summary = "Récupérer les prestations", description = "Récupérer tous les prestations proposées par le salon, accessible à tout utilisateur connectés")
    public ResponseEntity<Page<PrestationDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                prestationService.findAll(page, size)
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une prestation spécifique", description = "Accessible à tous")
    public ResponseEntity<PrestationDTO> findById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                prestationService.findById(id)
        );
    }

    @GetMapping("/{id}/prestataires")
    @Operation(summary = "Récupérer les prestataires qui savent réaliser une prestation spécifique", description = "Accessible à tous")
    public ResponseEntity<List<UtilisateurDTO>> findPrestataires(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                prestationService.findPrestationsPrestataires(id)
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une prestation",description = "Réserver au  gérant")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PrestationDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePrestationDTO dto
    ) {

        return ResponseEntity.ok(
                prestationService.update(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une prestation",description = "Réserver au gérant")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> delete(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(prestationService.delete(id));
    }
}
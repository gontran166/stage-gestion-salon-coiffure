package com.gestionSalon.controller;

import com.gestionSalon.dto.rendezvous.ChangementStatutRendezVousDTO;
import com.gestionSalon.dto.rendezvous.CreateRendezVousDTO;
import com.gestionSalon.dto.rendezvous.RendezVousDTO;
import com.gestionSalon.dto.rendezvous.ReporterRendezVousDTO;
import com.gestionSalon.entity.Utilisateur;
import com.gestionSalon.repository.UtilisateurRepository;
import com.gestionSalon.service.RendezVousService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "Rendez-vous",
        description = """
                Gestion complète du cycle de vie des rendez-vous.
                
                Ce module permet :
                - la création d'un rendez-vous ;
                - la consultation des rendez-vous du client ;
                - le report d'un rendez-vous ;
                - l'annulation d'un rendez-vous ;
                - la validation d'un rendez-vous honoré ;
                - la déclaration d'un rendez-vous non honoré (No-Show).
                """
)
@RequiredArgsConstructor
public class RendezVousController {

    private final RendezVousService rendezVousService;
    private final UtilisateurRepository utilisateurRepository;

    private Utilisateur getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        assert authentication != null;
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
    @Operation(
            summary = "Créer un rendez-vous",
            description = """
                    Permet à un client authentifié de réserver un créneau disponible.
                    
                    Le système vérifie automatiquement :
                    - la disponibilité du prestataire ;
                    - l'absence de chevauchement ;
                    - la cohérence du créneau sélectionné.
                    
                    Le rendez-vous est créé avec le statut CONFIRME.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Rendez-vous créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Créneau indisponible ou données invalides"),
            @ApiResponse(responseCode = "401", description = "Authentification requise"),
            @ApiResponse(responseCode = "403", description = "Réservé aux clients")
    })
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
    @Operation(
            summary = "Consulter mes rendez-vous",
            description = """
                    Retourne l'ensemble des rendez-vous associés
                    au client actuellement connecté.
                    
                    Les rendez-vous sont retournés quel que soit leur statut
                    (confirmé, honoré, annulé ou no-show).
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Authentification requise"),
            @ApiResponse(responseCode = "403", description = "Réservé aux clients")
    })
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<RendezVousDTO>>
    getMesRendezVous() {

        return ResponseEntity.ok(
                rendezVousService.getMesRendezVous(
                        getCurrentUser()
                )
        );
    }

    @PatchMapping("/{id}/reporter")
    @Operation(
            summary = "Reporter un rendez-vous",
            description = """
                    Permet au client de déplacer un rendez-vous vers
                    une nouvelle date ou un nouveau créneau disponible.
                    
                    Une nouvelle vérification de disponibilité est effectuée.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rendez-vous reporté avec succès"),
            @ApiResponse(responseCode = "400", description = "Nouveau créneau indisponible"),
            @ApiResponse(responseCode = "404", description = "Rendez-vous introuvable"),
            @ApiResponse(responseCode = "403", description = "Réservé au client propriétaire du rendez-vous")
    })
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<RendezVousDTO> reporterRendezVous(

            @Parameter(
                    description = "Identifiant du rendez-vous",
                    example = "15",
                    required = true
            )
            @PathVariable
            Long id,

            @Valid
            @RequestBody
            ReporterRendezVousDTO dto
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
    @Operation(
            summary = "Annuler un rendez-vous",
            description = """
                    Permet l'annulation d'un rendez-vous.
                    
                    Cette opération est autorisée :
                    - au client propriétaire du rendez-vous ;
                    - au prestataire concerné.
                    
                    Une note explicative peut être enregistrée.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rendez-vous annulé avec succès"),
            @ApiResponse(responseCode = "404", description = "Rendez-vous introuvable"),
            @ApiResponse(responseCode = "403", description = "Action non autorisée")
    })
    @PreAuthorize("hasAnyRole('CLIENT','PRESTATAIRE')")
    public ResponseEntity<RendezVousDTO> annulerRendezVous(

            @Parameter(
                    description = "Identifiant du rendez-vous",
                    example = "15",
                    required = true
            )
            @PathVariable
            Long id,

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
    @Operation(
            summary = "Marquer un rendez-vous comme honoré",
            description = """
                    Permet au prestataire concerné de confirmer
                    que la prestation a effectivement été réalisée.
                    
                    Le statut du rendez-vous devient HONORE.
                    Une note facultative peut être enregistrée.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rendez-vous marqué comme honoré"),
            @ApiResponse(responseCode = "404", description = "Rendez-vous introuvable"),
            @ApiResponse(responseCode = "403", description = "Réservé au prestataire concerné")
    })
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<RendezVousDTO> marquerCommeHonore(

            @Parameter(
                    description = "Identifiant du rendez-vous",
                    example = "15",
                    required = true
            )
            @PathVariable
            Long id,

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
    @Operation(
            summary = "Déclarer un client absent",
            description = """
                    Permet au prestataire concerné d'indiquer
                    que le client ne s'est pas présenté au rendez-vous.
                    
                    Le statut du rendez-vous devient NO_SHOW.
                    Une note facultative peut être enregistrée.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rendez-vous marqué comme no-show"),
            @ApiResponse(responseCode = "404", description = "Rendez-vous introuvable"),
            @ApiResponse(responseCode = "403", description = "Réservé au prestataire concerné")
    })
    @PreAuthorize("hasRole('PRESTATAIRE')")
    public ResponseEntity<RendezVousDTO> marquerCommeNoShow(

            @Parameter(
                    description = "Identifiant du rendez-vous",
                    example = "15",
                    required = true
            )
            @PathVariable
            Long id,

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
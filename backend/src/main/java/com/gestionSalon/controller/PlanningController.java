package com.gestionSalon.controller;

import com.gestionSalon.dto.planning.PlanningRendezVousDTO;
import com.gestionSalon.service.PlanningService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(
        name = "Planning",
        description = """
                Consultation des plannings des prestataires et du salon.
                
                Ces endpoints permettent :
                - d'obtenir le planning journalier d'un prestataire ;
                - d'obtenir le planning hebdomadaire d'un prestataire ;
                - d'obtenir une vue consolidée des rendez-vous de tous les prestataires.
                """
)
@RequiredArgsConstructor
public class PlanningController {

    private final PlanningService planningService;

    @GetMapping("/prestataires/{id}/planning/jour")
    @Operation(
            summary = "Consulter le planning journalier d'un prestataire",
            description = """
                    Retourne tous les rendez-vous d'un prestataire pour une date donnée.
                    
                    Cette vue est destinée au prestataire concerné ainsi qu'au gérant
                    afin de consulter l'activité prévue sur une journée.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Planning journalier récupéré avec succès"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prestataire introuvable"
            )
    })
    @PreAuthorize("hasAnyRole('PRESTATAIRE','ADMIN')")
    public ResponseEntity<List<PlanningRendezVousDTO>> getPlanningJour(

            @Parameter(
                    description = "Identifiant du prestataire",
                    example = "2",
                    required = true
            )
            @PathVariable
            Long id,

            @Parameter(
                    description = "Date concernée (format : yyyy-MM-dd)",
                    example = "2026-07-15",
                    required = true
            )
            @RequestParam
            LocalDate date
    ) {

        return ResponseEntity.ok(
                planningService.getPlanningJour(
                        id,
                        date
                )
        );
    }

    @GetMapping("/prestataires/{id}/planning/semaine")
    @Operation(
            summary = "Consulter le planning hebdomadaire d'un prestataire",
            description = """
                    Retourne tous les rendez-vous d'un prestataire pour la semaine
                    contenant la date fournie.
                    
                    Cette vue permet d'obtenir une vision globale de la charge
                    de travail hebdomadaire d'un prestataire.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Planning hebdomadaire récupéré avec succès"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès refusé"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prestataire introuvable"
            )
    })
    @PreAuthorize("hasAnyRole('PRESTATAIRE','ADMIN')")
    public ResponseEntity<List<PlanningRendezVousDTO>> getPlanningSemaine(

            @Parameter(
                    description = "Identifiant du prestataire",
                    example = "2",
                    required = true
            )
            @PathVariable
            Long id,

            @Parameter(
                    description = "Date appartenant à la semaine recherchée (format : yyyy-MM-dd)",
                    example = "2026-07-15",
                    required = true
            )
            @RequestParam
            LocalDate date
    ) {

        return ResponseEntity.ok(
                planningService.getPlanningSemaine(
                        id,
                        date
                )
        );
    }

    @GetMapping("/salon/planning-hebdomadaire")
    @Operation(
            summary = "Consulter le planning hebdomadaire du salon",
            description = """
                    Retourne l'ensemble des rendez-vous de tous les prestataires
                    pour la semaine contenant la date fournie.
                    
                    Cette vue globale permet au gérant de suivre l'activité
                    du salon et la répartition des rendez-vous entre les prestataires.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Planning hebdomadaire du salon récupéré avec succès"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès réservé au gérant"
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PlanningRendezVousDTO>>
    getPlanningHebdomadaire(

            @Parameter(
                    description = "Date appartenant à la semaine recherchée (format : yyyy-MM-dd)",
                    example = "2026-07-15",
                    required = true
            )
            @RequestParam
            LocalDate date
    ) {

        return ResponseEntity.ok(
                planningService.getPlanningHebdomadaire(
                        date
                )
        );
    }

}
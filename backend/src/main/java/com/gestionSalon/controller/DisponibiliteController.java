package com.gestionSalon.controller;

import com.gestionSalon.dto.CreneauDisponibleDTO;
import com.gestionSalon.service.DisponibiliteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/creneaux-disponibles")
@Tag(
        name = "Disponibilités",
        description = """
                Consultation des créneaux disponibles pour la prise de rendez-vous.
                
                Le calcul tient compte :
                - des horaires d'ouverture du salon ;
                - des horaires de travail du prestataire ;
                - de la durée de la prestation ;
                - des rendez-vous déjà confirmés.
                
                Les créneaux retournés peuvent être proposés directement au client
                lors de la réservation d'un rendez-vous.
                """
)
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class DisponibiliteController {

    private final DisponibiliteService disponibiliteService;

    @GetMapping
    @Operation(
            summary = "Obtenir les créneaux disponibles",
            description = """
                    Retourne la liste des créneaux horaires libres pour une prestation,
                    un prestataire et une date donnés.
                    
                    Cette opération est utilisée lors de la prise ou du report
                    d'un rendez-vous afin de proposer uniquement des créneaux disponibles.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des créneaux disponibles récupérée avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Paramètres invalides ou date incohérente"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentification requise"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prestation ou prestataire introuvable"
            )
    })
    public ResponseEntity<List<CreneauDisponibleDTO>>
    getCreneauxDisponibles(

            @Parameter(
                    description = "Identifiant de la prestation souhaitée",
                    example = "1",
                    required = true
            )
            @RequestParam
            Long prestationId,

            @Parameter(
                    description = "Identifiant du prestataire choisi",
                    example = "2",
                    required = true
            )
            @RequestParam
            Long prestataireId,

            @Parameter(
                    description = "Date souhaitée pour le rendez-vous (format : yyyy-MM-dd)",
                    example = "2026-07-20",
                    required = true
            )
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date

    ) throws BadRequestException {

        return ResponseEntity.ok(
                disponibiliteService.getCreneauxDisponibles(
                        prestationId,
                        prestataireId,
                        date
                )
        );
    }
}
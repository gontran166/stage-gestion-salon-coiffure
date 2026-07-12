package com.gestionSalon.controller;

import com.gestionSalon.dto.horaire.CreateHoraireTravailDTO;
import com.gestionSalon.dto.horaire.HoraireTravailDTO;
import com.gestionSalon.dto.horaire.UpdateHoraireTravailDTO;
import com.gestionSalon.dto.prestation.PrestationDTO;
import com.gestionSalon.dto.prestation.UpdatePrestatairePrestationsDTO;
import com.gestionSalon.dto.response.MessageResponse;
import com.gestionSalon.dto.utilisateur.UtilisateurDTO;
import com.gestionSalon.service.PrestataireService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestataires")
@Tag(
        name = "Prestataires",
        description = """
                Gestion des prestataires du salon.
                
                Ce module permet :
                - de consulter les prestataires ;
                - de gérer leurs compétences (prestations réalisées) ;
                - de gérer leurs horaires de travail.
                
                La plupart des opérations sont réservées au gérant du salon.
                """
)
@RequiredArgsConstructor
public class PrestataireController {

    private final PrestataireService prestataireService;

    @GetMapping("/{id}/prestations")
    @Operation(
            summary = "Lister les prestations d'un prestataire",
            description = """
                    Retourne la liste des prestations qu'un prestataire
                    est habilité à réaliser.
                    
                    Cette opération est utilisée notamment lors de la prise
                    de rendez-vous afin de filtrer les prestataires compétents
                    pour une prestation donnée.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Prestations récupérées avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prestataire introuvable"
            )
    })
    public ResponseEntity<List<PrestationDTO>>
    getPrestations(

            @Parameter(
                    description = "Identifiant du prestataire",
                    example = "2",
                    required = true
            )
            @PathVariable
            Long id
    ) {

        return ResponseEntity.ok(
                prestataireService.getPrestations(id)
        );
    }

    @GetMapping
    @Operation(
            summary = "Lister les prestataires",
            description = """
                    Retourne la liste de tous les prestataires du salon.
                    
                    Endpoint réservé au gérant.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des prestataires récupérée avec succès"
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
    public ResponseEntity<List<UtilisateurDTO>> getPrestataires() {

        return ResponseEntity.ok(
                prestataireService.findPrestataire()
        );
    }

    @PutMapping("/{id}/prestations")
    @Operation(
            summary = "Attribuer des prestations à un prestataire",
            description = """
                    Associe une ou plusieurs prestations à un prestataire.
                    
                    Ces prestations représentent les compétences du prestataire.
                    
                    Endpoint réservé au gérant.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Prestations attribuées avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Liste des prestations invalide"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prestataire ou prestation introuvable"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès réservé au gérant"
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PrestationDTO>>
    addPrestations(

            @Parameter(
                    description = "Identifiant du prestataire",
                    example = "2",
                    required = true
            )
            @PathVariable
            Long id,

            @Valid
            @RequestBody
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
    @Operation(
            summary = "Retirer une prestation à un prestataire",
            description = """
                    Retire une compétence précédemment attribuée à un prestataire.
                    
                    Endpoint réservé au gérant.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Prestation retirée avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prestataire ou prestation introuvable"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès réservé au gérant"
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse>
    removePrestation(

            @PathVariable Long id,

            @PathVariable Long prestationId
    ) {

        return ResponseEntity.ok(
                prestataireService.removePrestation(
                        id,
                        prestationId
                )
        );
    }

    @PostMapping("/{id}/horaires-travail")
    @Operation(
            summary = "Créer un horaire de travail",
            description = """
                    Définit un nouvel horaire de travail pour un prestataire.
                    
                    L'horaire doit être inclus dans un horaire d'ouverture
                    existant du même jour.
                    
                    Endpoint réservé au gérant.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Horaire créé avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Horaire invalide ou hors des horaires d'ouverture"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prestataire introuvable"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès réservé au gérant"
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HoraireTravailDTO>
    createHoraireTravail(

            @PathVariable Long id,

            @Valid
            @RequestBody
            CreateHoraireTravailDTO dto
    ) {

        return ResponseEntity.ok(
                prestataireService.createHoraireTravail(
                        id,
                        dto
                )
        );
    }

    @GetMapping("/{id}/horaires-travail")
    @Operation(
            summary = "Lister les horaires de travail",
            description = """
                    Retourne tous les horaires de travail configurés
                    pour un prestataire.
                    
                    Endpoint réservé au gérant.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Horaires récupérés avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prestataire introuvable"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès réservé au gérant"
            )
    })
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
    @Operation(
            summary = "Modifier un horaire de travail",
            description = """
                    Modifie un horaire de travail existant.
                    
                    Endpoint réservé au gérant.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Horaire modifié avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Nouvel horaire invalide"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prestataire ou horaire introuvable"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès réservé au gérant"
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HoraireTravailDTO>
    updateHoraireTravail(

            @PathVariable Long id,

            @PathVariable Long horaireId,

            @Valid
            @RequestBody
            UpdateHoraireTravailDTO dto
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
    @Operation(
            summary = "Supprimer un horaire de travail",
            description = """
                    Supprime un horaire de travail d'un prestataire.
                    
                    Endpoint réservé au gérant.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Horaire supprimé avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prestataire ou horaire introuvable"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès réservé au gérant"
            )
    })
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
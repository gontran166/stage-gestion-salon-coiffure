package com.gestionSalon.controller;

import com.gestionSalon.dto.horaireOuverture.CreateHoraireOuvertureDTO;
import com.gestionSalon.dto.horaireOuverture.HoraireOuvertureDTO;
import com.gestionSalon.dto.horaireOuverture.UpdateHoraireOuvertureDTO;
import com.gestionSalon.dto.response.MessageResponse;
import com.gestionSalon.service.SalonService;
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
@RequestMapping("/api/salon")
@Tag(
        name = "Configuration du salon",
        description = """
                Gestion de la configuration générale du salon.
                
                Ce module permet de gérer les horaires d'ouverture utilisés
                comme référence pour :
                - les horaires de travail des prestataires ;
                - le calcul des disponibilités ;
                - la prise de rendez-vous.
                
                Toutes les opérations sont réservées au gérant.
                """
)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class SalonController {

    private final SalonService salonService;

    @PostMapping("/horaires-ouverture")
    @Operation(
            summary = "Créer un horaire d'ouverture",
            description = """
                    Ajoute une nouvelle plage horaire d'ouverture du salon.
                    
                    Exemple :
                    - Lundi : 08h00 - 12h00
                    - Lundi : 14h00 - 18h00
                    
                    Les horaires de travail des prestataires devront être
                    inclus dans ces plages d'ouverture.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Horaire d'ouverture créé avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Horaire invalide ou chevauchement détecté"
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
    public ResponseEntity<HoraireOuvertureDTO>
    createHoraireOuverture(
            @Valid @RequestBody CreateHoraireOuvertureDTO dto
    ) {

        return ResponseEntity.ok(
                salonService.createHoraireOuverture(dto)
        );
    }

    @GetMapping("/horaires-ouverture")
    @Operation(
            summary = "Lister les horaires d'ouverture",
            description = """
                    Retourne l'ensemble des horaires d'ouverture
                    actuellement configurés pour le salon.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Horaires récupérés avec succès"
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
    public ResponseEntity<List<HoraireOuvertureDTO>>
    getHorairesOuverture() {

        return ResponseEntity.ok(
                salonService.getHorairesOuverture()
        );
    }

    @PutMapping("/horaires-ouverture/{id}")
    @Operation(
            summary = "Modifier un horaire d'ouverture",
            description = """
                    Met à jour une plage horaire d'ouverture existante.
                    
                    Les règles de validation appliquées lors de la création
                    sont également vérifiées lors de la modification.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Horaire d'ouverture modifié avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Horaire invalide ou conflit détecté"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Horaire d'ouverture introuvable"
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
    public ResponseEntity<HoraireOuvertureDTO>
    updateHoraireOuverture(

            @Parameter(
                    description = "Identifiant de l'horaire d'ouverture",
                    example = "1",
                    required = true
            )
            @PathVariable
            Long id,

            @Valid
            @RequestBody
            UpdateHoraireOuvertureDTO dto
    ) {

        return ResponseEntity.ok(
                salonService.updateHoraireOuverture(
                        id,
                        dto
                )
        );
    }

    @DeleteMapping("/horaires-ouverture/{id}")
    @Operation(
            summary = "Supprimer un horaire d'ouverture",
            description = """
                    Supprime définitivement une plage horaire d'ouverture.
                    
                    Cette opération peut impacter les horaires de travail
                    des prestataires qui dépendent de cette plage.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Horaire d'ouverture supprimé avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Horaire d'ouverture introuvable"
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
    public ResponseEntity<MessageResponse>
    deleteHoraireOuverture(

            @Parameter(
                    description = "Identifiant de l'horaire d'ouverture",
                    example = "1",
                    required = true
            )
            @PathVariable
            Long id
    ) {

        salonService.deleteHoraireOuverture(id);

        return ResponseEntity.ok(
                MessageResponse.builder()
                        .message(
                                "Horaire d'ouverture supprimé avec succès."
                        )
                        .build()
        );
    }

}
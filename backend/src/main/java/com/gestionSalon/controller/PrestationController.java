package com.gestionSalon.controller;

import com.gestionSalon.dto.prestation.*;
import com.gestionSalon.dto.response.MessageResponse;
import com.gestionSalon.dto.utilisateur.UtilisateurDTO;
import com.gestionSalon.service.PrestataireService;
import com.gestionSalon.service.PrestationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/prestations")
@Tag(
        name = "Prestations",
        description = """
                Gestion des prestations proposées par le salon.
                
                Ce module permet :
                - de créer et modifier les prestations ;
                - de consulter le catalogue des prestations ;
                - d'associer une prestation à des prestataires ;
                - de gérer les images illustrant les prestations.
                """
)
@RequiredArgsConstructor
public class PrestationController {

    private final PrestationService prestationService;
    private final PrestataireService prestataireService;

    @PostMapping
    @Operation(
            summary = "Créer une prestation",
            description = """
                    Ajoute une nouvelle prestation au catalogue du salon.
                    
                    Endpoint réservé au gérant.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Prestation créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "403", description = "Accès réservé au gérant")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PrestationDTO> create(
            @Valid @RequestBody CreatePrestationDTO dto
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(prestationService.create(dto));
    }

    @PutMapping("/{id}/prestataires")
    @Operation(
            summary = "Associer une prestation à des prestataires",
            description = """
                    Définit quels prestataires sont compétents pour réaliser
                    une prestation donnée.
                    
                    Endpoint réservé au gérant.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prestataires associés avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Prestation ou prestataire introuvable"),
            @ApiResponse(responseCode = "403", description = "Accès réservé au gérant")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse>
    addCompetenceToPrestataires(

            @Parameter(
                    description = "Identifiant de la prestation",
                    example = "1",
                    required = true
            )
            @PathVariable
            Long id,

            @Valid
            @RequestBody
            UpdatePrestataireCompetenceDTO dto

    ) throws BadRequestException {

        prestataireService.addPrestationPrestataires(
                id,
                dto
        );

        return ResponseEntity.ok(
                MessageResponse.builder()
                        .message(
                                "Compétence attribuée aux prestataires avec succès."
                        )
                        .build()
        );
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Lister les prestations",
            description = """
                    Retourne la liste paginée des prestations proposées par le salon.
                    
                    Accessible à tout utilisateur authentifié.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    })
    public ResponseEntity<Page<PrestationDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                prestationService.findAll(page, size)
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Consulter une prestation",
            description = """
                    Retourne les informations détaillées d'une prestation.
                    
                    Accessible à tout utilisateur authentifié.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prestation récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Prestation introuvable")
    })
    public ResponseEntity<PrestationDTO> findById(

            @Parameter(
                    description = "Identifiant de la prestation",
                    example = "1",
                    required = true
            )
            @PathVariable
            Long id
    ) {

        return ResponseEntity.ok(
                prestationService.findById(id)
        );
    }

    @GetMapping("/{id}/prestataires")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Lister les prestataires d'une prestation",
            description = """
                    Retourne les prestataires capables de réaliser
                    la prestation indiquée.
                    
                    Utilisé notamment lors de la prise de rendez-vous.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Prestation introuvable")
    })
    public ResponseEntity<List<UtilisateurDTO>> findPrestataires(

            @Parameter(
                    description = "Identifiant de la prestation",
                    example = "1",
                    required = true
            )
            @PathVariable
            Long id
    ) {

        return ResponseEntity.ok(
                prestationService.findPrestationsPrestataires(id)
        );
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier une prestation",
            description = """
                    Met à jour les informations d'une prestation.
                    
                    Endpoint réservé au gérant.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prestation modifiée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Prestation introuvable"),
            @ApiResponse(responseCode = "403", description = "Accès réservé au gérant")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PrestationDTO> update(

            @PathVariable Long id,

            @Valid
            @RequestBody
            UpdatePrestationDTO dto
    ) {

        return ResponseEntity.ok(
                prestationService.update(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer une prestation",
            description = """
                    Supprime une prestation du catalogue du salon.
                    
                    Endpoint réservé au gérant.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prestation supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Prestation introuvable"),
            @ApiResponse(responseCode = "403", description = "Accès réservé au gérant")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> delete(

            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                prestationService.delete(id)
        );
    }

    @PostMapping("/{id}/image")
    @Operation(
            summary = "Ajouter ou remplacer l'image d'une prestation",
            description = """
                    Upload une image associée à une prestation.
                    
                    Si une image existe déjà, elle est automatiquement remplacée.
                    
                    Endpoint réservé au gérant.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Image enregistrée avec succès"),
            @ApiResponse(responseCode = "400", description = "Fichier invalide"),
            @ApiResponse(responseCode = "404", description = "Prestation introuvable"),
            @ApiResponse(responseCode = "403", description = "Accès réservé au gérant")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImageUploadResponseDTO> uploadImage(

            @PathVariable Long id,

            @RequestParam("file")
            MultipartFile file
    ) {

        return ResponseEntity.ok(
                prestationService.uploadImage(
                        id,
                        file
                )
        );
    }

    @DeleteMapping("/{id}/image")
    @Operation(
            summary = "Supprimer l'image d'une prestation",
            description = """
                    Supprime l'image associée à une prestation ainsi que
                    le fichier physique stocké sur le serveur.
                    
                    Endpoint réservé au gérant.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Image supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Prestation ou image introuvable"),
            @ApiResponse(responseCode = "403", description = "Accès réservé au gérant")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteImage(

            @PathVariable Long id
    ) {

        prestationService.deleteImage(id);

        return ResponseEntity.noContent()
                .build();
    }
}
package com.gestionSalon.controller;

import com.gestionSalon.dto.ProfileDTO;
import com.gestionSalon.dto.UpdateProfileDTO;
import com.gestionSalon.dto.response.MessageResponse;
import com.gestionSalon.dto.utilisateur.CreateUtilisateurDTO;
import com.gestionSalon.dto.utilisateur.UpdateRoleDTO;
import com.gestionSalon.dto.utilisateur.UpdateUtilisateurDTO;
import com.gestionSalon.dto.utilisateur.UtilisateurDTO;
import com.gestionSalon.service.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(
        name = "Gestion des utilisateurs",
        description = """
                Gestion des comptes utilisateurs du système.
                
                Ce module permet :
                - à chaque utilisateur de consulter et modifier son profil ;
                - au gérant de gérer les comptes utilisateurs ;
                - d'attribuer ou modifier les rôles des utilisateurs.
                """
)
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @GetMapping("/profile")
    @Operation(
            summary = "Consulter son profil",
            description = """
                    Retourne les informations du profil de l'utilisateur
                    actuellement authentifié.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profil récupéré avec succès"),
            @ApiResponse(responseCode = "401", description = "Authentification requise")
    })
    public ResponseEntity<ProfileDTO> getProfile() {

        return ResponseEntity.ok(
                utilisateurService.getProfil()
        );
    }

    @PutMapping("/profile")
    @Operation(
            summary = "Modifier son profil",
            description = """
                    Permet à l'utilisateur connecté de mettre à jour
                    les informations de son profil.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profil mis à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "401", description = "Authentification requise")
    })
    public ResponseEntity<ProfileDTO> updateProfile(
            @Valid @RequestBody UpdateProfileDTO dto
    ) {

        return ResponseEntity.ok(
                utilisateurService.updateProfile(dto)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(
            summary = "Lister les utilisateurs",
            description = """
                    Retourne la liste paginée de tous les utilisateurs
                    enregistrés dans le système.
                    
                    Accès réservé au gérant.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Authentification requise"),
            @ApiResponse(responseCode = "403", description = "Accès réservé au gérant")
    })
    public ResponseEntity<Page<UtilisateurDTO>> getAllUsers(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "20")
            int size
    ) {

        return ResponseEntity.ok(
                utilisateurService.getAllUsers(
                        page,
                        size
                )
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @Operation(
            summary = "Consulter un utilisateur",
            description = """
                    Retourne les informations détaillées
                    d'un utilisateur à partir de son identifiant.
                    
                    Accès réservé au gérant.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur trouvé"),
            @ApiResponse(responseCode = "404", description = "Utilisateur introuvable"),
            @ApiResponse(responseCode = "401", description = "Authentification requise"),
            @ApiResponse(responseCode = "403", description = "Accès réservé au gérant")
    })
    public ResponseEntity<UtilisateurDTO> getUserById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                utilisateurService.getUserById(id)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(
            summary = "Créer un utilisateur",
            description = """
                    Permet au gérant de créer manuellement
                    un nouvel utilisateur.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "401", description = "Authentification requise"),
            @ApiResponse(responseCode = "403", description = "Accès réservé au gérant")
    })
    public ResponseEntity<UtilisateurDTO> createUser(
            @Valid
            @RequestBody
            CreateUtilisateurDTO dto
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        utilisateurService.createUser(dto)
                );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(
            summary = "Modifier un utilisateur",
            description = """
                    Permet au gérant de mettre à jour
                    les informations d'un utilisateur.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur modifié avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Utilisateur introuvable"),
            @ApiResponse(responseCode = "401", description = "Authentification requise"),
            @ApiResponse(responseCode = "403", description = "Accès réservé au gérant")
    })
    public ResponseEntity<UtilisateurDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUtilisateurDTO dto
    ) {

        return ResponseEntity.ok(
                utilisateurService.updateUser(id, dto)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer un utilisateur",
            description = """
                    Supprime logiquement un utilisateur
                    afin qu'il ne puisse plus accéder à l'application.
                    
                    Accès réservé au gérant.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur introuvable"),
            @ApiResponse(responseCode = "401", description = "Authentification requise"),
            @ApiResponse(responseCode = "403", description = "Accès réservé au gérant")
    })
    public ResponseEntity<MessageResponse> deleteUser(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                utilisateurService.deleteUser(id)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/role")
    @Operation(
            summary = "Modifier le rôle d'un utilisateur",
            description = """
                    Permet de changer le rôle attribué à un utilisateur.
                    
                    Exemples :
                    - CLIENT
                    - PRESTATAIRE
                    - ADMIN
                    
                    Accès réservé au gérant.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rôle modifié avec succès"),
            @ApiResponse(responseCode = "400", description = "Rôle invalide"),
            @ApiResponse(responseCode = "404", description = "Utilisateur ou rôle introuvable"),
            @ApiResponse(responseCode = "401", description = "Authentification requise"),
            @ApiResponse(responseCode = "403", description = "Accès réservé au gérant")
    })
    public ResponseEntity<UtilisateurDTO> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoleDTO dto
    ) {

        return ResponseEntity.ok(
                utilisateurService.updateUserRole(
                        id,
                        dto
                )
        );
    }

}
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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Gestion des utilisateurs", description = "Gestion des profils utilisateurs et administration des comptes")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @GetMapping("/profile")
    @Operation(
            summary = "Récupérer son propre profil",
            description = "Renvoie les informations du profil de l'utilisateur actuellement authentifié."
    )
    public ResponseEntity<ProfileDTO> getProfile() {
        return ResponseEntity.ok(utilisateurService.getProfil());
    }

    @PutMapping("/profile")
    @Operation(
            summary = "Mettre à jour son propre profil",
            description = "Permet à l'utilisateur connecté de modifier les informations de son compte."
    )
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
            summary = "Lister tous les utilisateurs (Paginé)",
            description = "Récupère la liste de tous les utilisateurs inscrits sous forme de page. Réservé aux ADMINISTRATEURS."
    )
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
            summary = "Récupérer un utilisateur par son ID",
            description = "Cherche et renvoie les détails d'un compte utilisateur spécifique. Réservé aux ADMINISTRATEURS."
    )
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
            summary = "Créer un nouvel utilisateur",
            description = "Permet à un administrateur d'enregistrer manuellement un nouvel utilisateur en base."
    )
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
            description = "Permet à un administrateur de mettre à jour globalement les informations d'un compte. Réservé aux ADMINISTRATEURS."
    )
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
            description = "Désactive ou supprime définitivement un utilisateur du système. Réservé aux ADMINISTRATEURS."
    )
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
            summary = "Changer le rôle d'un utilisateur",
            description = "Permet d'attribuer un nouveau groupe ou rôle de sécurité à un compte utilisateur. Réservé aux ADMINISTRATEURS."
    )
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

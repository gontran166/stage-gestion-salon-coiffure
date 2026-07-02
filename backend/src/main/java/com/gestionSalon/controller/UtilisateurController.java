package com.gestionSalon.controller;

import com.gestionSalon.dto.ProfileDTO;
import com.gestionSalon.dto.UpdateProfileDTO;
import com.gestionSalon.dto.response.MessageResponse;
import com.gestionSalon.dto.utilisateur.CreateUtilisateurDTO;
import com.gestionSalon.dto.utilisateur.UpdateRoleDTO;
import com.gestionSalon.dto.utilisateur.UpdateUtilisateurDTO;
import com.gestionSalon.dto.utilisateur.UtilisateurDTO;
import com.gestionSalon.service.UtilisateurService;
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
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> getProfile() {
        return ResponseEntity.ok(utilisateurService.getProfil());
    }

    @PutMapping("/profile")
    public ResponseEntity<ProfileDTO> updateProfile(
            @Valid @RequestBody UpdateProfileDTO dto
    ) {
        return ResponseEntity.ok(
                utilisateurService.updateProfile(dto)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
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
    public ResponseEntity<UtilisateurDTO> getUserById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                utilisateurService.getUserById(id)
        );
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
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
    public ResponseEntity<MessageResponse> deleteUser(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                utilisateurService.deleteUser(id)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/role")
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

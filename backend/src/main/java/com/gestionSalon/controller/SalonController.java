package com.gestionSalon.controller;

import com.gestionSalon.dto.horaireOuverture.CreateHoraireOuvertureDTO;
import com.gestionSalon.dto.horaireOuverture.HoraireOuvertureDTO;
import com.gestionSalon.dto.horaireOuverture.UpdateHoraireOuvertureDTO;
import com.gestionSalon.dto.response.MessageResponse;
import com.gestionSalon.service.SalonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salon")
@Tag(name = "Gestion des horaires d'ouverture du salon", description = "Gestion de la configuration du salon (Horaires d'ouverture, fermetures). Réservé au gérant.")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class SalonController {

    private final SalonService salonService;

    @PostMapping("/horaires-ouverture")
    @Operation(
            summary = "Ajouter un horaire d'ouverture",
            description = "Permet de définir une nouvelle plage horaire d'ouverture pour le salon."
    )
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
            summary = "Récupérer tous les horaires d'ouverture",
            description = "Renvoie la liste complète des plages horaires paramétrées pour le salon."
    )
    public ResponseEntity<List<HoraireOuvertureDTO>>
    getHorairesOuverture() {

        return ResponseEntity.ok(
                salonService.getHorairesOuverture()
        );
    }

    @PutMapping("/horaires-ouverture/{id}")
    @Operation(
            summary = "Modifier un horaire d'ouverture",
            description = "Permet de mettre à jour une plage horaire existante via son identifiant."
    )
    public ResponseEntity<HoraireOuvertureDTO>
    updateHoraireOuverture(
            @PathVariable Long id,
            @Valid @RequestBody UpdateHoraireOuvertureDTO dto
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
            description = "Retire définitivement une plage horaire d'ouverture de la configuration."
    )
    public ResponseEntity<MessageResponse>
    deleteHoraireOuverture(
            @PathVariable Long id
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

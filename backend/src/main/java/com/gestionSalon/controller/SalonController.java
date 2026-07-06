package com.gestionSalon.controller;

import com.gestionSalon.dto.horaireOuverture.CreateHoraireOuvertureDTO;
import com.gestionSalon.dto.horaireOuverture.HoraireOuvertureDTO;
import com.gestionSalon.dto.horaireOuverture.UpdateHoraireOuvertureDTO;
import com.gestionSalon.dto.response.MessageResponse;
import com.gestionSalon.service.SalonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salon")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class SalonController {

    private final SalonService salonService;

    @PostMapping("/horaires-ouverture")
    public ResponseEntity<HoraireOuvertureDTO>
    createHoraireOuverture(
            @Valid @RequestBody CreateHoraireOuvertureDTO dto
    ) {

        return ResponseEntity.ok(
                salonService.createHoraireOuverture(dto)
        );
    }

    @GetMapping("/horaires-ouverture")
    public ResponseEntity<List<HoraireOuvertureDTO>>
    getHorairesOuverture() {

        return ResponseEntity.ok(
                salonService.getHorairesOuverture()
        );
    }

    @PutMapping("/horaires-ouverture/{id}")
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
